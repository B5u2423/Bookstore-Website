package dev.vubl.bookstore.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
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
  @Column(name = "email")
  private String email;

  @Column(name = "phone_number")
  private String phoneNumber;

  @Column(name = "name")
  private String name;

  @Column(name = "note")
  private String note;

  @Enumerated(EnumType.STRING)
  private OrderStatus orderStatus;

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
  @JsonManagedReference
  private List<OrderItem> items;

  @Enumerated(EnumType.STRING)
  private PaymentMethod paymentMethod;

  @Column(name = "items_total")
  private BigDecimal itemsTotal;

  @Column(name = "shipping_fee")
  private BigDecimal shippingFee;

  @Column(name = "order_total")
  private BigDecimal orderTotal;

  @Column(name = "coupon_code")
  private String couponCode;

  @Column(name = "order_date")
  private LocalDate orderDate;

  @Column(name = "city")
  private String city; // tỉnh/thành

  @Column(name = "commune")
  private String commune; // xã/phường

  @Column(name = "street")
  private String street;

  @Column(name = "vnp_txnref")
  private String vnpTxnRef;

  @Column(name = "discount_value")
  private BigDecimal discountValue;
}
