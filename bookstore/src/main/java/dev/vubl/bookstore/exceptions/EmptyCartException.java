package dev.vubl.bookstore.exceptions;

public class EmptyCartException extends RuntimeException {
  public EmptyCartException(String message) {
    super(message);
  }
}
