package dev.vubl.bookstore.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
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

  private String author;

  @Column(name = "price")
  private BigDecimal price;

  @Column(name = "in_stock")
  private Integer inStock;

  @Column(name = "url_slug")
  private String urlSlug;

  // TODO: Add categories

  @Column(name = "publisher")
  private String publisher; // NXB

  @Column(name = "publish_year")
  private Integer publishYear; // năm xuất bản

  @Column(name = "page_count")
  private Integer pageCount; // số trang

  @Column(name = "image_url")
  private String imageUrl;
}
