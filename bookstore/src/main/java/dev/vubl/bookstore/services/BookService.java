package dev.vubl.bookstore.services;

import dev.vubl.bookstore.dtos.BookResponseDTO;
import dev.vubl.bookstore.entities.Book;
import dev.vubl.bookstore.entities.Category;
import dev.vubl.bookstore.entities.Collection;
import dev.vubl.bookstore.exceptions.BookDoesNotExistException;
import dev.vubl.bookstore.exceptions.BookWithIsbnAlreadyExists;
import dev.vubl.bookstore.exceptions.CategoryDoesNotExistException;
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

  public List<BookResponseDTO> getAllBooks() {
    return bookRepo.findAll().stream().map(this::mapToBookResponseDTO).toList();
  }

  public Page<BookResponseDTO> getAllBooksPaginated(
      int page, int size, String sortBy, String order) {
    List<String> allowed = List.of("id");
    if (!allowed.contains(sortBy)) {
      throw new IllegalArgumentException("Invalid sort field: %s".formatted(sortBy));
    }

    Sort sort = order.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
    Pageable pageable = PageRequest.of(page, size, sort);
    Page<Book> books = bookRepo.findAll(pageable);
    return books.map(this::mapToBookResponseDTO);
  }

  public BookResponseDTO getBookById(Integer id) {
    Book b = bookRepo.findById(id).orElseThrow(BookDoesNotExistException::new);
    return mapToBookResponseDTO(b);
  }

  public BookResponseDTO addNewBook(BookResponseDTO bookResponseDTO, MultipartFile image)
      throws IOException {
    String isbn = bookResponseDTO.isbn();
    if (isIsbnNotUnique(isbn)) {
      throw new BookWithIsbnAlreadyExists("Book with isbn :: %s already exists!".formatted(isbn));
    }

    Book b = mapToBookEntity(bookResponseDTO);
    try {
      if (image != null) {
        log.info("[{}] Uploading image...", this.getClass().getName());
        String returnedUrl = cloudinaryService.uploadImage(image);
        b.setImageUrl(returnedUrl);
      }
      log.info("[{}] Adding new book", this.getClass().getName());
      return mapToBookResponseDTO(bookRepo.save(b));
    } catch (DataIntegrityViolationException e) {
      throw new DataIntegrityViolationException("Error adding or updating new book!", e);
    } catch (IOException e) {
      log.info("[{}] Error uploading image", this.getClass().getName());
      throw new IOException("Error uploading book image to cloud");
    }
  }

  public BookResponseDTO updateBookById(
      BookResponseDTO bookResponseDTO, MultipartFile image, Integer id) {
    try {
      Book b =
          bookRepo
              .findById(id)
              .orElseThrow(
                  () ->
                      new BookDoesNotExistException(
                          "Book with id %d does not exist".formatted(id)));

      // update
      b.setTitle(bookResponseDTO.title());
      b.setAuthor(bookResponseDTO.author());
      b.setPublisher(bookResponseDTO.publisher());
      b.setPublishYear(bookResponseDTO.publishYear());
      b.setPageCount(bookResponseDTO.pageCount());
      b.setIsbn(bookResponseDTO.isbn());
      b.setDescription(bookResponseDTO.description());
      b.setAuthor(bookResponseDTO.author());
      if (image != null) {
        String returnedUrl = cloudinaryService.uploadImage(image);
        b.setImageUrl(returnedUrl);
      } else if (!b.getImageUrl().equals(bookResponseDTO.imageUrl())) {
        b.setImageUrl(bookResponseDTO.imageUrl());
      }
      b.setInStock(bookResponseDTO.inStock());
      b.setUrlSlug(SlugUtils.convertStringToSlug(bookResponseDTO.title()));
      b.setPrice(bookResponseDTO.price());
      b.setUpdateTimeStamp(Instant.now());
      if (bookResponseDTO.categoryId() != null) {

        b.setCategory(
            categoryRepo
                .findById(bookResponseDTO.categoryId())
                .orElseThrow(CategoryDoesNotExistException::new));
      }
      if (bookResponseDTO.collectionId() != null) {

        b.setCollection(
            collectionRepo
                .findById(bookResponseDTO.collectionId())
                .orElseThrow(() -> new IllegalArgumentException("Collection ID does not exist!")));
      }

      Book savedBook = bookRepo.save(b);
      return mapToBookResponseDTO(savedBook);

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

  public Page<BookResponseDTO> getBookByCategory(
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
    return bookPage.map(this::mapToBookResponseDTO);
  }

  @Deprecated
  public List<BookResponseDTO> searchBookV2(String keyword) {
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
    return query.getResultList().stream().map(this::mapToBookResponseDTO).toList();
  }

  public List<BookResponseDTO> searchBookV3(String keyword) {
    return bookRepo.searchBookV3(keyword).stream().map(this::mapToBookResponseDTO).toList();
  }

  public List<BookResponseDTO> getAllBooksInCollection(String collectionSlug) {
    Optional<Collection> res = collectionRepo.findByCollectionSlug(collectionSlug);
    if (res.isEmpty()) {
      log.error("Collection with slug {} does not exist", collectionSlug);
      throw new RuntimeException("Collection slug does not exist");
    }
    List<Book> booksWithCollection = bookRepo.findAllByCollection(res.get());
    return booksWithCollection.stream().map(this::mapToBookResponseDTO).toList();
  }

  private boolean isIsbnNotUnique(String isbn) {
    if (isbn != null) {
      return bookRepo.findByIsbn(isbn).isPresent();
    }
    return false;
  }

  private BookResponseDTO mapToBookResponseDTO(Book book) {
    return BookResponseDTO.builder()
        .id(book.getId())
        .isbn(book.getIsbn())
        .title(book.getTitle())
        .description(book.getDescription())
        .price(book.getPrice())
        .inStock(book.getInStock())
        .publisher(book.getPublisher())
        .publishYear(book.getPublishYear())
        .pageCount(book.getPageCount())
        .imageUrl(book.getImageUrl())
        .urlSlug(book.getUrlSlug())
        .author(book.getAuthor())
        .categoryId(book.getCategory() == null ? null : book.getCategory().getId())
        .categoryName(book.getCategory() == null ? null : book.getCategory().getCategoryName())
        .collectionId(book.getCollection() == null ? null : book.getCollection().getId())
        .collectionName(
            book.getCollection() == null ? null : book.getCollection().getCollectionName())
        .build();
  }

  private Book mapToBookEntity(BookResponseDTO bookResponseDTO) {
    return Book.builder()
        .title(bookResponseDTO.title())
        .author(bookResponseDTO.author())
        .publishYear(bookResponseDTO.publishYear())
        .imageUrl(bookResponseDTO.imageUrl())
        .pageCount(bookResponseDTO.pageCount())
        .urlSlug(SlugUtils.convertStringToSlug(bookResponseDTO.title()))
        .publisher(bookResponseDTO.publisher())
        .price(bookResponseDTO.price())
        .description(bookResponseDTO.description())
        .isbn(bookResponseDTO.isbn())
        .inStock(bookResponseDTO.inStock())
        .category(
            bookResponseDTO.categoryId() == null
                ? null
                : categoryRepo
                    .findById(bookResponseDTO.categoryId())
                    .orElseThrow(CategoryDoesNotExistException::new))
        .collection(
            bookResponseDTO.collectionId() == null
                ? null
                : collectionRepo
                    .findById(bookResponseDTO.collectionId())
                    .orElseThrow(
                        () ->
                            new IllegalArgumentException(
                                "Collection ID %d does not exist"
                                    .formatted(bookResponseDTO.collectionId()))))
        .build();
  }
}
