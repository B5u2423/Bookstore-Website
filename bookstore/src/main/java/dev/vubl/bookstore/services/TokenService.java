package dev.vubl.bookstore.services;

import dev.vubl.bookstore.dtos.LoginResponse;
import dev.vubl.bookstore.entities.ApplicationUser;
import dev.vubl.bookstore.entities.RefreshToken;
import dev.vubl.bookstore.exceptions.RevalidateTokenException;
import dev.vubl.bookstore.repos.RefreshTokenRepo;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class TokenService {
  private final JwtEncoder jwtEncoder;
  private final JwtDecoder jwtDecoder;
  private final RefreshTokenRepo refreshTokenRepo;

  // TODO: make expiration for tokens as ENV VAR
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

  public RefreshToken generateRefreshToken(ApplicationUser user) {
    Optional<RefreshToken> token = refreshTokenRepo.findByUser(user);
    // user is already logged in
    if (token.isPresent()) {
      this.deleteRefreshTokenByUser(user);
      refreshTokenRepo.flush();
    }
    RefreshToken refreshToken =
        RefreshToken.builder()
            .refreshToken(UUID.randomUUID().toString())
            .expiration(Instant.now().plusSeconds(60 * 24 * 60))
            .user(user)
            .build();
    return refreshTokenRepo.save(refreshToken);
  }

  public LoginResponse refreshJwt(String token) {
    RefreshToken refreshToken =
        refreshTokenRepo.findByRefreshToken(token).orElseThrow(RevalidateTokenException::new);
    Instant now = Instant.now();
    ApplicationUser user = refreshToken.getUser();
    if (now.isAfter(refreshToken.getExpiration())) {
      this.deleteRefreshTokenByUser(user);
      throw new RevalidateTokenException();
    }
    // implementing non-rotating refresh token
    // previous refresh token will be revoked
    this.deleteRefreshTokenByUser(user);
    refreshTokenRepo.flush();
    String jwtToken = this.generateJwt(user);
    RefreshToken newRefreshToken = this.generateRefreshToken(user);
    return LoginResponse.builder()
        .token(jwtToken)
        .refresh(newRefreshToken.getRefreshToken())
        .build();
  }

  public void deleteRefreshTokenByUser(ApplicationUser user) {
    Optional<RefreshToken> refreshToken = refreshTokenRepo.findByUser(user);
    if (refreshToken.isEmpty()) {
      return;
    }
    refreshTokenRepo.delete(refreshToken.get());
  }

  public String extractUserEmailFromToken(String token) {
    Jwt jwt = jwtDecoder.decode(splitToken(token));
    return jwt.getSubject();
  }

  private String splitToken(String token) {
    return token.split("Bearer ")[1];
  }
}
