package dev.vubl.bookstore.utils;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class RSAKeyPairGeneratorUtil {
  public static KeyPair generateRSAKeyPair () {
    KeyPair keyPair;
    try {
      KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
      generator.initialize(2048);
      keyPair = generator.generateKeyPair();
      return keyPair;
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException();
    }
  }
}
