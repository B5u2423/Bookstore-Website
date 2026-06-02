package dev.vubl.bookstore.repos;

import dev.vubl.bookstore.dtos.dashboard.UserMetricsDTO;
import dev.vubl.bookstore.entities.ApplicationUser;
import dev.vubl.bookstore.entities.UserType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationUserRepo extends JpaRepository<ApplicationUser, Integer> {
  Optional<ApplicationUser> findByEmail(String email);

  Page<ApplicationUser> findByUserTypeIn(List<UserType> types, Pageable pageable);

  @Query(
"""
    SELECT new dev.vubl.bookstore.dtos.dashboard.UserMetricsDTO(
        COUNT(u),
        SUM(CASE WHEN u.userType = dev.vubl.bookstore.entities.UserType.CUSTOMER THEN 1 ELSE 0 END),
        SUM(CASE WHEN u.userType = dev.vubl.bookstore.entities.UserType.STAFF THEN 1 ELSE 0 END),
        SUM(CASE WHEN u.userType = dev.vubl.bookstore.entities.UserType.ADMIN THEN 1 ELSE 0 END),
        SUM(CASE WHEN FUNCTION('DATE', u.createTimeStamp) = CURRENT_DATE THEN 1 ELSE 0 END),
        SUM(CASE WHEN EXTRACT(MONTH FROM u.createTimeStamp) = EXTRACT(MONTH FROM CURRENT_DATE)
                  AND EXTRACT(YEAR FROM u.createTimeStamp) = EXTRACT(YEAR FROM CURRENT_DATE)
            THEN 1 ELSE 0 END)
    )
    FROM ApplicationUser u
""")
  UserMetricsDTO getUserMetrics();
}
