package dev.vubl.bookstore.exceptions;

public class CollectionDoesNotExistException extends RuntimeException {

  public CollectionDoesNotExistException(String s) {
    super("Collection with slug %s does not exist".formatted(s));
  }
}
