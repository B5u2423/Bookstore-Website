package dev.vubl.bookstore.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "suppliers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Supplier extends BaseEntity {
  @Column(name = "supplier_name")
  private String supplierName;

  @OneToMany(mappedBy = "supplier")
  private List<Book> books;
}
