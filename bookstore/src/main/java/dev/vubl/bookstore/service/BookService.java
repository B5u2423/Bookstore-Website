package dev.vubl.bookstore.service;

import dev.vubl.bookstore.model.BookDTO;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BookService {

  public List<BookDTO> books = new ArrayList<>();

  public BookService() {
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

  public List<BookDTO> list() {
    return books;
  }
}
