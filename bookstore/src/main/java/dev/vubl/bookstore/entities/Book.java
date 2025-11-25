package dev.vubl.bookstore.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
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

  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinTable(
      name = "book_author",
      joinColumns = @JoinColumn(name = "book_id"),
      inverseJoinColumns = @JoinColumn(name = "author_id"))
  private List<Author> authors;

  @Column(name = "price")
  private BigDecimal price;

  @Column(name = "in_stock")
  private Integer inStock;

  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinTable(
      name = "book_collection",
      joinColumns = @JoinColumn(name = "book_id"),
      inverseJoinColumns = @JoinColumn(name = "collection_id"))
  private List<Collection> collections;

  // TODO: Add categories

  // display on Front-end

  @Column(name = "product_code", unique = true)
  private String productCode; // mã hàng

  @ManyToOne
  @JoinColumn(name = "supplier_id")
  private Supplier supplier; // tên nhà cung cấp

  @ManyToOne
  @JoinColumn(name = "publisher_id")
  private Publisher publisher; // NXB

  @Column(name = "publish_year")
  private Integer publishYear; // năm xuất bản

  @Column(name = "language")
  private String language; // ngôn ngữ

  @Column(name = "weight")
  private Integer weightGrams; // trọng lượng (gr)

  @Column(name = "dimensions")
  private String dimensions; // kích thước bao bì

  @Column(name = "page_count")
  private Integer pageCount; // số trang

  @Column(name = "format")
  private String format; // hình thức (bìa mềm, bìa cứng)

  @Column(name = "image_url")
  private String imageUrl;
}
