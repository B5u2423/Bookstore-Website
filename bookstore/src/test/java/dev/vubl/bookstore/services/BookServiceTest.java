package dev.vubl.bookstore.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import dev.vubl.bookstore.dtos.BookDTO;
import dev.vubl.bookstore.entities.Author;
import dev.vubl.bookstore.entities.Book;
import dev.vubl.bookstore.repos.AuthorRepo;
import dev.vubl.bookstore.repos.BookRepo;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

  @Mock private BookRepo bookRepo;

  @Mock private AuthorRepo authorRepo;

  @InjectMocks private BookService bookService;

  private Book testBook;
  private Author testAuthor;
  private BookDTO expectedBookDTO;

  @BeforeEach
  void setUp() {
    testAuthor = Author.builder().authorName("Test Author").build();

    testBook =
        Book.builder()
            .isbn("978-1234567890")
            .title("Test Book Title")
            .description("Test Book Description")
            .price(BigDecimal.valueOf(29.99))
            .inStock(10)
            .authors(List.of(testAuthor))
            .build();

    expectedBookDTO =
        BookDTO.builder()
            .isbn("978-1234567890")
            .title("Test Book Title")
            .description("Test Book Description")
            .price(BigDecimal.valueOf(29.99))
            .inStock(10)
            .build();
  }

  @Test
  void getAllBooks_ShouldReturnListOfBookDTOs() {
    // Given
    List<Book> books = Collections.singletonList(testBook);
    when(bookRepo.findAll()).thenReturn(books);

    // When
    List<BookDTO> result = bookService.getAllBooks();

    // Then
    assertNotNull(result);
    assertEquals(1, result.size());
    BookDTO actualBookDTO = result.getFirst();
    assertEquals(expectedBookDTO.isbn(), actualBookDTO.isbn());
    assertEquals(expectedBookDTO.title(), actualBookDTO.title());
    assertEquals(expectedBookDTO.description(), actualBookDTO.description());
    assertEquals(expectedBookDTO.price(), actualBookDTO.price());
    assertEquals(expectedBookDTO.inStock(), actualBookDTO.inStock());

    verify(bookRepo, times(1)).findAll();
  }

  @Test
  void getAllBooks_WithEmptyList_ShouldReturnEmptyList() {
    // Given
    when(bookRepo.findAll()).thenReturn(List.of());

    // When
    List<BookDTO> result = bookService.getAllBooks();

    // Then
    assertNotNull(result);
    assertTrue(result.isEmpty());
    verify(bookRepo, times(1)).findAll();
  }

  @Test
  void getAllBooks_WithMultipleBooks_ShouldReturnAllBookDTOs() {
    // Given
    Author author2 = Author.builder().authorName("Author 2").build();
    Book book2 =
        Book.builder()
            .isbn("978-0987654321")
            .title("Second Book")
            .description("Second Description")
            .price(BigDecimal.valueOf(19.99))
            .inStock(5)
            .authors(List.of(author2))
            .build();

    List<Book> books = Arrays.asList(testBook, book2);
    when(bookRepo.findAll()).thenReturn(books);

    // When
    List<BookDTO> result = bookService.getAllBooks();

    // Then
    assertNotNull(result);
    assertEquals(2, result.size());

    // Verify first book
    BookDTO firstBookDTO = result.getFirst();
    assertEquals(expectedBookDTO.isbn(), firstBookDTO.isbn());
    assertEquals(expectedBookDTO.title(), firstBookDTO.title());

    // Verify second book
    BookDTO secondBookDTO = result.get(1);
    assertEquals("978-0987654321", secondBookDTO.isbn());
    assertEquals("Second Book", secondBookDTO.title());

    verify(bookRepo, times(1)).findAll();
  }

  @Test
  void mapToBookDTO_ShouldCorrectlyMapBookToBookDTO() {
    // This test uses reflection to access the private method
    // Given - using the setup from @BeforeEach

    // When
    List<BookDTO> result;
    when(bookRepo.findAll()).thenReturn(List.of(testBook));

    // Invoke the service to trigger mapping
    result = bookService.getAllBooks();

    // Then
    assertNotNull(result);
    assertEquals(1, result.size());
    BookDTO mappedDTO = result.getFirst();

    assertEquals(testBook.getIsbn(), mappedDTO.isbn());
    assertEquals(testBook.getTitle(), mappedDTO.title());
    assertEquals(testBook.getDescription(), mappedDTO.description());
    assertEquals(testBook.getPrice(), mappedDTO.price());
    assertEquals(testBook.getInStock(), mappedDTO.inStock());
  }

  @Test
  void mapToBookDTO_WithNullValues_ShouldHandleGracefully() {
    // Given
    Book bookWithNulls =
        Book.builder()
            .isbn(null)
            .title(null)
            .description(null)
            .price(null)
            .inStock(0)
            .authors(List.of())
            .build();

    when(bookRepo.findAll()).thenReturn(List.of(bookWithNulls));

    // When
    List<BookDTO> result = bookService.getAllBooks();

    // Then
    assertNotNull(result);
    assertEquals(1, result.size());
    BookDTO mappedDTO = result.getFirst();

    assertNull(mappedDTO.isbn());
    assertNull(mappedDTO.title());
    assertNull(mappedDTO.description());
    assertNull(mappedDTO.price());
    assertEquals(0, mappedDTO.inStock());
  }
}
