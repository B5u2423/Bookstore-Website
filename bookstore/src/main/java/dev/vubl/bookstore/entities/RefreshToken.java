package dev.vubl.bookstore.entities;

import jakarta.persistence.*;
import java.time.Instant;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "refresh_tokens")
public class RefreshToken extends BaseEntity {
  private String refreshToken;

  @Column(name = "expiration")
  private Instant expiration;

  @OneToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private ApplicationUser user;
}
