package dev.vubl.bookstore.services;

import dev.vubl.bookstore.dtos.BookResponseDTO;
import dev.vubl.bookstore.entities.Book;
import dev.vubl.bookstore.exceptions.BookWithIsbnAlreadyExists;
import dev.vubl.bookstore.repos.AuthorRepo;
import dev.vubl.bookstore.repos.BookRepo;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BookService {
  private final BookRepo bookRepo;
  private final AuthorRepo authorRepo;

  public List<BookResponseDTO> getAllBooks() {
    return bookRepo.findAll().stream().map(this::mapToBookResponseDTO).toList();
  }

  public BookResponseDTO addOrUpdateBook(BookResponseDTO bookResponseDTO) {
    String isbn = bookResponseDTO.isbn();
    // some book will not have isbn
    if (isbn != null) {
      if (bookRepo.findByIsbn(isbn).isPresent()) {
        throw new BookWithIsbnAlreadyExists("Book with isbn :: %s already exists!".formatted(isbn));
      }
    }
    try {
      log.info("[{}] Adding new book", this.getClass().getName());
      return mapToBookResponseDTO(bookRepo.save(mapToBookEntity(bookResponseDTO)));
    } catch (DataIntegrityViolationException e) {
      throw new DataIntegrityViolationException("Error adding or updating new book!", e);
    }
  }

  public void deleteBook(BookResponseDTO bookResponseDTO) {
    bookRepo.delete(mapToBookEntity(bookResponseDTO));
    log.info("[{}] Book deleted", this.getClass().getName());
  }

  private BookResponseDTO mapToBookResponseDTO(Book book) {
    return BookResponseDTO.builder()
        .id(book.getId())
        .isbn(book.getIsbn())
        .title(book.getTitle())
        .description(book.getDescription())
        .price(book.getPrice())
        .inStock(book.getInStock())
        .productCode(book.getProductCode())
        .publishYear(book.getPublishYear())
        .language(book.getLanguage())
        .weightGrams(book.getWeightGrams())
        .dimensions(book.getDimensions())
        .pageCount(book.getPageCount())
        .format(book.getFormat())
        .imageUrl(book.getImageUrl())
        .build();
  }

  private Book mapToBookEntity(BookResponseDTO bookResponseDTO) {
    return Book.builder()
        .title(bookResponseDTO.title())
        .authors(bookResponseDTO.authors())
        .price(bookResponseDTO.price())
        .description(bookResponseDTO.description())
        .isbn(bookResponseDTO.isbn())
        .inStock(bookResponseDTO.inStock())
        .build();
  }
}
