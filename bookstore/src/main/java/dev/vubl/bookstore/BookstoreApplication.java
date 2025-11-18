package dev.vubl.bookstore;

import dev.vubl.bookstore.services.AuthService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BookstoreApplication {

  public static void main(String[] args) {
    System.setProperty("user.timezone", "Asia/Ho_Chi_Minh");
    SpringApplication.run(BookstoreApplication.class, args);
  }

  // TODO: Remove this later when finish setting up database and image bucket
  @Bean
  CommandLineRunner initForTesting(AuthService authService) {
    return args -> {
      authService.loadAdminUser();
    };
  }
}
