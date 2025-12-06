package dev.vubl.bookstore.services;

import dev.vubl.bookstore.dtos.CategoryCreationRequest;
import dev.vubl.bookstore.dtos.CategoryDTO;
import dev.vubl.bookstore.dtos.CategoryUpdateRequest;
import dev.vubl.bookstore.entities.Category;
import dev.vubl.bookstore.exceptions.CategoryDoesNotExistException;
import dev.vubl.bookstore.repos.CategoryRepo;
import dev.vubl.bookstore.utils.SlugUtils;
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
    Page<Category> categoryPage = categoryRepo.findAllByOrderByIdAsc(pageable);
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

    return categoryRepo.findAll().stream()
        .filter(item -> !illegalCategoryIds.contains(item.getId()))
        .filter(
            // filter grandparent category
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

  public void updateCategory(CategoryUpdateRequest payload) {
    Category c = getCategoryByIdOrThrowException(payload.id());
    c.setCategoryName(payload.categoryName());
    c.setCategorySlug(SlugUtils.convertStringToSlug(payload.categoryName()));
    c.removeAllChildren();
    for (Integer childId : payload.children()) {
      c.addChild(getCategoryByIdOrThrowException(childId));
    }
    categoryRepo.save(c);
  }

  private Category getCategoryByIdOrThrowException(Integer id) {
    return categoryRepo
        .findById(id)
        .orElseThrow(
            () ->
                new CategoryDoesNotExistException(
                    "Category with id %d does not exist".formatted(id)));
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

  public void addNewCategory(CategoryCreationRequest payload) {
    Category c =
        Category.builder()
            .categoryName(payload.categoryName())
            .categorySlug(SlugUtils.convertStringToSlug(payload.categoryName()))
            .build();
    for (Integer childID : payload.children()) {
      c.addChild(getCategoryByIdOrThrowException(childID));
    }
    categoryRepo.save(c);
  }
}
