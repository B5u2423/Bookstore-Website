package dev.vubl.bookstore.entities;

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

  @Column(name = "category_slug")
  private String categorySlug;

  @ManyToOne
  @JoinColumn(name = "parent_id", nullable = true)
  private Category parentCategory;

  @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL)
  private Set<Category> childrenCategories;
}
