package dev.vubl.bookstore.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.Set;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "categories")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category extends BaseEntity {
  @Column(name = "category_name")
  private String categoryName;

  @ManyToOne
  @JoinColumn(name = "parent_id", nullable = true)
  @JsonBackReference
  private Category parentCategory;

  @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL)
  @JsonManagedReference
  private Set<Category> childrenCategories;
}
