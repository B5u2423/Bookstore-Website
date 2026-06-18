package dev.vubl.bookstore.services;

import dev.vubl.bookstore.dtos.BookDTO;
import dev.vubl.bookstore.dtos.LandingBookCollectionResponse;
import dev.vubl.bookstore.entities.Book;
import dev.vubl.bookstore.entities.Category;
import dev.vubl.bookstore.entities.Collection;
import dev.vubl.bookstore.exceptions.BookDoesNotExistException;
import dev.vubl.bookstore.exceptions.BookWithIsbnAlreadyExists;
import dev.vubl.bookstore.exceptions.CategoryDoesNotExistException;
import dev.vubl.bookstore.exceptions.CollectionDoesNotExistException;
import dev.vubl.bookstore.mappers.BookMapper;
import dev.vubl.bookstore.repos.BookRepo;
import dev.vubl.bookstore.repos.CategoryRepo;
import dev.vubl.bookstore.repos.CollectionRepo;
import dev.vubl.bookstore.utils.SlugUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BookService {
  @PersistenceContext private final EntityManager em;
  private final BookRepo bookRepo;
  private final CategoryRepo categoryRepo;
  private final CloudinaryService cloudinaryService;
  private final CollectionRepo collectionRepo;

  public List<BookDTO> getAllBooks() {
    return bookRepo.findAll().stream().map(this::toDtoWrapper).toList();
  }

  public Page<BookDTO> getAllBooksPaginated(int page, int size, String sortBy, String order) {
    List<String> allowed = List.of("id");
    if (!allowed.contains(sortBy)) {
      throw new IllegalArgumentException("Invalid sort field: %s".formatted(sortBy));
    }

    Sort sort = order.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
    Pageable pageable = PageRequest.of(page, size, sort);
    Page<Book> books = bookRepo.findAll(pageable);
    return books.map(this::toDtoWrapper);
  }

  public BookDTO getBookById(Integer id) {
    Book b = bookRepo.findById(id).orElseThrow(BookDoesNotExistException::new);
    return toDtoWrapper(b);
  }

  public BookDTO addNewBook(BookDTO bookDTO, MultipartFile image) throws IOException {
    String isbn = bookDTO.isbn();
    if (isIsbnNotUnique(isbn)) {
      throw new BookWithIsbnAlreadyExists("Book with isbn :: %s already exists!".formatted(isbn));
    }

    Book b = toBookWrapper(bookDTO);
    try {
      if (image != null) {
        log.info("[{}] Uploading image...", this.getClass().getName());
        String returnedUrl = cloudinaryService.uploadImage(image);
        b.setImageUrl(returnedUrl);
      }
      log.info("[{}] Adding new book", this.getClass().getName());
      return toDtoWrapper(bookRepo.save(b));
    } catch (DataIntegrityViolationException e) {
      throw new DataIntegrityViolationException("Error adding or updating new book!", e);
    } catch (IOException e) {
      log.info("[{}] Error uploading image", this.getClass().getName());
      throw new IOException("Error uploading book image to cloud");
    }
  }

  public BookDTO updateBookById(BookDTO bookDTO, MultipartFile image, Integer id) {
    try {
      Book b =
          bookRepo
              .findById(id)
              .orElseThrow(
                  () ->
                      new BookDoesNotExistException(
                          "Book with id %d does not exist".formatted(id)));

      // update
      b.setTitle(bookDTO.title());
      b.setAuthor(bookDTO.author());
      b.setPublisher(bookDTO.publisher());
      b.setPublishYear(bookDTO.publishYear());
      b.setPageCount(bookDTO.pageCount());
      b.setIsbn(bookDTO.isbn());
      b.setDescription(bookDTO.description());
      b.setAuthor(bookDTO.author());
      if (image != null) {
        String returnedUrl = cloudinaryService.uploadImage(image);
        b.setImageUrl(returnedUrl);
      } else if (!b.getImageUrl().equals(bookDTO.imageUrl())) {
        b.setImageUrl(bookDTO.imageUrl());
      }
      b.setInStock(bookDTO.inStock());
      b.setUrlSlug(SlugUtils.convertStringToSlug(bookDTO.title()));
      b.setPrice(bookDTO.price());
      b.setUpdateTimeStamp(Instant.now());
      if (bookDTO.categoryId() != null) {

        b.setCategory(
            categoryRepo
                .findById(bookDTO.categoryId())
                .orElseThrow(CategoryDoesNotExistException::new));
      }
      if (bookDTO.collectionId() != null) {

        b.setCollection(
            collectionRepo
                .findById(bookDTO.collectionId())
                .orElseThrow(() -> new IllegalArgumentException("Collection ID does not exist!")));
      }

      Book savedBook = bookRepo.save(b);
      return toDtoWrapper(savedBook);

    } catch (DataIntegrityViolationException e) {
      throw new DataIntegrityViolationException("Error adding or updating new book!", e);
    } catch (IOException e) {
      throw new RuntimeException("Error uploading image when updating book");
    }
  }

  public void deleteBookById(Integer id) {
    bookRepo.deleteById(id);
    log.info("[{}] Book with id {} deleted", this.getClass().getName(), id);
  }

  public Page<BookDTO> getBookByCategory(
      String slug, int page, int size, String sortBy, String order) {
    List<String> allowed = List.of("id");
    if (!allowed.contains(sortBy)) {
      throw new IllegalArgumentException("Invalid sort field: %s".formatted(sortBy));
    }
    // get children of category
    Category c =
        categoryRepo.findByCategorySlug(slug).orElseThrow(CategoryDoesNotExistException::new);
    List<Category> categories = new ArrayList<>();
    categories.add(c);
    if (c.getChildrenCategories() != null) {
      categories.addAll(c.getChildrenCategories());
    }
    // query all books from children
    Sort sort = order.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
    Pageable pageable = PageRequest.of(page, size, sort);
    Page<Book> bookPage = bookRepo.findAllByCategoryIn(categories, pageable);
    return bookPage.map(this::toDtoWrapper);
  }

  @Deprecated
  public List<BookDTO> searchBookV2(String keyword) {
    String[] tokens = keyword.toLowerCase().split("\\s+");

    StringBuilder jpql = new StringBuilder("SELECT b FROM Book b WHERE 1=1");

    for (int i = 0; i < tokens.length; i++) {
      jpql.append(
          """
        AND (
          LOWER(b.title) LIKE :t%1$d
          OR LOWER(b.urlSlug) LIKE :t%1$d
        )
      """
              .formatted(i));
    }

    TypedQuery<Book> query = em.createQuery(jpql.toString(), Book.class);

    for (int i = 0; i < tokens.length; i++) {
      query.setParameter("t" + i, "%" + tokens[i] + "%");
    }
    return query.getResultList().stream().map(this::toDtoWrapper).toList();
  }

  public List<BookDTO> searchBookV3(String keyword) {
    return bookRepo.searchBookV3(keyword).stream().map(this::toDtoWrapper).toList();
  }

  public List<BookDTO> getAllBooksInCollection(String collectionSlug) {
    Optional<Collection> res = collectionRepo.findByCollectionSlug(collectionSlug);
    if (res.isEmpty()) {
      log.error("Collection with slug {} does not exist", collectionSlug);
      throw new RuntimeException("Collection slug does not exist");
    }
    List<Book> booksWithCollection = bookRepo.findAllByCollection(res.get());
    return booksWithCollection.stream().map(this::toDtoWrapper).toList();
  }

  public LandingBookCollectionResponse getBooksInCollectionForLandingPage(String slug) {
    // just get book in collection but a nice wrapper for view
    List<BookDTO> list = getAllBooksInCollection(slug);
    var col = collectionRepo.findByCollectionSlug(slug);
    if (col.isEmpty()) {
      throw new CollectionDoesNotExistException(slug);
    }
    // return list or first 10 items of all book
    return LandingBookCollectionResponse.builder()
        .collectionName(col.get().getCollectionName())
        .collectionSlug(col.get().getCollectionSlug())
        .list(
            !list.isEmpty()
                ? list
                : bookRepo.findBy(PageRequest.of(0, 15)).stream().map(this::toDtoWrapper).toList())
        .build();
  }

  private boolean isIsbnNotUnique(String isbn) {
    if (isbn != null) {
      return bookRepo.findByIsbn(isbn).isPresent();
    }
    return false;
  }

  private BookDTO toDtoWrapper(Book book) {
    return BookMapper.INSTANCE.toDto(book);
  }

  private Book toBookWrapper(BookDTO bookDTO) {
    Book b = BookMapper.INSTANCE.toBook(bookDTO);
    b.setUrlSlug(SlugUtils.convertStringToSlug(bookDTO.title()));
    b.setCategory(
        bookDTO.categoryId() == null
            ? null
            : categoryRepo
                .findById(bookDTO.categoryId())
                .orElseThrow(CategoryDoesNotExistException::new));
    b.setCollection(
        bookDTO.collectionId() == null
            ? null
            : collectionRepo
                .findById(bookDTO.collectionId())
                .orElseThrow(
                    () ->
                        new IllegalArgumentException(
                            "Collection ID %d does not exist".formatted(bookDTO.collectionId()))));
    return b;
  }
}
