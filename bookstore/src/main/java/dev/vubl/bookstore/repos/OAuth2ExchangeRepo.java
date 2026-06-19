package dev.vubl.bookstore.repos;

import dev.vubl.bookstore.entities.OAuth2Exchange;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

@Repository
public interface OAuth2ExchangeRepo extends JpaRepository<OAuth2Exchange, Integer> {
  void deleteByExchangeCode(String exCode);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  Optional<OAuth2Exchange> findByExchangeCode(String exchangeCode);
}
