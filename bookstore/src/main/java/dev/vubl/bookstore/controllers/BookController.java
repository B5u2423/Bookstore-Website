package dev.vubl.bookstore.controllers;

import dev.vubl.bookstore.dtos.BookResponseDTO;
import dev.vubl.bookstore.exceptions.BookWithIsbnAlreadyExists;
import dev.vubl.bookstore.services.BookService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class BookController {
  private final BookService bookService;

  @GetMapping
  public ResponseEntity<List<BookResponseDTO>> getAllBooks() {
    return null;
  }

  @GetMapping("/featured")
  public List<BookResponseDTO> getFeaturedBooks() {
    return bookService.getAllBooks();
  }

  @GetMapping("/best-sellers")
  public List<BookResponseDTO> getBestSellerBooks() {
    return bookService.getAllBooks();
  }

  @GetMapping("/new")
  public List<BookResponseDTO> getNewArrivalBooks() {
    return bookService.getAllBooks();
  }

  @GetMapping("/{id}")
  public ResponseEntity<BookResponseDTO> getBookById(@PathVariable Integer id) {
    return ResponseEntity.ok().body(bookService.getBookById(id));
  }

  // ADMIN
  @PostMapping("/add")
  public ResponseEntity<BookResponseDTO> addNewBook(@RequestBody BookResponseDTO payload) {
    return ResponseEntity.status(HttpStatus.OK).body(bookService.addOrUpdateBook(payload));
  }

  @ExceptionHandler({BookWithIsbnAlreadyExists.class})
  public ResponseEntity<String> handleBookWithExistingIsbn() {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("Book with this ISBN already exists.");
  }
}
