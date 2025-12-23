package dev.vubl.bookstore.repos;

import dev.vubl.bookstore.entities.ApplicationUser;
import dev.vubl.bookstore.entities.UserType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationUserRepo extends JpaRepository<ApplicationUser, Integer> {
  Optional<ApplicationUser> findByEmail(String email);

  Page<ApplicationUser> findByUserTypeIn(List<UserType> types, Pageable pageable);
}
