package dev.vubl.bookstore.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
  private Integer quantity;

  @ManyToOne @JsonBackReference private Cart cart;

  @ManyToOne private Book book;
}
