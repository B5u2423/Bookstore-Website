package dev.vubl.bookstore.controllers;

import dev.vubl.bookstore.entities.Category;
import dev.vubl.bookstore.services.CategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
  private final CategoryService categoryService;

  @GetMapping("/all")
  public ResponseEntity<List<Category>> getAllCategories() {
    return ResponseEntity.ok().body(categoryService.getAllCategories());
  }

  @GetMapping
  public PagedModel<Category> getAllCategoriesPaginated(
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "10") int size) {
    return new PagedModel<>(categoryService.getAllCategoriesPaginated(page, size));
  }
}
