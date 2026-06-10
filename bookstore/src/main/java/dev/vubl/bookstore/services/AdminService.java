package dev.vubl.bookstore.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
  private final OrderService orderService;
  private final ApplicationUserService userService;
  private final BookService bookService;

  public void getDashboardData() {}
}
