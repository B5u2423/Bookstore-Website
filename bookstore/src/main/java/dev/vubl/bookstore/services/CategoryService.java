package dev.vubl.bookstore.services;

import dev.vubl.bookstore.dtos.CategoryDTO;
import dev.vubl.bookstore.entities.Category;
import dev.vubl.bookstore.repos.CategoryRepo;
import jakarta.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {
  private final CategoryRepo categoryRepo;

  public List<CategoryDTO> getAllCategories() {
    return categoryRepo.findAll().stream().map(this::toDto).toList();
  }

  public Page<CategoryDTO> getAllCategoriesPaginated(int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    Page<Category> categoryPage = categoryRepo.findAll(pageable);
    return categoryPage.map(this::toDto);
  }

  private CategoryDTO toDto(Category c) {
    return CategoryDTO.builder()
        .id(c.getId())
        .parent(c.getParentCategory() != null ? c.getParentCategory().getId() : null)
        .categorySlug(c.getCategorySlug())
        .categoryName(c.getCategoryName())
        .children(
            c.getChildrenCategories() == null
                ? Collections.emptyList()
                : c.getChildrenCategories().stream().map(this::toDto).toList())
        .build();
  }

  private CategoryDTO toDto(Category c) {
    return CategoryDTO.builder()
        .id(c.getId())
        .parent(c.getParentCategory() != null ? c.getParentCategory().getId() : null)
        .categorySlug(c.getCategorySlug())
        .categoryName(c.getCategoryName())
        .children(
            c.getChildrenCategories() == null
                ? Collections.emptyList()
                : c.getChildrenCategories().stream().map(this::toDto).toList())
        .build();
  }
}
