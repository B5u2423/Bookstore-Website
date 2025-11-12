package dev.vubl.bookstore.services;

import dev.vubl.bookstore.dtos.BookDTO;
import dev.vubl.bookstore.entities.Author;
import dev.vubl.bookstore.entities.Book;
import dev.vubl.bookstore.exceptions.BookWithIsbnAlreadyExists;
import dev.vubl.bookstore.repos.AuthorRepo;
import dev.vubl.bookstore.repos.BookRepo;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
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
      return mapToBookDTO(
          bookRepo.save(
              Book.builder()
                  .title(bookDTO.title())
                  .authors(List.of())
                  .price(bookDTO.price())
                  .description(bookDTO.description())
                  .isbn(isbn)
                  .inStock(bookDTO.inStock())
                  .build()));
    } catch (DataIntegrityViolationException e) {
      throw new DataIntegrityViolationException("Error adding or updating new book!", e);
    }
  }

  public void loadBooksForTesting() {
    for (int i = 0; i < 20; i++) {
      String authorName = "Author" + i % 3;
      Author author =
          authorRepo
              .findByAuthorName(authorName)
              .orElseGet(() -> authorRepo.save(Author.builder().authorName(authorName).build()));

      bookRepo.save(
          Book.builder()
              .title("Title %d".formatted(i))
              .isbn(String.valueOf(Math.random()))
              .description("Description %s".formatted(i))
              .authors(List.of(author))
              .price(BigDecimal.valueOf(Math.random()))
              .build());
    }
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
}
