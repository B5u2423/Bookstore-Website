package dev.vubl.bookstore.configs;

import static dev.vubl.bookstore.utils.Constants.*;

import dev.vubl.bookstore.dtos.LoginResponse;
import dev.vubl.bookstore.services.ApplicationUserService;
import dev.vubl.bookstore.services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
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
      throws IOException {
    if (!(authentication instanceof OAuth2AuthenticationToken oauthToken)) {
      throw new IllegalArgumentException("Unexpected authentication type");
    }

    OAuth2User user = (OAuth2User) authentication.getPrincipal();

    String registrationId = oauthToken.getAuthorizedClientRegistrationId();
    // login
    LoginResponse res = authService.logInOAuth(user, registrationId);
    // redirect
    String redirectUrl =
        "%s/oauth2/callback?token=%s&ref=%s".formatted(frontEndUrl, res.token(), res.refresh());
    getRedirectStrategy().sendRedirect(request, response, redirectUrl);
  }
}
