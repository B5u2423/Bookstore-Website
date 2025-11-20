package dev.vubl.bookstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BookstoreApplication {

  public static void main(String[] args) {
    System.setProperty("user.timezone", "Asia/Ho_Chi_Minh");
    SpringApplication.run(BookstoreApplication.class, args);
  }
}
