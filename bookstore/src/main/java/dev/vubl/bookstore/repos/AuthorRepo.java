package dev.vubl.bookstore.repos;

import dev.vubl.bookstore.entities.Author;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepo extends JpaRepository<Author, Integer> {
  Optional<Author> findByAuthorName(String name);
}
