package dev.vubl.bookstore.services;

import dev.vubl.bookstore.dtos.AddToCartRequest;
import dev.vubl.bookstore.dtos.CartDto;
import dev.vubl.bookstore.entities.*;
import dev.vubl.bookstore.exceptions.BookDoesNotExistException;
import dev.vubl.bookstore.exceptions.UserDoesNotExistException;
import dev.vubl.bookstore.mappers.CartMapper;
import dev.vubl.bookstore.repos.BookRepo;
import dev.vubl.bookstore.repos.CartItemRepo;
import dev.vubl.bookstore.repos.CartRepo;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CartService {
  private final CartRepo cartRepo;
  private final CartItemRepo cartItemRepo;
  private final BookRepo bookRepo;
  private final AuthService authService;

  public void addToCart(AddToCartRequest payload, String token) {
    // find user's active cart or create new one
    Cart cart = getActiveCartByUser(token);
    addItemToUserCartHelper(cart, payload);
  }

  public Cart getActiveCartByUser(String token) {
    ApplicationUser user = authService.readUserFromToken(token);
    if (user == null) {
      throw new UserDoesNotExistException("User does not exist!");
    }
    // if cart does not exist, create new cart
    Optional<Cart> c = cartRepo.findCartByUserAndCartStatus(user, CartStatus.ACTIVE);
    return c.orElseGet(
        () -> cartRepo.save(Cart.builder().cartStatus(CartStatus.ACTIVE).user(user).build()));
  }

  public CartDto getActiveUserCartToDto(String token) {
    return CartMapper.INSTANCE.toDto(getActiveCartByUser(token));
  }

  public void syncCart(String token, List<AddToCartRequest> payload) {
    log.info("Start removing all items in cart");
    removeAllItemsHelper(token);
    log.info("Start re-adding all items in cart");
    Cart cart = getActiveCartByUser(token);
    payload.forEach(r -> addItemToUserCartHelper(cart, r));
    log.info("[{}] Sync cart items successfully", this.getClass().getName());
  }

  public void removeItemFromCart(String token, Integer bookId) {
    // user's cart
    Cart c = getActiveCartByUser(token);
    // book
    Book b = bookRepo.findById(bookId).orElseThrow(BookDoesNotExistException::new);
    cartItemRepo.deleteByCartAndBook(c, b);
  }

  private void addItemToUserCartHelper(Cart c, AddToCartRequest r) {
    Book book = bookRepo.findById(r.bookId()).orElseThrow(BookDoesNotExistException::new);
    Optional<CartItem> ci = cartItemRepo.findByBookAndCart(book, c);
    if (ci.isPresent()) {
      ci.get().setQuantity(ci.get().getQuantity() + r.quantity());
      log.info(
          "CartItemId {} increase by {}, in cartId {}", ci.get().getId(), r.quantity(), c.getId());
    } else {
      cartItemRepo.save(CartItem.builder().cart(c).book(book).quantity(r.quantity()).build());
      log.info("New item added to cart with id {}", c.getId());
    }
  }

  private void removeAllItemsHelper(String token) {
    try {
      Cart c = getActiveCartByUser(token);
      cartItemRepo.deleteByCart(c);
      c.getItems().clear();
      cartRepo.save(c);
      log.info("[{}] Remove all item from cart", this.getClass().getName());
    } catch (Exception e) {
      log.error("[{}] Error remove all items from cart", this.getClass().getName());
    }
  }
}
