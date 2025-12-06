package dev.vubl.bookstore.controllers;

import dev.vubl.bookstore.dtos.CategoryDTO;
import dev.vubl.bookstore.services.CategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
  private final CategoryService categoryService;

  @GetMapping("/all")
  public ResponseEntity<List<CategoryDTO>> getAllCategories() {
    return ResponseEntity.ok().body(categoryService.getAllCategories());
  }

  @GetMapping
  public PagedModel<CategoryDTO> getAllCategoriesPaginated(
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "10") int size) {
    return new PagedModel<>(categoryService.getAllCategoriesPaginated(page, size));
  }

  // TODO: Might remove this later
  @PutMapping("/add-child")
  public ResponseEntity<String> addChildCategory(
      @RequestParam(value = "parent") Integer parentId,
      @RequestParam(value = "child") Integer childId) {
    categoryService.addChildCategory(parentId, childId);
    return ResponseEntity.ok().body("Ok");
  }

  @GetMapping("/candidates")
  public ResponseEntity<List<CategoryDTO>> getAllCandidates(
      @RequestParam(value = "id") Integer id) {
    return ResponseEntity.ok().body(categoryService.getPossibleChildCategories(id));
  }
}
