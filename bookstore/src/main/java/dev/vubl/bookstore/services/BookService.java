package dev.vubl.bookstore.services;

import dev.vubl.bookstore.dtos.BookResponseDTO;
import dev.vubl.bookstore.entities.Book;
import dev.vubl.bookstore.exceptions.BookDoesNotExistException;
import dev.vubl.bookstore.exceptions.BookWithIsbnAlreadyExists;
import dev.vubl.bookstore.repos.BookRepo;
import jakarta.transaction.Transactional;
import java.text.Normalizer;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BookService {
  private final BookRepo bookRepo;

  public List<BookResponseDTO> getAllBooks() {
    return bookRepo.findAll().stream().map(this::mapToBookResponseDTO).toList();
  }

  public Page<Book> getAllBooksPaginated(int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    return bookRepo.findAll(pageable);
  }

  public BookResponseDTO getBookById(Integer id) {
    Book b = bookRepo.findById(id).orElseThrow(BookDoesNotExistException::new);
    return mapToBookResponseDTO(b);
  }

  public BookResponseDTO addNewBook(BookResponseDTO bookResponseDTO) {
    String isbn = bookResponseDTO.isbn();
    if (isIsbnNotUnique(isbn)) {
      throw new BookWithIsbnAlreadyExists("Book with isbn :: %s already exists!".formatted(isbn));
    }

    try {
      log.info("[{}] Adding new book", this.getClass().getName());
      return mapToBookResponseDTO(bookRepo.save(mapToBookEntity(bookResponseDTO)));
    } catch (DataIntegrityViolationException e) {
      throw new DataIntegrityViolationException("Error adding or updating new book!", e);
    }
  }

  public BookResponseDTO updateBookById(BookResponseDTO bookResponseDTO, Integer id) {
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
      b.setAuthor(bookResponseDTO.author());
      b.setImageUrl(bookResponseDTO.imageUrl());
      b.setInStock(bookResponseDTO.inStock());
      b.setUrlSlug(convertTitleToSlug(bookResponseDTO.title()));
      b.setPrice(bookResponseDTO.price());

      return mapToBookResponseDTO(bookRepo.save(b));

    } catch (DataIntegrityViolationException e) {
      throw new DataIntegrityViolationException("Error adding or updating new book!", e);
    }
  }

  public void deleteBookById(Integer id) {
    bookRepo.deleteById(id);
    log.info("[{}] Book with id {} deleted", this.getClass().getName(), id);
  }

  private boolean isIsbnNotUnique(String isbn) {
    if (isbn != null) {
      return bookRepo.findByIsbn(isbn).isPresent();
    }
    return false;
  }

  private String convertTitleToSlug(String title) {
    if (title == null) return null;

    // normalize
    String normalized = Normalizer.normalize(title, Normalizer.Form.NFD);

    // remove diacritics
    String slug = normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    slug = slug.toLowerCase();
    // replace all non-alphanumeric characters with hyphens
    slug = slug.replaceAll("[^a-z0-9]+", "-");
    // Trim leading & trailing hyphens
    slug = slug.replaceAll("^-+|-+$", "");

    return slug;
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
        .build();
  }

  private Book mapToBookEntity(BookResponseDTO bookResponseDTO) {
    return Book.builder()
        .title(bookResponseDTO.title())
        .author(bookResponseDTO.author())
        .publishYear(bookResponseDTO.publishYear())
        .imageUrl(bookResponseDTO.imageUrl())
        .pageCount(bookResponseDTO.pageCount())
        .urlSlug(convertTitleToSlug(bookResponseDTO.title()))
        .publisher(bookResponseDTO.publisher())
        .price(bookResponseDTO.price())
        .description(bookResponseDTO.description())
        .isbn(bookResponseDTO.isbn())
        .inStock(bookResponseDTO.inStock())
        .build();
  }
}
