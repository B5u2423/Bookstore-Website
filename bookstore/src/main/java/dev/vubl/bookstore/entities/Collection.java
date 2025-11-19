package dev.vubl.bookstore.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "collections")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Collection extends BaseEntity {
  @Column(name = "collection_name")
  private String genreName;

  @ManyToMany(mappedBy = "collections")
  private List<Book> books;
}
