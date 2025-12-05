package dev.vubl.bookstore.services;

import dev.vubl.bookstore.entities.Category;
import dev.vubl.bookstore.repos.CategoryRepo;
import jakarta.transaction.Transactional;
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

  public List<Category> getAllCategories() {
    return categoryRepo.findAll();
  }

  public Page<Category> getAllCategoriesPaginated(int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    return categoryRepo.findAll(pageable);
  }
}
