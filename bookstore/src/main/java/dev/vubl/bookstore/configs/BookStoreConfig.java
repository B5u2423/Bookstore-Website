package dev.vubl.bookstore.configs;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
}
