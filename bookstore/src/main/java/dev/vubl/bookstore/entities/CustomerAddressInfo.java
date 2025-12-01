package dev.vubl.bookstore.entities;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "customer_addresses")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAddressInfo extends BaseEntity {
  // new VN address structure
  @Column(name = "city")
  private String city; // tỉnh/thành

  @Column(name = "commune")
  private String commune; // xã/phường

  @Column(name = "street")
  private String street;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private ApplicationUser customer;
}
