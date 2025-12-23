package dev.vubl.bookstore.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "collections")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Collection extends BaseEntity {
  @Column(name = "collection_name")
  private String collectionName;

  @Column(name = "collection_slug")
  private String collectionSlug;
}
