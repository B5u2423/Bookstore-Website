package dev.vubl.bookstore.services;

import dev.vubl.bookstore.repos.UserRepo;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  private final UserRepo userRepo;

  public UserService(UserRepo userRepo) {
    this.userRepo = userRepo;
  }
}
