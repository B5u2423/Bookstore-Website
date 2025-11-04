package dev.vubl.bookstore.exceptions;

public class UserDoesNotExistException extends RuntimeException {
  public UserDoesNotExistException(String message) {
    super(message);
  }

  public UserDoesNotExistException() {
    super("User does not exist!");
  }
}
