package dev.vubl.bookstore.books;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
public class BookController {
  @GetMapping("/featured")
  public List<BookDTO> getFeaturedBooks() {
    return new ArrayList<>(
        List.of(
            new BookDTO(123L, "123", "Title", "description", BigDecimal.valueOf(123), 3, "abc"),
            new BookDTO(123L, "123", "Title", "description", BigDecimal.valueOf(123), 3, "abc"),
            new BookDTO(123L, "123", "Title", "description", BigDecimal.valueOf(123), 3, "abc"),
            new BookDTO(123L, "123", "Title", "description", BigDecimal.valueOf(123), 3, "abc"),
            new BookDTO(123L, "123", "Title", "description", BigDecimal.valueOf(123), 3, "abc"),
            new BookDTO(123L, "123", "Title", "description", BigDecimal.valueOf(123), 3, "abc")));
  }

  @GetMapping("/best-sellers")
  public List<BookDTO> getBestSellerBooks() {
    return new ArrayList<>(
        List.of(
            new BookDTO(123L, "123", "Title", "description", BigDecimal.valueOf(123), 3, "abc"),
            new BookDTO(123L, "123", "Title", "description", BigDecimal.valueOf(123), 3, "abc"),
            new BookDTO(123L, "123", "Title", "description", BigDecimal.valueOf(123), 3, "abc"),
            new BookDTO(123L, "123", "Title", "description", BigDecimal.valueOf(123), 3, "abc"),
            new BookDTO(123L, "123", "Title", "description", BigDecimal.valueOf(123), 3, "abc"),
            new BookDTO(123L, "123", "Title", "description", BigDecimal.valueOf(123), 3, "abc")));
  }

  @GetMapping("/new")
  public List<BookDTO> getNewArrivalBooks() {
    return new ArrayList<>(
        List.of(
            new BookDTO(123L, "123", "Title", "description", BigDecimal.valueOf(123), 3, "abc"),
            new BookDTO(123L, "123", "Title", "description", BigDecimal.valueOf(123), 3, "abc"),
            new BookDTO(123L, "123", "Title", "description", BigDecimal.valueOf(123), 3, "abc"),
            new BookDTO(123L, "123", "Title", "description", BigDecimal.valueOf(123), 3, "abc"),
            new BookDTO(123L, "123", "Title", "description", BigDecimal.valueOf(123), 3, "abc"),
            new BookDTO(123L, "123", "Title", "description", BigDecimal.valueOf(123), 3, "abc")));
  }
}
