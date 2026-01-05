package dev.vubl.bookstore.configs;

import static dev.vubl.bookstore.utils.Constants.*;

import dev.vubl.bookstore.dtos.LoginResponse;
import dev.vubl.bookstore.dtos.RegistrationRequest;
import dev.vubl.bookstore.entities.UserType;
import dev.vubl.bookstore.services.ApplicationUserService;
import dev.vubl.bookstore.services.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
  private final AuthService authService;
  private final ApplicationUserService userService;

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain,
      Authentication authentication)
      throws IOException, ServletException {
    AuthenticationSuccessHandler.super.onAuthenticationSuccess(
        request, response, chain, authentication);
  }

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException, ServletException {
    OAuth2User user = (OAuth2User) authentication.getPrincipal();
    OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;

    // get provider: google or facebook
    String registrationId = token.getAuthorizedClientRegistrationId();

    String email = "";
    String name = "";
    String id = "";

    switch (registrationId) {
      case IDP_GOOGLE -> {
        email = user.getAttribute(ATTR_EMAIL);
        name = user.getAttribute(ATTR_USERNAME);
        id = user.getAttribute(ATTR_GOOGLE_ID);
      }
      case IDP_FACEBOOK -> {
        email = user.getAttribute(ATTR_EMAIL);
        name = user.getAttribute(ATTR_USERNAME);
        id = user.getAttribute(ATTR_FACEBOOK_ID);
      }
      default -> throw new IllegalStateException("Invalid provider!");
    }
    // register if it does not exist
    if (!userService.isUserExistByEmail(email)) {
      authService.registerUser(
          RegistrationRequest.builder()
              .userType(UserType.CUSTOMER)
              .email(email)
              .name(name)
              .password(UUID.randomUUID().toString())
              .build(),
          registrationId,
          id);
    }
    // login
    LoginResponse res = authService.logInOAuth(email);

    Cookie access = new Cookie("access_token", res.token());
    Cookie refresh = new Cookie("refresh_token", res.refresh());
    access.setHttpOnly(false); // required for JS access
    access.setPath("/");
    refresh.setHttpOnly(false);
    refresh.setPath("/");

    // redirect
    response.addCookie(access);
    response.addCookie(refresh);
    response.sendRedirect("http://localhost:5173/oauth/callback");
  }
}
