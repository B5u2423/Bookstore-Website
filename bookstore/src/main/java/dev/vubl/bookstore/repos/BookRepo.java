package dev.vubl.bookstore.repos;

import dev.vubl.bookstore.entities.Book;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepo extends JpaRepository<Book, Integer> {
  Optional<Book> findByIsbn(String isbn);
}
