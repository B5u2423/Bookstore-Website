package dev.vubl.bookstore.configs;

import static dev.vubl.bookstore.utils.Constants.*;

import dev.vubl.bookstore.dtos.LoginResponse;
import dev.vubl.bookstore.dtos.RegistrationRequest;
import dev.vubl.bookstore.entities.UserType;
import dev.vubl.bookstore.services.ApplicationUserService;
import dev.vubl.bookstore.services.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
  private final AuthService authService;
  private final ApplicationUserService userService;

  @Value("${app.frontend-url}")
  private String frontEndUrl;

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException, ServletException {
    OAuth2User user = (OAuth2User) authentication.getPrincipal();
    OAuth2AuthenticationToken oAuth2AuthenticationToken =
        (OAuth2AuthenticationToken) authentication;

    // get provider: google or facebook
    String registrationId = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();
    String email, name, id;

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

    // redirect
    String redirectUrl =
        "%s/oauth2/callback?token=%s&ref=%s".formatted(frontEndUrl, res.token(), res.refresh());
    getRedirectStrategy().sendRedirect(request, response, redirectUrl);
  }
}
