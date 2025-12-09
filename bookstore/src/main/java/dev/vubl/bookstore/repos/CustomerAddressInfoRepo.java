package dev.vubl.bookstore.repos;

import dev.vubl.bookstore.entities.CustomerAddressInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerAddressInfoRepo extends JpaRepository<CustomerAddressInfo, Integer> {}
