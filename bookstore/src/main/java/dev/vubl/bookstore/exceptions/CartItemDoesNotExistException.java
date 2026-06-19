package dev.vubl.bookstore.exceptions;

public class CartItemDoesNotExistException extends RuntimeException {
  public CartItemDoesNotExistException() {
    super("CartItem does not exist");
  }
}
