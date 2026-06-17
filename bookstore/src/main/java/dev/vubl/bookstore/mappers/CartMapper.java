package dev.vubl.bookstore.mappers;

import dev.vubl.bookstore.dtos.CartDto;
import dev.vubl.bookstore.dtos.CartItemDto;
import dev.vubl.bookstore.entities.Cart;
import dev.vubl.bookstore.entities.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CartMapper {
  CartMapper INSTANCE = Mappers.getMapper(CartMapper.class);

  @Mapping(source = "user.id", target = "userId")
  @Mapping(source = "id", target = "cartId")
  CartDto toDto(Cart c);

  @Mapping(source = "book.id", target = "bookId")
  @Mapping(source = "book.title", target = "bookTitle")
  @Mapping(source = "book.price", target = "bookPrice")
  @Mapping(source = "book.urlSlug", target = "bookSlug")
  @Mapping(source = "book.imageUrl", target = "bookImage")
  @Mapping(source = "book.author", target = "bookAuthor")
  CartItemDto toCartItemDto(CartItem ci);
}
