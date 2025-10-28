package dev.vubl.bookstore.services;

import dev.vubl.bookstore.dto.BookDTO;
import dev.vubl.bookstore.repos.BookRepo;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BookService {
  private final BookRepo bookRepo;
  private List<BookDTO> books = new ArrayList<>();

  public BookService(BookRepo bookRepo) {
    this.bookRepo = bookRepo;
    books.add(new BookDTO(123L, "123", "Title", "description", BigDecimal.valueOf(123), 3, "abc"));
    books.add(new BookDTO(123L, "123", "Title", "description", BigDecimal.valueOf(123), 3, "abc"));
    books.add(new BookDTO(123L, "123", "Title", "description", BigDecimal.valueOf(123), 3, "abc"));
    books.add(new BookDTO(123L, "123", "Title", "description", BigDecimal.valueOf(123), 3, "abc"));
    books.add(new BookDTO(123L, "123", "Title", "description", BigDecimal.valueOf(123), 3, "abc"));
    books.add(new BookDTO(123L, "123", "Title", "description", BigDecimal.valueOf(123), 3, "abc"));
    books.add(new BookDTO(123L, "123", "Title", "description", BigDecimal.valueOf(123), 3, "abc"));
    books.add(new BookDTO(123L, "123", "Title", "description", BigDecimal.valueOf(123), 3, "abc"));
    books.add(new BookDTO(123L, "123", "Title", "description", BigDecimal.valueOf(123), 3, "abc"));
    books.add(new BookDTO(123L, "123", "Title", "description", BigDecimal.valueOf(123), 3, "abc"));
    books.add(new BookDTO(123L, "123", "Title", "description", BigDecimal.valueOf(123), 3, "abc"));
    books.add(new BookDTO(123L, "123", "Title", "description", BigDecimal.valueOf(123), 3, "abc"));
  }

  public List<BookDTO> getAllBooks() {
    return books;
  }
}
