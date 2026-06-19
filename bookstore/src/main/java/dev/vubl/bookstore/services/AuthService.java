package dev.vubl.bookstore.services;

import static dev.vubl.bookstore.utils.Constants.*;
import static dev.vubl.bookstore.utils.Constants.ATTR_EMAIL;
import static dev.vubl.bookstore.utils.Constants.ATTR_FACEBOOK_ID;
import static dev.vubl.bookstore.utils.Constants.ATTR_GOOGLE_ID;
import static dev.vubl.bookstore.utils.Constants.ATTR_USERNAME;

import dev.vubl.bookstore.dtos.*;
import dev.vubl.bookstore.entities.ApplicationUser;
import dev.vubl.bookstore.entities.UserType;
import dev.vubl.bookstore.exceptions.InvalidCredentialException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
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

  public RegistrationResponse registerUser(
      RegistrationRequest request, String oauthProvider, String id) {
    ApplicationUser newUser =
        ApplicationUser.builder()
            .userType(request.userType())
            .name(request.name())
            .email(request.email())
            .password(passwordEncoder.encode(request.password()))
            .build();
    switch (oauthProvider) {
      case IDP_GOOGLE -> newUser.setGoogleId(id);
      case IDP_FACEBOOK -> newUser.setFacebookId(id);
    }
    ApplicationUser u = userService.createOrUpdateUser(newUser);
    return RegistrationResponse.builder().email(u.getEmail()).name(u.getName()).build();
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

  public LoginResponse logInOAuth(OAuth2User user, String registrationId) {
    try {
      String email, name, id;

      // get provider: google or facebook
      email = user.getAttribute(ATTR_EMAIL);
      name = user.getAttribute(ATTR_USERNAME);
      switch (registrationId) {
        case IDP_GOOGLE -> {
          id = user.getAttribute(ATTR_GOOGLE_ID);
        }
        case IDP_FACEBOOK -> {
          id = user.getAttribute(ATTR_FACEBOOK_ID);
        }
        default -> throw new IllegalStateException("Invalid provider!");
      }

      // registration if user does not exist
      if (!userService.isUserExistByEmail(email)) {
        registerUser(
            RegistrationRequest.builder()
                .userType(UserType.CUSTOMER)
                .email(email)
                .name(name)
                .password(UUID.randomUUID().toString())
                .build(),
            registrationId,
            id);
      }
      ApplicationUser au = userService.readUserByEmail(email);
      // generate access token
      String jwtToken = tokenService.generateJwt(au);
      // generate refresh token
      String refreshToken = tokenService.generateRefreshToken(au).getRefreshToken();

      return LoginResponse.builder().token(jwtToken).refresh(refreshToken).build();
    } catch (Exception e) {
      log.error("Error log in/register via OAuth2.0");
      throw new IllegalArgumentException("Some thing is wrong with OAuth y'know...");
    }
  }

  public ApplicationUser readUserFromToken(String token) {
    String email = tokenService.extractUserEmailFromToken(token);
    return userService.readUserByEmail(email);
  }
}
