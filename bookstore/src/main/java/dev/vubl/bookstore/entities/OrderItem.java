package dev.vubl.bookstore.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
  private Integer quantity;

  @Column(name = "price")
  private BigDecimal price; // price at the time of ordering

  @ManyToOne @JsonBackReference private Order order;

  @ManyToOne private Book book;
}
