package dev.vubl.bookstore.services;

import dev.vubl.bookstore.dtos.BookDTO;
import dev.vubl.bookstore.entities.Book;
import dev.vubl.bookstore.exceptions.BookWithIsbnAlreadyExists;
import dev.vubl.bookstore.repos.AuthorRepo;
import dev.vubl.bookstore.repos.BookRepo;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BookService {
  private final BookRepo bookRepo;
  private final AuthorRepo authorRepo;

  public List<BookDTO> getAllBooks() {
    return bookRepo.findAll().stream().map(this::mapToBookDTO).toList();
  }

  public BookDTO addOrUpdateBook(BookDTO bookDTO) {
    String isbn = bookDTO.isbn();
    if (bookRepo.findByIsbn(isbn).isPresent()) {
      throw new BookWithIsbnAlreadyExists("Book with isbn :: %s already exists!".formatted(isbn));
    }
    try {
      log.info("[{}] Adding new book", this.getClass().getName());
      return mapToBookDTO(bookRepo.save(mapToBookEntity(bookDTO)));
    } catch (DataIntegrityViolationException e) {
      throw new DataIntegrityViolationException("Error adding or updating new book!", e);
    }
  }

  public void deleteBook(BookDTO bookDTO) {
    bookRepo.delete(mapToBookEntity(bookDTO));
    log.info("[{}] Book deleted", this.getClass().getName());
  }

  private BookDTO mapToBookDTO(Book book) {
    return BookDTO.builder()
        .isbn(book.getIsbn())
        .title(book.getTitle())
        .description(book.getDescription())
        .price(book.getPrice())
        .inStock(book.getInStock())
        .build();
  }

  private Book mapToBookEntity(BookDTO bookDTO) {
    return Book.builder()
        .title(bookDTO.title())
        .authors(bookDTO.authors())
        .price(bookDTO.price())
        .description(bookDTO.description())
        .isbn(bookDTO.isbn())
        .inStock(bookDTO.inStock())
        .build();
  }
}
