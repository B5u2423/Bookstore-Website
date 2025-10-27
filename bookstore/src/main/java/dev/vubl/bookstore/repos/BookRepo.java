package dev.vubl.bookstore.repos;

import dev.vubl.bookstore.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepo extends JpaRepository<Book, Integer> {
}
