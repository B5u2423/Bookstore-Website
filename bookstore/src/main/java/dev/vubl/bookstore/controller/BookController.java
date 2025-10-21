package dev.vubl.bookstore.controller;

import dev.vubl.bookstore.model.BookDTO;
import dev.vubl.bookstore.service.BookService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
@CrossOrigin
public class BookController {
  private final BookService bookService;

  @Autowired
  public BookController(BookService bookService) {
    this.bookService = bookService;
  }

  @GetMapping("/featured")
  public List<BookDTO> getFeaturedBooks() {
    return bookService.list();
  }

  @GetMapping("/best-sellers")
  public List<BookDTO> getBestSellerBooks() {
    return bookService.list();
  }

  @GetMapping("/new")
  public List<BookDTO> getNewArrivalBooks() {
    return bookService.list();
  }
}
