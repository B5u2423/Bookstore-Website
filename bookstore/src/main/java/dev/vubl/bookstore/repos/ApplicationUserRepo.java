package dev.vubl.bookstore.repos;

import dev.vubl.bookstore.entities.ApplicationUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationUserRepo extends JpaRepository<ApplicationUser, Integer> {
  Optional<ApplicationUser> findByEmail(String email);
}
