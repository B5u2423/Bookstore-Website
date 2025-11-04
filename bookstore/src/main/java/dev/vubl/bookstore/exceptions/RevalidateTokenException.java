package dev.vubl.bookstore.exceptions;

public class RevalidateTokenException extends RuntimeException {
  public RevalidateTokenException(String message) {
    super(message);
  }

  public RevalidateTokenException() {
    super("Refresh token expired! Please re-authenticate!");
  }
}
