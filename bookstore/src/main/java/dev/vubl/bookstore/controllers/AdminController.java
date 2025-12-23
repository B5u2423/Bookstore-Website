package dev.vubl.bookstore.controllers;

import dev.vubl.bookstore.entities.ApplicationUser;
import dev.vubl.bookstore.services.ApplicationUserService;
import dev.vubl.bookstore.services.BookService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/admin")
@RestController
@RequiredArgsConstructor
public class AdminController {
  private final ApplicationUserService userService;
  private final BookService bookService;

  @GetMapping
  public ResponseEntity<List<ApplicationUser>> getAllUsers() {
    return ResponseEntity.status(HttpStatus.OK).body(userService.readAllUsers());
  }

  @GetMapping("/get-customers")
  public PagedModel<ApplicationUser> getAllCustomers(
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "5") int size,
      @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
      @RequestParam(value = "order", defaultValue = "asc") String order) {
    return new PagedModel<>(
        userService.getAllUsersPaginated(page, size, sortBy, order, "customers"));
  }

  @GetMapping("/get-staff")
  public PagedModel<ApplicationUser> getAllStaff(
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "5") int size,
      @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
      @RequestParam(value = "order", defaultValue = "asc") String order) {
    return new PagedModel<>(userService.getAllUsersPaginated(page, size, sortBy, order, "staff"));
  }
}
