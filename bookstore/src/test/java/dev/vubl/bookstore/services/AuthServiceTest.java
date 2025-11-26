package dev.vubl.bookstore.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import dev.vubl.bookstore.dtos.LoginRequest;
import dev.vubl.bookstore.dtos.LoginResponse;
import dev.vubl.bookstore.dtos.RegistrationRequest;
import dev.vubl.bookstore.dtos.RegistrationResponse;
import dev.vubl.bookstore.entities.ApplicationUser;
import dev.vubl.bookstore.entities.RefreshToken;
import dev.vubl.bookstore.entities.UserType;
import dev.vubl.bookstore.exceptions.InvalidCredentialException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

  @Mock private ApplicationUserService userService;

  @Mock private PasswordEncoder passwordEncoder;

  @Mock private AuthenticationManager authenticationManager;

  @Mock private TokenService tokenService;

  @Mock private HttpServletRequest httpServletRequest;

  @Mock private HttpServletResponse httpServletResponse;

  @InjectMocks private AuthService authService;

  private ApplicationUser testUser;
  private ApplicationUser testAdmin;
  private RegistrationRequest registrationRequest;
  private LoginRequest loginRequest;
  private RefreshToken testRefreshToken;

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

    testAdmin =
        ApplicationUser.builder()
            .userType(UserType.ADMIN)
            .firstName("Admin")
            .lastName("User")
            .email("admin@company.com")
            .password("adminPassword")
            .build();

    registrationRequest =
        RegistrationRequest.builder()
            .userType(UserType.CUSTOMER)
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@example.com")
            .password("plainPassword")
            .build();

    loginRequest =
        LoginRequest.builder().email("john.doe@example.com").password("plainPassword").build();

    testRefreshToken =
        RefreshToken.builder()
            .refreshToken("test-refresh-token")
            .expiration(Instant.now().plusSeconds(60 * 24 * 60))
            .user(testUser)
            .build();
  }

  @Test
  void registerUser_WithValidRequest_ShouldReturnRegistrationResponse() {
    // Given
    String encodedPassword = "encodedPassword123";
    when(passwordEncoder.encode("plainPassword")).thenReturn(encodedPassword);
    when(userService.createOrUpdateUser(any(ApplicationUser.class))).thenReturn(testUser);

    // When
    RegistrationResponse result = authService.registerUser(registrationRequest);

    // Then
    assertNotNull(result);
    assertEquals(testUser, result.createdUser());

    verify(passwordEncoder, times(1)).encode("plainPassword");
    verify(userService, times(1))
        .createOrUpdateUser(
            argThat(
                user ->
                    user.getUserType() == UserType.CUSTOMER
                        && user.getFirstName().equals("John")
                        && user.getLastName().equals("Doe")
                        && user.getEmail().equals("john.doe@example.com")
                        && user.getPassword().equals(encodedPassword)));
  }

  @Test
  void registerUser_WithAdminType_ShouldCreateAdminUser() {
    // Given
    RegistrationRequest adminRequest =
        RegistrationRequest.builder()
            .userType(UserType.ADMIN)
            .firstName("Admin")
            .lastName("User")
            .email("admin@company.com")
            .password("adminPass")
            .build();

    String encodedPassword = "encodedAdminPass";
    when(passwordEncoder.encode("adminPass")).thenReturn(encodedPassword);
    when(userService.createOrUpdateUser(any(ApplicationUser.class))).thenReturn(testAdmin);

    // When
    RegistrationResponse result = authService.registerUser(adminRequest);

    // Then
    assertNotNull(result);
    assertEquals(testAdmin, result.createdUser());

    verify(passwordEncoder, times(1)).encode("adminPass");
    verify(userService, times(1))
        .createOrUpdateUser(
            argThat(
                user ->
                    user.getUserType() == UserType.ADMIN
                        && user.getEmail().equals("admin@company.com")));
  }

  @Test
  void logInUser_WithValidCredentials_ShouldReturnLoginResponse() {
    // Given
    Authentication mockAuthentication = mock(Authentication.class);
    String jwtToken = "jwt-token";

    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenReturn(mockAuthentication);
    when(userService.readUserByEmail("john.doe@example.com")).thenReturn(testUser);
    when(tokenService.generateJwt(testUser)).thenReturn(jwtToken);
    when(tokenService.generateRefreshToken(testUser)).thenReturn(testRefreshToken);

    // When
    LoginResponse result =
        authService.logInUser(loginRequest, httpServletRequest, httpServletResponse);

    // Then
    assertNotNull(result);
    assertEquals(jwtToken, result.token());
    assertEquals("test-refresh-token", result.refresh());

    verify(authenticationManager, times(1))
        .authenticate(any(UsernamePasswordAuthenticationToken.class));
    verify(userService, times(1)).readUserByEmail("john.doe@example.com");
    verify(tokenService, times(1)).generateJwt(testUser);
    verify(tokenService, times(1)).generateRefreshToken(testUser);
  }

  @Test
  void logInUser_WithInvalidCredentials_ShouldThrowInvalidCredentialException() {
    // Given
    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenThrow(new BadCredentialsException("Bad credentials"));

    // When & Then
    assertThrows(
        InvalidCredentialException.class,
        () -> authService.logInUser(loginRequest, httpServletRequest, httpServletResponse));

    verify(authenticationManager, times(1))
        .authenticate(any(UsernamePasswordAuthenticationToken.class));
    verify(userService, never()).readUserByEmail(anyString());
    verify(tokenService, never()).generateJwt(any(ApplicationUser.class));
    verify(tokenService, never()).generateRefreshToken(any(ApplicationUser.class));
  }

  @Test
  void logInUser_WithAuthenticationException_ShouldThrowInvalidCredentialException() {
    // Given
    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenThrow(new RuntimeException("Authentication failed"));

    // When & Then
    assertThrows(
        InvalidCredentialException.class,
        () -> authService.logInUser(loginRequest, httpServletRequest, httpServletResponse));

    verify(authenticationManager, times(1))
        .authenticate(any(UsernamePasswordAuthenticationToken.class));
  }

  @Test
  void logOutUser_WithValidJwt_ShouldDeleteRefreshToken() {
    // Given
    String bearerToken = "Bearer jwt-token";
    String userEmail = "john.doe@example.com";

    when(tokenService.extractUserEmailFromToken(bearerToken)).thenReturn(userEmail);
    when(userService.readUserByEmail(userEmail)).thenReturn(testUser);
    doNothing().when(tokenService).deleteRefreshTokenByUser(testUser);

    // When
    authService.logOutUser(bearerToken);

    // Then
    verify(tokenService, times(1)).extractUserEmailFromToken(bearerToken);
    verify(userService, times(1)).readUserByEmail(userEmail);
    verify(tokenService, times(1)).deleteRefreshTokenByUser(testUser);
  }

  @Test
  void logOutUser_WithInvalidJwt_ShouldPropagateException() {
    // Given
    String invalidToken = "Bearer invalid-jwt";
    when(tokenService.extractUserEmailFromToken(invalidToken))
        .thenThrow(new RuntimeException("Invalid token"));

    // When & Then
    assertThrows(RuntimeException.class, () -> authService.logOutUser(invalidToken));

    verify(tokenService, times(1)).extractUserEmailFromToken(invalidToken);
    verify(userService, never()).readUserByEmail(anyString());
    verify(tokenService, never()).deleteRefreshTokenByUser(any(ApplicationUser.class));
  }

  @Test
  void readUserFromToken_WithInvalidToken_ShouldPropagateException() {
    // Given
    String invalidToken = "Bearer expired-jwt";
    when(tokenService.extractUserEmailFromToken(invalidToken))
        .thenThrow(new RuntimeException("Token expired"));

    // When & Then
    assertThrows(RuntimeException.class, () -> authService.readUserFromToken(invalidToken));

    verify(tokenService, times(1)).extractUserEmailFromToken(invalidToken);
    verify(userService, never()).readUserByEmail(anyString());
  }

  @Test
  void registerUser_WithNullRequest_ShouldThrowException() {
    // When & Then
    assertThrows(NullPointerException.class, () -> authService.registerUser(null));

    verify(passwordEncoder, never()).encode(anyString());
    verify(userService, never()).createOrUpdateUser(any(ApplicationUser.class));
  }

  @Test
  void logInUser_WithNullRequest_ShouldThrowException() {
    // When & Then
    assertThrows(
        InvalidCredentialException.class,
        () -> authService.logInUser(null, httpServletRequest, httpServletResponse));

    verify(authenticationManager, never())
        .authenticate(any(UsernamePasswordAuthenticationToken.class));
  }

  @Test
  void logInUser_ShouldCreateCorrectAuthenticationToken() {
    // Given
    Authentication mockAuthentication = mock(Authentication.class);

    when(authenticationManager.authenticate(
            argThat(
                token -> {
                  UsernamePasswordAuthenticationToken authToken =
                      (UsernamePasswordAuthenticationToken) token;
                  return authToken.getPrincipal().equals("john.doe@example.com")
                      && authToken.getCredentials().equals("plainPassword")
                      && !authToken.isAuthenticated();
                })))
        .thenReturn(mockAuthentication);

    when(userService.readUserByEmail("john.doe@example.com")).thenReturn(testUser);
    when(tokenService.generateJwt(testUser)).thenReturn("jwt-token");
    when(tokenService.generateRefreshToken(testUser)).thenReturn(testRefreshToken);

    // When
    LoginResponse result =
        authService.logInUser(loginRequest, httpServletRequest, httpServletResponse);

    // Then
    assertNotNull(result);
    verify(authenticationManager, times(1))
        .authenticate(any(UsernamePasswordAuthenticationToken.class));
  }

  @Test
  void registerUser_WithDifferentUserTypes_ShouldHandleAllTypes() {
    // Test STAFF user type
    RegistrationRequest staffRequest =
        RegistrationRequest.builder()
            .userType(UserType.STAFF)
            .firstName("Staff")
            .lastName("Member")
            .email("staff@company.com")
            .password("staffPass")
            .build();

    ApplicationUser staffUser =
        ApplicationUser.builder()
            .userType(UserType.STAFF)
            .firstName("Staff")
            .lastName("Member")
            .email("staff@company.com")
            .password("encodedStaffPass")
            .build();

    when(passwordEncoder.encode("staffPass")).thenReturn("encodedStaffPass");
    when(userService.createOrUpdateUser(any(ApplicationUser.class))).thenReturn(staffUser);

    // When
    RegistrationResponse result = authService.registerUser(staffRequest);

    // Then
    assertNotNull(result);
    assertEquals(staffUser, result.createdUser());
    verify(userService, times(1))
        .createOrUpdateUser(argThat(user -> user.getUserType() == UserType.STAFF));
  }
}
