package dev.vubl.bookstore.controller;

import dev.vubl.bookstore.dto.BookDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {
  @GetMapping("/featured")
  public List<BookDTO> getFeaturedBooks() {
    List<BookDTO> books = new ArrayList<>();
    books.add(
      new BookDTO(123L, "123", "Title", "description", BigDecimal.valueOf(123), 3)
    );
    return books;
  }

  @GetMapping("/bestsellers")
  public List<BookDTO> getBestSellerBooks() {
    return null;
  }

  @GetMapping("/new")
  public List<BookDTO> getNewArrivalBooks() {
    return null;
  }
}
