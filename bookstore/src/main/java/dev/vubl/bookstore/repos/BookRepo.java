package dev.vubl.bookstore.repos;

import dev.vubl.bookstore.entities.Book;
import dev.vubl.bookstore.entities.Category;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepo extends JpaRepository<Book, Integer> {
  Optional<Book> findByIsbn(String isbn);

  Page<Book> findAllByCategoryIn(List<Category> categories, Pageable pageable);
}
