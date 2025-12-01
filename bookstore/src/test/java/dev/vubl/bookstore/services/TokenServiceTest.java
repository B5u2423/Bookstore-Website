package dev.vubl.bookstore.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import dev.vubl.bookstore.dtos.LoginResponse;
import dev.vubl.bookstore.entities.ApplicationUser;
import dev.vubl.bookstore.entities.RefreshToken;
import dev.vubl.bookstore.entities.UserType;
import dev.vubl.bookstore.exceptions.RevalidateTokenException;
import dev.vubl.bookstore.repos.RefreshTokenRepo;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

  @Mock private JwtEncoder jwtEncoder;

  @Mock private JwtDecoder jwtDecoder;

  @Mock private RefreshTokenRepo refreshTokenRepo;

  @InjectMocks private TokenService tokenService;

  private ApplicationUser testUser;
  private RefreshToken testRefreshToken;
  private Jwt testJwt;

  @BeforeEach
  void setUp() {
    testUser =
        ApplicationUser.builder()
            .userType(UserType.CUSTOMER)
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@example.com")
            .password("encodedPassword")
            .build();

    testRefreshToken =
        RefreshToken.builder()
            .refreshToken("test-refresh-token")
            .expiration(Instant.now().plusSeconds(60 * 24 * 60))
            .user(testUser)
            .build();

    testJwt = mock(Jwt.class);
  }

  @Test
  void generateJwt_ShouldReturnValidJwtToken() {
    // Given
    String expectedToken = "mocked-jwt-token";
    Jwt mockJwt = mock(Jwt.class);
    when(mockJwt.getTokenValue()).thenReturn(expectedToken);
    when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(mockJwt);

    // When
    String result = tokenService.generateJwt(testUser);

    // Then
    assertNotNull(result);
    assertEquals(expectedToken, result);
    verify(jwtEncoder, times(1)).encode(any(JwtEncoderParameters.class));
  }

  @Test
  void generateJwt_WithAdminUser_ShouldSetCorrectRole() {
    // Given
    ApplicationUser adminUser =
        ApplicationUser.builder().userType(UserType.ADMIN).email("admin@company.com").build();

    String expectedToken = "admin-jwt-token";
    Jwt mockJwt = mock(Jwt.class);
    when(mockJwt.getTokenValue()).thenReturn(expectedToken);
    when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(mockJwt);

    // When
    String result = tokenService.generateJwt(adminUser);

    // Then
    assertNotNull(result);
    assertEquals(expectedToken, result);

    verify(jwtEncoder, times(1))
        .encode(
            argThat(
                params -> {
                  JwtClaimsSet claims = params.getClaims();
                  return claims.getClaim("roles").equals("ADMIN");
                }));
  }

  @Test
  void generateRefreshToken_WithNewUser_ShouldCreateNewToken() {
    // Given
    when(refreshTokenRepo.findByUser(testUser)).thenReturn(Optional.empty());
    when(refreshTokenRepo.save(any(RefreshToken.class))).thenReturn(testRefreshToken);

    // When
    RefreshToken result = tokenService.generateRefreshToken(testUser);

    // Then
    assertNotNull(result);
    assertEquals(testRefreshToken, result);

    verify(refreshTokenRepo, times(1)).findByUser(testUser);
    verify(refreshTokenRepo, never()).delete(any(RefreshToken.class));
    verify(refreshTokenRepo, never()).flush();
    verify(refreshTokenRepo, times(1)).save(any(RefreshToken.class));
  }

  @Test
  void generateRefreshToken_WithExistingUser_ShouldDeleteOldTokenAndCreateNew() {
    // Given
    RefreshToken existingToken =
        RefreshToken.builder().refreshToken("old-token").user(testUser).build();

    when(refreshTokenRepo.findByUser(testUser)).thenReturn(Optional.of(existingToken));
    doNothing().when(refreshTokenRepo).delete(existingToken);
    doNothing().when(refreshTokenRepo).flush();
    when(refreshTokenRepo.save(any(RefreshToken.class))).thenReturn(testRefreshToken);

    // When
    RefreshToken result = tokenService.generateRefreshToken(testUser);

    // Then
    assertNotNull(result);
    assertEquals(testRefreshToken, result);

    verify(refreshTokenRepo, times(2))
        .findByUser(testUser); // Called twice: once in generateRefreshToken, once in
    // deleteRefreshTokenByUser
    verify(refreshTokenRepo, times(1)).delete(existingToken);
    verify(refreshTokenRepo, times(1)).flush();
    verify(refreshTokenRepo, times(1)).save(any(RefreshToken.class));
  }

  @Test
  void refreshJwt_WithValidToken_ShouldReturnNewLoginResponse() {
    // Given
    String refreshTokenValue = "valid-refresh-token";
    String newJwtToken = "new-jwt-token";
    String newRefreshTokenValue = "new-refresh-token";

    RefreshToken validRefreshToken =
        RefreshToken.builder()
            .refreshToken(refreshTokenValue)
            .expiration(Instant.now().plusSeconds(3600))
            .user(testUser)
            .build();

    RefreshToken newRefreshToken =
        RefreshToken.builder()
            .refreshToken(newRefreshTokenValue)
            .expiration(Instant.now().plusSeconds(60 * 24 * 60))
            .user(testUser)
            .build();

    Jwt mockJwt = mock(Jwt.class);
    when(mockJwt.getTokenValue()).thenReturn(newJwtToken);
    when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(mockJwt);

    when(refreshTokenRepo.findByRefreshToken(refreshTokenValue))
        .thenReturn(Optional.of(validRefreshToken));
    // Mock the findByUser calls (first for deleteRefreshTokenByUser, second for
    // generateRefreshToken)
    when(refreshTokenRepo.findByUser(testUser))
        .thenReturn(Optional.of(validRefreshToken))
        .thenReturn(Optional.empty());
    doNothing().when(refreshTokenRepo).delete(validRefreshToken);
    doNothing().when(refreshTokenRepo).flush();
    when(refreshTokenRepo.save(any(RefreshToken.class))).thenReturn(newRefreshToken);

    // When
    LoginResponse result = tokenService.refreshJwt(refreshTokenValue);

    // Then
    assertNotNull(result);
    assertEquals(newJwtToken, result.token());
    assertEquals(newRefreshTokenValue, result.refresh());

    verify(refreshTokenRepo, times(1)).findByRefreshToken(refreshTokenValue);
    verify(refreshTokenRepo, times(2))
        .findByUser(testUser); // Called in deleteRefreshTokenByUser and generateRefreshToken
    verify(refreshTokenRepo, times(1)).delete(validRefreshToken);
    verify(refreshTokenRepo, times(1)).flush();
    verify(refreshTokenRepo, times(1)).save(any(RefreshToken.class));
    verify(jwtEncoder, times(1)).encode(any(JwtEncoderParameters.class));
  }

  @Test
  void refreshJwt_WithInvalidToken_ShouldThrowRevalidateTokenException() {
    // Given
    String invalidToken = "invalid-token";
    when(refreshTokenRepo.findByRefreshToken(invalidToken)).thenReturn(Optional.empty());

    // When & Then
    assertThrows(RevalidateTokenException.class, () -> tokenService.refreshJwt(invalidToken));

    verify(refreshTokenRepo, times(1)).findByRefreshToken(invalidToken);
    verify(refreshTokenRepo, never()).delete(any(RefreshToken.class));
    verify(jwtEncoder, never()).encode(any(JwtEncoderParameters.class));
  }

  @Test
  void refreshJwt_WithExpiredToken_ShouldThrowRevalidateTokenException() {
    // Given
    String expiredTokenValue = "expired-token";
    RefreshToken expiredToken =
        RefreshToken.builder()
            .refreshToken(expiredTokenValue)
            .expiration(Instant.now().minusSeconds(3600)) // Expired 1 hour ago
            .user(testUser)
            .build();

    when(refreshTokenRepo.findByRefreshToken(expiredTokenValue))
        .thenReturn(Optional.of(expiredToken));
    when(refreshTokenRepo.findByUser(testUser)).thenReturn(Optional.of(expiredToken));
    doNothing().when(refreshTokenRepo).delete(expiredToken);

    // When & Then
    assertThrows(RevalidateTokenException.class, () -> tokenService.refreshJwt(expiredTokenValue));

    verify(refreshTokenRepo, times(1)).findByRefreshToken(expiredTokenValue);
    verify(refreshTokenRepo, times(1)).findByUser(testUser);
    verify(refreshTokenRepo, times(1)).delete(expiredToken);
    verify(jwtEncoder, never()).encode(any(JwtEncoderParameters.class));
  }

  @Test
  void deleteRefreshTokenByUser_WithExistingToken_ShouldDeleteToken() {
    // Given
    when(refreshTokenRepo.findByUser(testUser)).thenReturn(Optional.of(testRefreshToken));
    doNothing().when(refreshTokenRepo).delete(testRefreshToken);

    // When
    tokenService.deleteRefreshTokenByUser(testUser);

    // Then
    verify(refreshTokenRepo, times(1)).findByUser(testUser);
    verify(refreshTokenRepo, times(1)).delete(testRefreshToken);
  }

  @Test
  void deleteRefreshTokenByUser_WithNoExistingToken_ShouldDoNothing() {
    // Given
    when(refreshTokenRepo.findByUser(testUser)).thenReturn(Optional.empty());

    // When
    tokenService.deleteRefreshTokenByUser(testUser);

    // Then
    verify(refreshTokenRepo, times(1)).findByUser(testUser);
    verify(refreshTokenRepo, never()).delete(any(RefreshToken.class));
  }

  @Test
  void extractUserEmailFromToken_WithValidToken_ShouldReturnEmail() {
    // Given
    String bearerToken = "Bearer valid-jwt-token";
    String expectedEmail = "john.doe@example.com";

    when(testJwt.getSubject()).thenReturn(expectedEmail);
    when(jwtDecoder.decode("valid-jwt-token")).thenReturn(testJwt);

    // When
    String result = tokenService.extractUserEmailFromToken(bearerToken);

    // Then
    assertNotNull(result);
    assertEquals(expectedEmail, result);

    verify(jwtDecoder, times(1)).decode("valid-jwt-token");
    verify(testJwt, times(1)).getSubject();
  }

  @Test
  void extractUserEmailFromToken_WithInvalidFormat_ShouldThrowException() {
    // Given
    String invalidToken = "invalid-format-token";

    // When & Then
    assertThrows(
        ArrayIndexOutOfBoundsException.class,
        () -> tokenService.extractUserEmailFromToken(invalidToken));

    verify(jwtDecoder, never()).decode(anyString());
  }

  @Test
  void extractUserEmailFromToken_WithExpiredJwt_ShouldPropagateException() {
    // Given
    String bearerToken = "Bearer expired-jwt-token";
    when(jwtDecoder.decode("expired-jwt-token")).thenThrow(new JwtException("JWT expired"));

    // When & Then
    assertThrows(JwtException.class, () -> tokenService.extractUserEmailFromToken(bearerToken));

    verify(jwtDecoder, times(1)).decode("expired-jwt-token");
  }

  @Test
  void generateRefreshToken_ShouldCreateTokenWithCorrectExpiration() {
    // Given
    when(refreshTokenRepo.findByUser(testUser)).thenReturn(Optional.empty());
    when(refreshTokenRepo.save(any(RefreshToken.class)))
        .thenAnswer(
            invocation -> {
              RefreshToken token = invocation.getArgument(0);
              // Verify the token has correct properties
              assertNotNull(token.getRefreshToken());
              assertNotNull(token.getExpiration());
              assertEquals(testUser, token.getUser());
              // Verify expiration is approximately 1 day from now (allowing 1 minute tolerance)
              long expectedExpiration = Instant.now().plusSeconds(60 * 24 * 60).getEpochSecond();
              long actualExpiration = token.getExpiration().getEpochSecond();
              assertTrue(Math.abs(expectedExpiration - actualExpiration) < 60);
              return token;
            });

    // When
    tokenService.generateRefreshToken(testUser);

    // Then
    verify(refreshTokenRepo, times(1)).save(any(RefreshToken.class));
  }

  @Test
  void generateJwt_ShouldSetCorrectExpirationTime() {
    // Given
    when(jwtEncoder.encode(any(JwtEncoderParameters.class)))
        .thenAnswer(
            invocation -> {
              JwtEncoderParameters params = invocation.getArgument(0);
              JwtClaimsSet claims = params.getClaims();

              // Verify expiration is approximately 15 minutes from now (allowing 1 minute
              // tolerance)
              Instant expectedExpiration = Instant.now().plusSeconds(60 * 15);
              Instant actualExpiration = claims.getExpiresAt();
              assertTrue(
                  Math.abs(expectedExpiration.getEpochSecond() - actualExpiration.getEpochSecond())
                      < 60);

              Jwt mockJwt = mock(Jwt.class);
              when(mockJwt.getTokenValue()).thenReturn("test-token");
              return mockJwt;
            });

    // When
    tokenService.generateJwt(testUser);

    // Then
    verify(jwtEncoder, times(1)).encode(any(JwtEncoderParameters.class));
  }
}
