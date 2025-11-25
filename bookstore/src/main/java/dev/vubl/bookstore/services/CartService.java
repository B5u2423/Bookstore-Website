package dev.vubl.bookstore.services;

import dev.vubl.bookstore.dtos.AddToCartRequest;
import dev.vubl.bookstore.entities.*;
import dev.vubl.bookstore.exceptions.BookDoesNotExistException;
import dev.vubl.bookstore.exceptions.UserDoesNotExistException;
import dev.vubl.bookstore.repos.BookRepo;
import dev.vubl.bookstore.repos.CartItemRepo;
import dev.vubl.bookstore.repos.CartRepo;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {
  private final CartRepo cartRepo;
  private final CartItemRepo cartItemRepo;
  private final ApplicationUserService userService;
  private final TokenService tokenService;
  private final BookRepo bookRepo;
  private final AuthService authService;

  public void addToCart(AddToCartRequest payload, String token) {
    String userEmail = tokenService.extractUserEmailFromToken(token);
    ApplicationUser user = userService.readUserByEmail(userEmail);

    // find user's active cart or create new one
    Cart cart =
        cartRepo
            .findCartByUser(user)
            .orElseGet(
                () ->
                    cartRepo.save(Cart.builder().cartStatus(CartStatus.ACTIVE).user(user).build()));

    Book book = bookRepo.findById(payload.bookId()).orElseThrow(BookDoesNotExistException::new);

    Optional<CartItem> ci = cartItemRepo.findByBookAndCart(book, cart);
    if (ci.isPresent()) {
      ci.get().setQuantity(ci.get().getQuantity() + payload.quantity());
    } else {
      cartItemRepo.save(
          CartItem.builder().cart(cart).book(book).quantity(payload.quantity()).build());
    }
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
}
