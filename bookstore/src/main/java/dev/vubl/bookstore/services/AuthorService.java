package dev.vubl.bookstore.services;

import dev.vubl.bookstore.dtos.AuthorDTO;
import dev.vubl.bookstore.entities.Author;
import dev.vubl.bookstore.repos.AuthorRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthorService {
  private final AuthorRepo authorRepo;

  public void deleteAuthor(AuthorDTO authorDTO) {}

  private Author mapToAuthorEntity(AuthorDTO authorDTO) {
    return Author.builder().authorName(authorDTO.name()).build();
  }
}
