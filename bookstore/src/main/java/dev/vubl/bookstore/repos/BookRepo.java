package dev.vubl.bookstore.repos;

import dev.vubl.bookstore.entities.Book;
import dev.vubl.bookstore.entities.Category;
import dev.vubl.bookstore.entities.Collection;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT b FROM Book b WHERE b.id = :id")
  Optional<Book> findByIdForUpdate(@Param("id") Integer id);

  @Modifying
  @Query("UPDATE Book b SET b.collection = null WHERE b.collection.id = :collectionId")
  void nullifyCollectionReference(@Param("collectionId") Integer collectionId);

  List<Book> findAllByCollection(Collection c);
}
