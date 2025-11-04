package dev.vubl.bookstore.services;

import dev.vubl.bookstore.dtos.RegistrationRequest;
import dev.vubl.bookstore.dtos.RegistrationResponse;
import dev.vubl.bookstore.entities.ApplicationUser;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
  private final ApplicationUserService userService;
  private final PasswordEncoder passwordEncoder;

  public RegistrationResponse registerUser(RegistrationRequest request) {
    ApplicationUser newUser =
        userService.createOrUpdateUser(
            ApplicationUser.builder()
                .userType(request.userType())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .build());
    return RegistrationResponse.builder().createdUser(newUser).build();
  }

  public void loginUser() {
    return;
  }
}
