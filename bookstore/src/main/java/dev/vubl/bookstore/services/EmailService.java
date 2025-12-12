package dev.vubl.bookstore.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
  private final JavaMailSender javaMailSender;

  @Value("${spring.mail.username}")
  private String sender;

  public void sendEmail(String to, String subject, String body) {
    SimpleMailMessage message = new SimpleMailMessage();

    message.setFrom(sender);
    message.setSubject(subject);
    message.setTo(to);
    message.setText(body);

    javaMailSender.send(message);
  }
}
