package dev.vubl.bookstore.services;

import dev.vubl.bookstore.dtos.*;
import dev.vubl.bookstore.entities.ApplicationUser;
import dev.vubl.bookstore.exceptions.InvalidCredentialException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
  private final ApplicationUserService userService;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final TokenService tokenService;

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

  public LoginResponse logInUser(
      LoginRequest payload, HttpServletRequest request, HttpServletResponse response) {
    try {
      UsernamePasswordAuthenticationToken authenticationToken =
          UsernamePasswordAuthenticationToken.unauthenticated(payload.email(), payload.password());
      Authentication auth = authenticationManager.authenticate(authenticationToken);

      ApplicationUser user = userService.readUserByEmail(payload.email());
      // generate access token
      String jwtToken = tokenService.generateJwt(user);
      // generate refresh token
      String refreshToken = tokenService.generateRefreshToken(user).getRefreshToken();

      return LoginResponse.builder().token(jwtToken).refresh(refreshToken).build();
    } catch (Exception e) {
      throw new InvalidCredentialException();
    }
  }

  public void logOutUser(String jwt) {
    String email = tokenService.extractUserEmailFromToken(jwt);
    ApplicationUser user = userService.readUserByEmail(email);
    tokenService.deleteRefreshTokenByUser(user);
  }

  public ApplicationUser readUserFromToken(String token) {
    String email = tokenService.extractUserEmailFromToken(token);
    return userService.readUserByEmail(email);
  }
}
