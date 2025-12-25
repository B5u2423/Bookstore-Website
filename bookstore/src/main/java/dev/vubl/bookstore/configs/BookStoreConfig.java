package dev.vubl.bookstore.configs;

import com.cloudinary.Cloudinary;
import dev.vubl.bookstore.services.ApplicationUserService;
import dev.vubl.bookstore.services.AuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BookStoreConfig {
  @Value("${my.cloud.url}")
  private String cloudinaryURL;

  @Bean
  public Cloudinary cloudinary() {
    return new Cloudinary(cloudinaryURL);
  }

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

  @Bean
  public AuthenticationSuccessHandler oAuth2SuccessHandler() {
    return (request, response, authentication) -> {
      OAuth2User user = (OAuth2User) authentication.getPrincipal();

      String email = user.getAttribute("email");
      String name = user.getAttribute("name");
      String googleId = user.getAttribute("sub");


      // 1. Find or create local user
      System.out.println(name);
      System.out.println(email);
      System.out.println(googleId);
      response.sendRedirect("https://example.com");
    };
  }
}
