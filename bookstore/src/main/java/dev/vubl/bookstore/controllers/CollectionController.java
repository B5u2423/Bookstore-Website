package dev.vubl.bookstore.controllers;

import dev.vubl.bookstore.dtos.CollectionDTO;
import dev.vubl.bookstore.entities.Collection;
import dev.vubl.bookstore.services.CollectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/collections")
@RequiredArgsConstructor
public class CollectionController {
  private final CollectionService collectionService;

  @GetMapping
  public PagedModel<Collection> getAllCoupons(
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "5") int size,
      @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
      @RequestParam(value = "order", defaultValue = "asc") String order) {
    return new PagedModel<>(collectionService.getAllCollections(page, size, sortBy, order));
  }

  @PostMapping("/add")
  public ResponseEntity<Collection> addNewCoupon(@Valid @RequestBody CollectionDTO payload) {
    return ResponseEntity.ok().body(collectionService.addNewCollection(payload));
  }

  @PutMapping("/update")
  public ResponseEntity<Collection> updateCoupon(@Valid @RequestBody CollectionDTO payload) {
    return ResponseEntity.ok().body(collectionService.updateCollection(payload));
  }

  @DeleteMapping("/delete")
  public ResponseEntity<String> deleteCoupon(@RequestParam(value = "id") Integer id) {
    collectionService.deleteCollectionById(id);
    return ResponseEntity.ok().body("Ok");
  }
}
