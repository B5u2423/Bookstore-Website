package dev.vubl.bookstore.entities;

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
  @OneToOne private ApplicationUser customer;

  @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<CartItem> items;
}
