package dev.vubl.bookstore.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "customer_addresses")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAddressInfo extends BaseEntity {
  @Column(name = "city_id")
  private Integer cityId;

  @Column(name = "city")
  private String city; // tỉnh/thành

  @Column(name = "commune_id")
  private Integer communeId; // tỉnh/thành

  @Column(name = "commune")
  private String commune; // xã/phường

  @Column(name = "street")
  private String street;

  @ManyToOne
  @JoinColumn(name = "user_id")
  @JsonBackReference
  private ApplicationUser customer;
}
