package dev.vubl.bookstore.exceptions;

public class CategoryWithSlugAlreadyExists extends RuntimeException {

  public CategoryWithSlugAlreadyExists(String slug) {
    super("Category with slug %s already exists".formatted(slug));
  }
}
