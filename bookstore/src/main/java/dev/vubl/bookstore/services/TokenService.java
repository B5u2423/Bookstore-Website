package dev.vubl.bookstore.services;

import dev.vubl.bookstore.entities.ApplicationUser;
import jakarta.transaction.Transactional;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.jmx.export.annotation.AnnotationMBeanExporter;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class TokenService {
  private final JwtEncoder jwtEncoder;
  private final JwtDecoder jwtDecoder;
  private final AnnotationMBeanExporter annotationMBeanExporter;

  public String generateJwt(ApplicationUser user) {
    Instant now = Instant.now();
    Instant expiration = now.plusSeconds(60 * 15);
    String scope = user.getUserType().toString();
    JwtClaimsSet jwtClaimsSet =
        JwtClaimsSet.builder()
            .issuer("self")
            .issuedAt(now)
            .expiresAt(expiration)
            .subject(user.getEmail())
            .claim("roles", scope)
            .build();
    return jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
  }

  private String splitToken(String token) {
    return token.split("Bearer ")[1];
  }
}
