package dev.vubl.bookstore;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {"spring.profiles.active=local,h2"})
class BookstoreApplicationTests {

  @Test
  void contextLoads() {}
}
