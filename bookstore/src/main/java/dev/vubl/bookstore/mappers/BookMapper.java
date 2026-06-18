package dev.vubl.bookstore.mappers;

import dev.vubl.bookstore.dtos.BookDTO;
import dev.vubl.bookstore.entities.Book;
import dev.vubl.bookstore.entities.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BookMapper {
  BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

  @Mapping(source = "id", target = "bookId")
  @Mapping(source = "title", target = "titleAtPurchase")
  @Mapping(source = "price", target = "priceAtPurchase")
  // these must be set after eval
  OrderItem toOrderItemEntity(Book b);

  Book toBook(BookDTO dto);

  @Mapping(source = "category.id", target = "categoryId")
  @Mapping(source = "category.categoryName", target = "categoryName")
  @Mapping(source = "collection.id", target = "collectionId")
  @Mapping(source = "collection.collectionName", target = "collectionName")
  BookDTO toDto(Book b);
}
