package dev.vubl.bookstore.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.List;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "carts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cart extends BaseEntity {
  @OneToOne private ApplicationUser user;

  @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonManagedReference
  private List<CartItem> items;

  @Enumerated(EnumType.STRING)
  @Column(name = "cart_status")
  private CartStatus cartStatus;
}
