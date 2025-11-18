package dev.vubl.bookstore.repos;

import dev.vubl.bookstore.entities.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepo extends JpaRepository<Genre, Integer> {}
