package dev.vubl.bookstore.services;

import dev.vubl.bookstore.entities.ApplicationUser;
import dev.vubl.bookstore.exceptions.UnableToRegisterApplicationUserException;
import dev.vubl.bookstore.exceptions.UserDoesNotExistException;
import dev.vubl.bookstore.repos.ApplicationUserRepo;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ApplicationUserService implements UserDetailsService {
  private final ApplicationUserRepo userRepo;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    try {
      ApplicationUser user = readUserByEmailOrThrowException(username);
      return User.builder()
          .username(username)
          .password(user.getPassword())
          .authorities(user.getUserType().toString())
          .build();
    } catch (UsernameNotFoundException e) {
      throw new UsernameNotFoundException("User does not exist!");
    }
  }

  public ApplicationUser createOrUpdateUser(ApplicationUser user) {
    try {
      return userRepo.save(user);
    } catch (Exception e) {
      throw new UnableToRegisterApplicationUserException();
    }
  }

  public List<ApplicationUser> readAllUsers() {
    return userRepo.findAll();
  }

  public void deleteUser(String email) {
    ApplicationUser user = readUserByEmailOrThrowException(email);
    userRepo.delete(user);
  }

  private ApplicationUser readUserByEmailOrThrowException(String email) {
    return userRepo.findByEmail(email).orElseThrow(UserDoesNotExistException::new);
  }
}
