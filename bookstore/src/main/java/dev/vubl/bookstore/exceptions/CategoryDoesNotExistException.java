package dev.vubl.bookstore.exceptions;

public class CategoryDoesNotExistException extends RuntimeException {

  public CategoryDoesNotExistException(String message) {
    super(message);
  }

  public CategoryDoesNotExistException() {
    super("Category does not exist");
  }
}
