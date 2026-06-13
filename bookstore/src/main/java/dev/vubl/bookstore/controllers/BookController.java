package dev.vubl.bookstore.controllers;

import dev.vubl.bookstore.dtos.BookResponseDTO;
import dev.vubl.bookstore.exceptions.BookWithIsbnAlreadyExists;
import dev.vubl.bookstore.exceptions.CategoryDoesNotExistException;
import dev.vubl.bookstore.services.BookService;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class BookController {
  private final BookService bookService;

  @GetMapping
  public PagedModel<BookResponseDTO> getAllBooks(
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "5") int size,
      @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
      @RequestParam(value = "order", defaultValue = "asc") String order) {
    return new PagedModel<>(bookService.getAllBooksPaginated(page, size, sortBy, order));
  }

  @GetMapping("/{id}")
  public ResponseEntity<BookResponseDTO> getBookById(@PathVariable Integer id) {
    return ResponseEntity.ok().body(bookService.getBookById(id));
  }

  @GetMapping("/search")
  public List<BookResponseDTO> searchBook(@RequestParam String keyword) {
    return bookService.searchBookV3(keyword);
  }

  // ADMIN
  @PostMapping(path = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<BookResponseDTO> addNewBook(
      @Valid @RequestPart("book") BookResponseDTO payload,
      @RequestPart(value = "image", required = false) MultipartFile image)
      throws IOException {
    return ResponseEntity.status(HttpStatus.OK).body(bookService.addNewBook(payload, image));
  }

  @PutMapping(path = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<BookResponseDTO> updateBookById(
      @RequestPart("book") BookResponseDTO payload,
      @RequestPart(value = "image", required = false) MultipartFile image,
      @RequestParam(value = "id") Integer id) {
    return ResponseEntity.ok().body(bookService.updateBookById(payload, image, id));
  }

  @DeleteMapping("/delete")
  public ResponseEntity<String> deleteBookById(@RequestParam(value = "id") Integer id) {
    bookService.deleteBookById(id);
    return ResponseEntity.ok().body("Thành công xóa sản phẩm với id %d!".formatted(id));
  }

  @GetMapping("/get-books")
  public ResponseEntity<List<BookResponseDTO>> getAllBookInCollection(
      @RequestParam(value = "collection") String collection) {
    return ResponseEntity.ok().body(bookService.getAllBooksInCollection(collection));
  }

  @GetMapping("/landing")
  public ResponseEntity<List<BookResponseDTO>> getBooksInCollectionForLanding(
      @RequestParam(value = "collection") String collection) {
    // just get book in collection but a nice wrapper for view
    List<BookResponseDTO> list = bookService.getAllBooksInCollection(collection);
    if (!list.isEmpty()) {
      return ResponseEntity.ok().body(list);
    }
    // fetch 12 items
    return ResponseEntity.ok().body(bookService.getAllBooks().subList(0, 12));
  }

  @ExceptionHandler({BookWithIsbnAlreadyExists.class})
  public ResponseEntity<String> handleBookWithExistingIsbn(BookWithIsbnAlreadyExists ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
  }

  @ExceptionHandler({CategoryDoesNotExistException.class})
  public ResponseEntity<String> handleCategoryDoesNotExistException(
      CategoryDoesNotExistException ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
  }

  @ExceptionHandler({MethodArgumentNotValidException.class})
  public ResponseEntity<Map<String, String>> methodArgumentNotValidException(
      MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult()
        .getFieldErrors()
        .forEach(
            error -> {
              errors.put(error.getField(), error.getDefaultMessage());
            });
    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
  }
}
