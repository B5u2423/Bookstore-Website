package dev.vubl.bookstore.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "books")
@Entity
public class Book extends BaseEntity {
  @Column(name = "isbn", unique = true)
  private String isbn;

  @Column(name = "title")
  private String title;

  @Column(name = "description")
  private String description;

  @ManyToMany
  @JoinTable(
      name = "book_author",
      joinColumns = @JoinColumn(name = "book_id"),
      inverseJoinColumns = @JoinColumn(name = "author_id"))
  private List<Author> authors = new ArrayList<>();

  @Column(name = "price")
  private BigDecimal price;
}
