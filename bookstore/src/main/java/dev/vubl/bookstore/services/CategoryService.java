package dev.vubl.bookstore.services;

import dev.vubl.bookstore.dtos.CategoryDTO;
import dev.vubl.bookstore.entities.Category;
import dev.vubl.bookstore.repos.CategoryRepo;
import jakarta.transaction.Transactional;
import java.util.*;
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

  // TODO: Might remove this later
  public void addChildCategory(Integer parentId, Integer childId) {
    if (parentId == null || childId == null)
      throw new IllegalArgumentException("Parent/child category Id must not be null!");
    Category parent = categoryRepo.findById(parentId).orElseThrow();
    Category child = categoryRepo.findById(childId).orElseThrow();

    parent.addChild(child);
  }

  public List<CategoryDTO> getPossibleChildCategories(Integer id) {
    if (id == null) throw new IllegalArgumentException("Category Id must not be null");
    Category c = categoryRepo.findById(id).orElseThrow();
    Set<Integer> illegalCategoryIds = new HashSet<>();
    illegalCategoryIds.add(c.getId());
    // parent Id
    if (c.getParentCategory() != null) {
      illegalCategoryIds.add(c.getParentCategory().getId());
    }
    addAllDescendantIds(c, illegalCategoryIds);

    return categoryRepo.findAll().stream()
        .filter(item -> !illegalCategoryIds.contains(item.getId()))
        .filter(
            item -> {
              if (item.getChildrenCategories() != null) {
                return item.getChildrenCategories().stream()
                    .noneMatch(child -> illegalCategoryIds.contains(child.getId()));
              }
              return true;
            })
        .map(this::toDto)
        .toList();
  }

  private void addAllDescendantIds(Category category, Set<Integer> ids) {
    if (category.getChildrenCategories() != null) {
      for (Category child : category.getChildrenCategories()) {
        ids.add(child.getId());
        addAllDescendantIds(child, ids); // recursive call for each child
      }
    }
  }

  private CategoryDTO toDto(Category c) {
    return CategoryDTO.builder()
        .id(c.getId())
        .parent(c.getParentCategory() != null ? c.getParentCategory().getId() : null)
        .parentName(c.getParentCategory() != null ? c.getParentCategory().getCategoryName() : null)
        .categorySlug(c.getCategorySlug())
        .categoryName(c.getCategoryName())
        .children(
            c.getChildrenCategories() == null
                ? Collections.emptyList()
                : c.getChildrenCategories().stream().map(this::toDto).toList())
        .build();
  }
}
