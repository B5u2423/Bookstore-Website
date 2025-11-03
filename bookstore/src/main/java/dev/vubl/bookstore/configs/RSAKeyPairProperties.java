package dev.vubl.bookstore.configs;

import dev.vubl.bookstore.utils.RSAKeyPairGeneratorUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Component
@Getter
@Setter
public class RSAKeyPairProperties {
  public RSAPublicKey rsaPublicKey;
  public RSAPrivateKey rsaPrivateKey;

  public RSAKeyPairProperties () {
    KeyPair keyPair = RSAKeyPairGeneratorUtil.generateRSAKeyPair();
    this.rsaPublicKey = ((RSAPublicKey) keyPair.getPublic());
    this.rsaPrivateKey = ((RSAPrivateKey) keyPair.getPrivate());
  }
}
