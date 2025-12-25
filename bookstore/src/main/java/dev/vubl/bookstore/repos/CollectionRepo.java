package dev.vubl.bookstore.repos;

import dev.vubl.bookstore.entities.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollectionRepo extends JpaRepository<Collection, Integer> {}
