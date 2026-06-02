package dev.vubl.bookstore;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {"spring.profiles.active=local,h2"})
class BookstoreApplicationTests {}
