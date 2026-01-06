package dev.vubl.bookstore.repos;

import dev.vubl.bookstore.dtos.dashboard.CatalogHealthCountDTO;
import dev.vubl.bookstore.entities.Book;
import dev.vubl.bookstore.entities.Category;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepo extends JpaRepository<Book, Integer> {
  Optional<Book> findByIsbn(String isbn);

  Page<Book> findAllByCategoryIn(List<Category> categories, Pageable pageable);

  @Query(
      value =
          """
    SELECT *,
           ts_rank(search_vector, plainto_tsquery('simple', :keyword)) AS rank
    FROM books
    WHERE search_vector @@ plainto_tsquery('simple', :keyword)
    ORDER BY rank DESC
  """,
      nativeQuery = true)
  List<Book> searchBookV3(String keyword);

  @Query(
      """
        SELECT new dev.vubl.bookstore.dtos.dashboard.CatalogHealthCountDTO(
            SUM(CASE WHEN b.category IS NULL THEN 1 ELSE 0 END),
            SUM(CASE WHEN b.collection IS NULL THEN 1 ELSE 0 END),
            SUM(CASE WHEN b.inStock IS NULL OR b.inStock = 0 THEN 1 ELSE 0 END),
            SUM(CASE WHEN b.imageUrl IS NULL OR b.imageUrl = '' THEN 1 ELSE 0 END),
            SUM(CASE WHEN EXTRACT(YEAR FROM b.createTimeStamp) = EXTRACT(YEAR FROM CURRENT_DATE)
                      AND EXTRACT(MONTH FROM b.createTimeStamp) = EXTRACT(MONTH FROM CURRENT_DATE)
                THEN 1 ELSE 0 END),
            SUM(CASE WHEN b.inStock = 0 THEN 1 ELSE 0 END),
            (SELECT COUNT(c) FROM Category c),
            (SELECT COUNT(col) FROM Collection col),
            COUNT(b) / NULLIF((SELECT COUNT(c) FROM Category c), 0)
        )
        FROM Book b
    """)
  CatalogHealthCountDTO getCatalogHealthCounts();
}
