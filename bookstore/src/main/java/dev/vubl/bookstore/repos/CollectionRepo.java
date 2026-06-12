package dev.vubl.bookstore.repos;

import dev.vubl.bookstore.entities.Collection;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollectionRepo extends JpaRepository<Collection, Integer> {
  Optional<Collection> findByCollectionSlug(String slug);
}
