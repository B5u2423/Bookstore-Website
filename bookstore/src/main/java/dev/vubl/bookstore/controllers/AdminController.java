package dev.vubl.bookstore.controllers;

import dev.vubl.bookstore.dtos.DashboardAnalyticsResponse;
import dev.vubl.bookstore.entities.ApplicationUser;
import dev.vubl.bookstore.entities.DateRange;
import dev.vubl.bookstore.services.ApplicationUserService;
import dev.vubl.bookstore.services.BookService;
import dev.vubl.bookstore.services.DashboardAnalyticsService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/admin")
@RestController
@RequiredArgsConstructor
public class AdminController {
  private final ApplicationUserService userService;
  private final BookService bookService;
  private final DashboardAnalyticsService dashboardAnalyticsService;

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

  @GetMapping("/analytics")
  public DashboardAnalyticsResponse getDashboardAnalytics(
      @RequestParam(value = "startDate", required = false)
          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          LocalDate startDate,
      @RequestParam(value = "endDate", required = false)
          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          LocalDate endDate,
      @RequestParam(value = "range", defaultValue = "CUSTOM") DateRange range) {
    return dashboardAnalyticsService.getAnalytics(range, startDate, endDate);
  }
}
