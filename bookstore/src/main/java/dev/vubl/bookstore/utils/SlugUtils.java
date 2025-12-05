package dev.vubl.bookstore.utils;

import java.text.Normalizer;

public class SlugUtils {
  public static String convertStringToSlug(String str) {

    if (str == null) throw new IllegalArgumentException("Input string must not be null");

    // normalize
    String normalized = Normalizer.normalize(str, Normalizer.Form.NFD);

    // remove diacritics
    String slug = normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    slug = slug.toLowerCase();
    // replace all non-alphanumeric characters with hyphens
    slug = slug.replaceAll("[^a-z0-9]+", "-");
    // Trim leading & trailing hyphens
    slug = slug.replaceAll("^-+|-+$", "");

    return slug;
  }
}
