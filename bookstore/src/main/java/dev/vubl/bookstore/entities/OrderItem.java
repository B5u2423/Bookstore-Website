package dev.vubl.bookstore.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "order_items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem extends BaseEntity {
  @Column(name = "quantity")
  private int quantity;

  @Column(name = "price")
  private BigDecimal price; // price at the time of ordering

  @ManyToOne private Order order;

  @ManyToOne private Book book;
}
