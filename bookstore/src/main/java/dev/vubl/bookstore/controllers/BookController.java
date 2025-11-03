package dev.vubl.bookstore.controllers;

import dev.vubl.bookstore.dtos.BookDTO;
import dev.vubl.bookstore.services.BookService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/books")
@CrossOrigin(origins = "http://localhost:5713")
public class BookController {
  private final BookService bookService;

  @Autowired
  public BookController(BookService bookService) {
    this.bookService = bookService;
  }

  @GetMapping("/featured")
  public List<BookDTO> getFeaturedBooks() {
    return bookService.getAllBooks();
  }

  @GetMapping("/best-sellers")
  public List<BookDTO> getBestSellerBooks() {
    return bookService.getAllBooks();
  }

  @GetMapping("/new")
  public List<BookDTO> getNewArrivalBooks() {
    return bookService.getAllBooks();
  }
}
