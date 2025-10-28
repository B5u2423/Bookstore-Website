package dev.vubl.bookstore.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "books")
public class Book {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer bookId;

  private String title;
}
