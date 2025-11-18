package dev.vubl.bookstore.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "genres")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Genre extends BaseEntity {
  @Column(name = "genre_name")
  private String genreName;

  @ManyToMany(mappedBy = "genres")
  private List<Book> books = new ArrayList<>();
}
