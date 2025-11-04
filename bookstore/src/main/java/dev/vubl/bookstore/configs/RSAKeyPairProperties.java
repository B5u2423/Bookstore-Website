package dev.vubl.bookstore.configs;

import dev.vubl.bookstore.utils.RSAKeyPairGeneratorUtil;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class RSAKeyPairProperties {
  public RSAPublicKey rsaPublicKey;
  public RSAPrivateKey rsaPrivateKey;

  public RSAKeyPairProperties() {
    KeyPair keyPair = RSAKeyPairGeneratorUtil.generateRSAKeyPair();
    this.rsaPublicKey = ((RSAPublicKey) keyPair.getPublic());
    this.rsaPrivateKey = ((RSAPrivateKey) keyPair.getPrivate());
  }
}
