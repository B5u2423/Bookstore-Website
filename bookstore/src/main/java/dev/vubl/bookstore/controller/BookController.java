package dev.vubl.bookstore.controller;

import java.util.ArrayList;
import java.util.List;

import dev.vubl.bookstore.model.BookDTO;
import dev.vubl.bookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
public class BookController {
  @Autowired
  BookService bookService;

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
