package dev.vubl.bookstore.services;

import dev.vubl.bookstore.dtos.AccountDetailDTO;
import dev.vubl.bookstore.dtos.AddressDTO;
import dev.vubl.bookstore.dtos.ResetPasswordRequest;
import dev.vubl.bookstore.dtos.UpdateProfileRequest;
import dev.vubl.bookstore.entities.ApplicationUser;
import dev.vubl.bookstore.entities.CustomerAddressInfo;
import dev.vubl.bookstore.exceptions.UnableToRegisterApplicationUserException;
import dev.vubl.bookstore.exceptions.UserDoesNotExistException;
import dev.vubl.bookstore.repos.ApplicationUserRepo;
import dev.vubl.bookstore.repos.CustomerAddressInfoRepo;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ApplicationUserService implements UserDetailsService {
  private static final String EMAIL_SUBJECT = "Đặt lại mật khẩu";
  private final String INSTANCE_NAME = this.getClass().getName();

  private final ApplicationUserRepo userRepo;
  private final CustomerAddressInfoRepo customerAddressInfoRepo;
  private final PasswordEncoder passwordEncoder;
  private final EmailService emailService;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    try {
      ApplicationUser user = readUserByEmailOrThrowException(username);
      return User.builder()
          .username(username)
          .password(user.getPassword())
          .authorities(user.getUserType().toString())
          .build();
    } catch (UsernameNotFoundException e) {
      throw new UsernameNotFoundException("User does not exist!");
    }
  }

  public ApplicationUser createOrUpdateUser(ApplicationUser user) {
    try {
      return userRepo.save(user);
    } catch (Exception e) {
      throw new UnableToRegisterApplicationUserException();
    }
  }

  public List<ApplicationUser> readAllUsers() {
    return userRepo.findAll();
  }

  public ApplicationUser readUserByEmail(String email) {
    return readUserByEmailOrThrowException(email);
  }

  public void deleteUser(String email) {
    ApplicationUser user = readUserByEmailOrThrowException(email);
    userRepo.delete(user);
  }

  public String updateUserProfileInfo(ApplicationUser user, UpdateProfileRequest payload) {
    user.setFirstName(payload.firstName());
    user.setLastName(payload.lastName());
    user.setPhoneNumber(payload.phoneNumber());
    user.setEmail(payload.email());

    try {
      this.createOrUpdateUser(user);
      return "Update profile successfully";
    } catch (RuntimeException e) {
      log.error("[{}] Error updating user profile", INSTANCE_NAME);
      throw e;
    }
  }

  public CustomerAddressInfo addAddressInfo(ApplicationUser user, AddressDTO payload) {
    CustomerAddressInfo addressInfo =
        CustomerAddressInfo.builder()
            .city(payload.city())
            .commune(payload.commune())
            .street(payload.street())
            .customer(user)
            .build();
    user.getAddressList().add(addressInfo);

    try {
      userRepo.save(user);
      return addressInfo;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public CustomerAddressInfo updateAddressInfo(Integer addressId, AddressDTO payload) {
    CustomerAddressInfo address = customerAddressInfoRepo.findById(addressId).orElseThrow();
    address.setCity(payload.city());
    address.setCommune(payload.commune());
    address.setStreet(payload.street());
    try {
      return customerAddressInfoRepo.save(address);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public String removeAddressInfo(ApplicationUser user, Integer addressId) {
    if (addressId == null) {
      throw new IllegalArgumentException("Address ID should not be null");
    }
    user.getAddressList().removeIf(item -> item.getId().equals(addressId));
    try {
      userRepo.save(user);
      return "Address with id %d removed".formatted(addressId);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public AccountDetailDTO getAccountDetail(ApplicationUser user) {
    return AccountDetailDTO.builder()
        .email(user.getEmail())
        .phoneNumber(user.getPhoneNumber())
        .firstName(user.getFirstName())
        .lastName(user.getLastName())
        .addressList(
            user.getAddressList().stream()
                .map(
                    item ->
                        AddressDTO.builder()
                            .id(item.getId())
                            .city(item.getCity())
                            .commune(item.getCommune())
                            .street(item.getStreet())
                            .build())
                .toList())
        .build();
  }

  public void resetPassword(ResetPasswordRequest payload) {
    // reset password
    ApplicationUser user =
        userRepo
            .findByEmail(payload.email())
            .orElseThrow(
                () ->
                    new RuntimeException(
                        "[%s] No user with email  %s".formatted(INSTANCE_NAME, payload.email())));
    String pass = UUID.randomUUID().toString().replace("-", "").substring(0, 12);
    user.setPassword(passwordEncoder.encode(pass));
    log.info("[{}] Password generated", INSTANCE_NAME);
    try {
      userRepo.save(user);
      // send email
      emailService.sendEmail(
          payload.email(), EMAIL_SUBJECT, "Mật khẩu mới của bạn là %s".formatted(pass));
      log.info("[{}] Email sent", INSTANCE_NAME);
    } catch (RuntimeException e) {
      log.error("[{}] Error in forget password flow", INSTANCE_NAME);
    }
  }

  private ApplicationUser readUserByEmailOrThrowException(String email) {
    return userRepo.findByEmail(email).orElseThrow(UserDoesNotExistException::new);
  }
}
