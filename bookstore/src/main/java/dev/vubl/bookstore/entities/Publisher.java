package dev.vubl.bookstore.entities;

import jakarta.persistence.*;
import java.util.List;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "publishers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Publisher extends BaseEntity {
  @Column(name = "publisher_name")
  private String publisherName;

  @OneToMany(mappedBy = "publisher")
  private List<Book> books;
}
