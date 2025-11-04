package dev.vubl.bookstore.exceptions;

public class InvalidCredentialException extends RuntimeException {
  public InvalidCredentialException(String message) {
    super(message);
  }

  public InvalidCredentialException() {
    super("Invalid password or email. Please re-authenticate!");
  }
}
