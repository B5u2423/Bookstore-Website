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
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem extends BaseEntity {
  @Column(name = "quantity")
  private Integer quantity;

  @Column(name = "price_at_purchase")
  private BigDecimal priceAtPurchase; // price at the time of ordering

  @Column(name = "title_at_purchase")
  private String titleAtPurchase;

  @Column(name = "isbn")
  private String isbn;

  @Column(name = "product_code")
  private String productCode;

  @ManyToOne @JsonBackReference private Order order;

  @Column(name = "book_id")
  private Integer bookId;
}
