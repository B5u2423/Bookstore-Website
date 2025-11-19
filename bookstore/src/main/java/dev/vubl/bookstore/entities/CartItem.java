package dev.vubl.bookstore.entities;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "cart_items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItem extends BaseEntity {
  private int quantity;

  @ManyToOne private Cart cart;

  @ManyToOne private Book book;
}
