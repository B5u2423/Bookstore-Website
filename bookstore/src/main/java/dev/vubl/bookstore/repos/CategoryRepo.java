package dev.vubl.bookstore.repos;

import dev.vubl.bookstore.entities.Category;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Integer> {
  Page<Category> findAllByOrderByIdAsc(Pageable pageable);

  Optional<Category> findByCategorySlug(String slug);
}
