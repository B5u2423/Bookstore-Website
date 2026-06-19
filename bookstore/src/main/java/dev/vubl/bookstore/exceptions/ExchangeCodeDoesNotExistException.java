package dev.vubl.bookstore.exceptions;

public class ExchangeCodeDoesNotExistException extends RuntimeException {
  public ExchangeCodeDoesNotExistException() {
    super("Exchange code does not exist");
  }
}
