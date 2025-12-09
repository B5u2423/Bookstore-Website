package dev.vubl.bookstore.entities;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import lombok.*;

@Entity
@Table(name = "categories")
@Getter
@Setter
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

  public void addChild(Category child) {
    child.setParentCategory(this);
    if (this.childrenCategories == null) {
      this.setChildrenCategories(new HashSet<>());
    }
    this.childrenCategories.add(child);
  }

  public void removeChild(Category child) {
    this.childrenCategories.remove(child);
    child.setParentCategory(null);
  }

  public void removeAllChildren() {
    for (Category child : childrenCategories) {
      child.setParentCategory(null);
    }
    this.childrenCategories.clear();
  }

  // has to override these methods so category doesn't call super infinitely
  // thus lead to a SFO.

  @Override
  public String toString() {
    return "Category{"
        + "id="
        + getId()
        + ", categoryName='"
        + categoryName
        + '\''
        + ", categorySlug='"
        + categorySlug
        + '\''
        + '}';
  }

  // Override equals and hashCode to prevent infinite recursion
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Category category)) return false;
    return Objects.equals(getId(), category.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }
}
