package dev.vubl.bookstore.exceptions;

public class BookDoesNotExistException extends RuntimeException {

  public BookDoesNotExistException(String message) {
    super(message);
  }

  public BookDoesNotExistException() {
    super("Book does not exist");
  }
}
