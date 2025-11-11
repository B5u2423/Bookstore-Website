package dev.vubl.bookstore.services;

import dev.vubl.bookstore.dtos.BookDTO;
import dev.vubl.bookstore.entities.Author;
import dev.vubl.bookstore.entities.Book;
import dev.vubl.bookstore.repos.AuthorRepo;
import dev.vubl.bookstore.repos.BookRepo;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class BookService {
  private final BookRepo bookRepo;
  private final AuthorRepo authorRepo;

  public List<BookDTO> getAllBooks() {
    return bookRepo.findAll().stream().map(this::mapToBookDTO).toList();
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
