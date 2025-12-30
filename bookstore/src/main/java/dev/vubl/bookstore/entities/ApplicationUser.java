package dev.vubl.bookstore.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.List;
import lombok.*;

@Entity
@Table(name = "users")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationUser extends BaseEntity {
  @Enumerated(EnumType.STRING)
  @Column(name = "user_type")
  private UserType userType;

  @Column(name = "email", unique = true)
  private String email;

  @Column(name = "name")
  private String name;

  @JsonIgnore private String password;

  @Column(name = "phone_number")
  private String phoneNumber;

  @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonManagedReference
  private List<CustomerAddressInfo> addressList;

  @Column(name = "google_id")
  private String googleId;

  @Column(name = "facebook_id")
  private String facebookId;
}
