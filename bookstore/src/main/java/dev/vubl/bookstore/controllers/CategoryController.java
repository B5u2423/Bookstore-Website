package dev.vubl.bookstore.controllers;

import dev.vubl.bookstore.dtos.BookResponseDTO;
import dev.vubl.bookstore.dtos.CategoryCreationRequest;
import dev.vubl.bookstore.dtos.CategoryDTO;
import dev.vubl.bookstore.dtos.CategoryUpdateRequest;
import dev.vubl.bookstore.exceptions.CategoryWithSlugAlreadyExists;
import dev.vubl.bookstore.services.BookService;
import dev.vubl.bookstore.services.CategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
  private final CategoryService categoryService;
  private final BookService bookService;

  @GetMapping("/all")
  public ResponseEntity<List<CategoryDTO>> getAllCategories() {
    return ResponseEntity.ok().body(categoryService.getAllCategories());
  }

  @GetMapping("/children")
  public ResponseEntity<List<CategoryDTO>> getChildrenCategories() {
    return ResponseEntity.ok().body(categoryService.getChildrenCategories());
  }

  @GetMapping("/parents")
  public ResponseEntity<List<CategoryDTO>> getParentCategories() {
    return ResponseEntity.ok().body(categoryService.getParentCategories());
  }

  @GetMapping
  public PagedModel<CategoryDTO> getAllCategoriesPaginated(
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "10") int size) {
    return new PagedModel<>(categoryService.getAllCategoriesPaginated(page, size));
  }

  @PutMapping("/update")
  public ResponseEntity<String> updateCategory(@RequestBody CategoryUpdateRequest payload) {
    categoryService.updateCategory(payload);
    return ResponseEntity.ok().body("Ok");
  }

  @PostMapping("/add")
  public ResponseEntity<String> addCategory(@RequestBody CategoryCreationRequest payload) {
    categoryService.addNewCategory(payload);
    return ResponseEntity.ok().body("Ok");
  }

  @GetMapping("/candidates")
  public ResponseEntity<List<CategoryDTO>> getAllCandidates(
      @RequestParam(value = "id") Integer id) {
    return ResponseEntity.ok().body(categoryService.getPossibleChildCategories(id));
  }

  @DeleteMapping("/delete")
  public ResponseEntity<String> deleteCategoryById(@RequestParam(value = "id") Integer id) {
    categoryService.deleteCategoryById(id);
    return ResponseEntity.ok().body("Category deleted");
  }

  @GetMapping("/{category}")
  public PagedModel<BookResponseDTO> getAllBooksWithCategory(
      @PathVariable(name = "category") String category,
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "24") int size,
      @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
      @RequestParam(value = "order", defaultValue = "asc") String order) {
    return new PagedModel<>(bookService.getBookByCategory(category, page, size, sortBy, order));
  }

  @GetMapping("/name")
  public String getCategoryName(@RequestParam(value = "slug") String slug) {
    return categoryService.getCategoryName(slug);
  }

  @ExceptionHandler({CategoryWithSlugAlreadyExists.class})
  public ResponseEntity<String> handleExistingCategory(CategoryWithSlugAlreadyExists ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
  }
}
