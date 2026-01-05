package dev.vubl.bookstore.services;

import dev.vubl.bookstore.dtos.CollectionDTO;
import dev.vubl.bookstore.entities.Collection;
import dev.vubl.bookstore.repos.CollectionRepo;
import dev.vubl.bookstore.utils.SlugUtils;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class CollectionService {
  private final CollectionRepo collectionRepo;

  public List<Collection> getAllCollections() {
    return collectionRepo.findAll();
  }

  public Page<Collection> getAllCollectionsPaginated(
      int page, int size, String sortBy, String order) {
    List<String> allowed = List.of("id", "name");
    if (!allowed.contains(sortBy)) {
      throw new IllegalArgumentException("Invalid sort field: %s".formatted(sortBy));
    }

    Sort sort = order.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
    Pageable pageable = PageRequest.of(page, size, sort);
    return collectionRepo.findAll(pageable);
  }

  public Collection addNewCollection(@Valid CollectionDTO payload) {
    Collection c =
        Collection.builder()
            .collectionName(payload.collectionName())
            .collectionSlug(SlugUtils.convertStringToSlug(payload.collectionName()))
            .build();
    return collectionRepo.save(c);
  }

  public Collection updateCollection(CollectionDTO payload) {
    Collection found =
        collectionRepo
            .findById(payload.id())
            .orElseThrow(() -> new IllegalArgumentException("Invalid collection id!"));
    found.setCollectionName(payload.collectionName());
    found.setCollectionSlug(SlugUtils.convertStringToSlug(payload.collectionName()));
    found.setUpdateTimeStamp(Instant.now());
    return collectionRepo.save(found);
  }

  public void deleteCollectionById(Integer id) {
    collectionRepo.deleteById(id);
  }
}
