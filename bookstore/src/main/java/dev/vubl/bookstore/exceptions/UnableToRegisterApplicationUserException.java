package dev.vubl.bookstore.exceptions;

public class UnableToRegisterApplicationUserException extends RuntimeException {
  public UnableToRegisterApplicationUserException() {
    super("Unable to register user!");
  }

  public UnableToRegisterApplicationUserException(String message) {
    super(message);
  }
}
