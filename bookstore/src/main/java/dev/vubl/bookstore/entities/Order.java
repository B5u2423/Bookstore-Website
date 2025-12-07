package dev.vubl.bookstore.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.List;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "orders")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order extends BaseEntity {
  @ManyToOne private ApplicationUser customer;

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
  @JsonManagedReference
  private List<OrderItem> items;

  // TODO: Payment method

  @Column(name = "city")
  private String city; // tỉnh/thành

  @Column(name = "commune")
  private String commune; // xã/phường

  @Column(name = "street")
  private String street;
}
