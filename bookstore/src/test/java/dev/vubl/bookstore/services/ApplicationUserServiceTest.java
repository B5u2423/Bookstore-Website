package dev.vubl.bookstore.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import dev.vubl.bookstore.entities.ApplicationUser;
import dev.vubl.bookstore.entities.UserType;
import dev.vubl.bookstore.exceptions.UnableToRegisterApplicationUserException;
import dev.vubl.bookstore.exceptions.UserDoesNotExistException;
import dev.vubl.bookstore.repos.ApplicationUserRepo;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

@ExtendWith(MockitoExtension.class)
class ApplicationUserServiceTest {

  @Mock private ApplicationUserRepo userRepo;

  @InjectMocks private ApplicationUserService applicationUserService;

  private ApplicationUser testUser;
  private ApplicationUser testAdmin;

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
  }

  @Test
  void loadUserByUsername_WithValidUsername_ShouldReturnUserDetails() {
    // Given
    when(userRepo.findByEmail("john.doe@example.com")).thenReturn(Optional.of(testUser));

    // When
    UserDetails result = applicationUserService.loadUserByUsername("john.doe@example.com");

    // Then
    assertNotNull(result);
    assertEquals("john.doe@example.com", result.getUsername());
    assertEquals("encodedPassword", result.getPassword());
    assertTrue(
        result.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("CUSTOMER")));
    assertTrue(result.isEnabled());
    assertTrue(result.isAccountNonExpired());
    assertTrue(result.isAccountNonLocked());
    assertTrue(result.isCredentialsNonExpired());

    verify(userRepo, times(1)).findByEmail("john.doe@example.com");
  }

  @Test
  void loadUserByUsername_WithAdminUser_ShouldReturnUserDetailsWithAdminAuthority() {
    // Given
    when(userRepo.findByEmail("admin@company.com")).thenReturn(Optional.of(testAdmin));

    // When
    UserDetails result = applicationUserService.loadUserByUsername("admin@company.com");

    // Then
    assertNotNull(result);
    assertEquals("admin@company.com", result.getUsername());
    assertEquals("adminPassword", result.getPassword());
    assertTrue(
        result.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ADMIN")));

    verify(userRepo, times(1)).findByEmail("admin@company.com");
  }

  @Test
  void loadUserByUsername_WithInvalidUsername_ShouldThrowUsernameNotFoundException() {
    // Given
    String invalidEmail = "nonexistent@example.com";
    when(userRepo.findByEmail(invalidEmail)).thenReturn(Optional.empty());

    // When & Then
    Exception exception =
        assertThrows(
            UserDoesNotExistException.class,
            () -> applicationUserService.loadUserByUsername(invalidEmail));

    assertEquals("User does not exist!", exception.getMessage());
    verify(userRepo, times(1)).findByEmail(invalidEmail);
  }

  @Test
  void createOrUpdateUser_WithValidUser_ShouldReturnSavedUser() {
    // Given
    when(userRepo.save(testUser)).thenReturn(testUser);

    // When
    ApplicationUser result = applicationUserService.createOrUpdateUser(testUser);

    // Then
    assertNotNull(result);
    assertEquals(testUser.getEmail(), result.getEmail());
    assertEquals(testUser.getFirstName(), result.getFirstName());
    assertEquals(testUser.getLastName(), result.getLastName());
    assertEquals(testUser.getUserType(), result.getUserType());

    verify(userRepo, times(1)).save(testUser);
  }

  @Test
  void createOrUpdateUser_WithRepositoryException_ShouldThrowUnableToRegisterException() {
    // Given
    when(userRepo.save(testUser)).thenThrow(new RuntimeException("Database error"));

    // When & Then
    assertThrows(
        UnableToRegisterApplicationUserException.class,
        () -> applicationUserService.createOrUpdateUser(testUser));

    verify(userRepo, times(1)).save(testUser);
  }

  @Test
  void createOrUpdateUser_WithExistingUser_ShouldUpdateUser() {
    // Given
    ApplicationUser updatedUser =
        ApplicationUser.builder()
            .userType(UserType.CUSTOMER)
            .firstName("John Updated")
            .lastName("Doe Updated")
            .email("john.doe@example.com")
            .password("newEncodedPassword")
            .build();

    when(userRepo.save(updatedUser)).thenReturn(updatedUser);

    // When
    ApplicationUser result = applicationUserService.createOrUpdateUser(updatedUser);

    // Then
    assertNotNull(result);
    assertEquals("John Updated", result.getFirstName());
    assertEquals("Doe Updated", result.getLastName());
    assertEquals("newEncodedPassword", result.getPassword());

    verify(userRepo, times(1)).save(updatedUser);
  }

  @Test
  void readAllUsers_ShouldReturnListOfUsers() {
    // Given
    List<ApplicationUser> users = Arrays.asList(testUser, testAdmin);
    when(userRepo.findAll()).thenReturn(users);

    // When
    List<ApplicationUser> result = applicationUserService.readAllUsers();

    // Then
    assertNotNull(result);
    assertEquals(2, result.size());
    assertTrue(result.contains(testUser));
    assertTrue(result.contains(testAdmin));

    verify(userRepo, times(1)).findAll();
  }

  @Test
  void readAllUsers_WithEmptyRepository_ShouldReturnEmptyList() {
    // Given
    when(userRepo.findAll()).thenReturn(List.of());

    // When
    List<ApplicationUser> result = applicationUserService.readAllUsers();

    // Then
    assertNotNull(result);
    assertTrue(result.isEmpty());

    verify(userRepo, times(1)).findAll();
  }

  @Test
  void readUserByEmail_WithValidEmail_ShouldReturnUser() {
    // Given
    when(userRepo.findByEmail("john.doe@example.com")).thenReturn(Optional.of(testUser));

    // When
    ApplicationUser result = applicationUserService.readUserByEmail("john.doe@example.com");

    // Then
    assertNotNull(result);
    assertEquals(testUser.getEmail(), result.getEmail());
    assertEquals(testUser.getFirstName(), result.getFirstName());
    assertEquals(testUser.getLastName(), result.getLastName());

    verify(userRepo, times(1)).findByEmail("john.doe@example.com");
  }

  @Test
  void readUserByEmail_WithInvalidEmail_ShouldThrowUserDoesNotExistException() {
    // Given
    String invalidEmail = "nonexistent@example.com";
    when(userRepo.findByEmail(invalidEmail)).thenReturn(Optional.empty());

    // When & Then
    assertThrows(
        UserDoesNotExistException.class,
        () -> applicationUserService.readUserByEmail(invalidEmail));

    verify(userRepo, times(1)).findByEmail(invalidEmail);
  }

  @Test
  void deleteUser_WithValidEmail_ShouldDeleteUser() {
    // Given
    when(userRepo.findByEmail("john.doe@example.com")).thenReturn(Optional.of(testUser));
    doNothing().when(userRepo).delete(testUser);

    // When
    applicationUserService.deleteUser("john.doe@example.com");

    // Then
    verify(userRepo, times(1)).findByEmail("john.doe@example.com");
    verify(userRepo, times(1)).delete(testUser);
  }

  @Test
  void deleteUser_WithInvalidEmail_ShouldThrowUserDoesNotExistException() {
    // Given
    String invalidEmail = "nonexistent@example.com";
    when(userRepo.findByEmail(invalidEmail)).thenReturn(Optional.empty());

    // When & Then
    assertThrows(
        UserDoesNotExistException.class, () -> applicationUserService.deleteUser(invalidEmail));

    verify(userRepo, times(1)).findByEmail(invalidEmail);
    verify(userRepo, never()).delete(any(ApplicationUser.class));
  }

  @Test
  void deleteUser_WithRepositoryException_ShouldPropagateException() {
    // Given
    when(userRepo.findByEmail("john.doe@example.com")).thenReturn(Optional.of(testUser));
    doThrow(new RuntimeException("Database error")).when(userRepo).delete(testUser);

    // When & Then
    assertThrows(
        RuntimeException.class, () -> applicationUserService.deleteUser("john.doe@example.com"));

    verify(userRepo, times(1)).findByEmail("john.doe@example.com");
    verify(userRepo, times(1)).delete(testUser);
  }

  @Test
  void readUserByEmailOrThrowException_WithValidEmail_ShouldReturnUser() {
    // Given
    when(userRepo.findByEmail("john.doe@example.com")).thenReturn(Optional.of(testUser));

    // When
    ApplicationUser result = applicationUserService.readUserByEmail("john.doe@example.com");

    // Then
    assertNotNull(result);
    assertEquals(testUser, result);

    verify(userRepo, times(1)).findByEmail("john.doe@example.com");
  }

  @Test
  void loadUserByUsername_WithNullUsername_ShouldThrowUsernameNotFoundException() {
    // Given
    when(userRepo.findByEmail(null)).thenReturn(Optional.empty());

    // When & Then
    assertThrows(
        UserDoesNotExistException.class, () -> applicationUserService.loadUserByUsername(null));

    verify(userRepo, times(1)).findByEmail(null);
  }

  @Test
  void loadUserByUsername_WithEmptyUsername_ShouldThrowUsernameNotFoundException() {
    // Given
    when(userRepo.findByEmail("")).thenReturn(Optional.empty());

    // When & Then
    assertThrows(
        UserDoesNotExistException.class, () -> applicationUserService.loadUserByUsername(""));

    verify(userRepo, times(1)).findByEmail("");
  }
}
