package dev.vubl.bookstore.entities;

import jakarta.persistence.*;
import java.util.List;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "orders")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order extends BaseEntity {
  @ManyToOne private ApplicationUser customer;

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
  private List<OrderItem> items;
}
