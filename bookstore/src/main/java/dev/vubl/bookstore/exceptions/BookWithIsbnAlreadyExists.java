package dev.vubl.bookstore.exceptions;

public class BookWithIsbnAlreadyExists extends RuntimeException {
  public BookWithIsbnAlreadyExists(String message) {
    super(message);
  }
}
