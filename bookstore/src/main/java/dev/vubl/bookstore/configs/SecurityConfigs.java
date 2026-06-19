package dev.vubl.bookstore.configs;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import dev.vubl.bookstore.entities.UserType;
import jakarta.servlet.http.HttpServletResponse;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.RequestCacheConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfigs {
  @Value("${spring.security.oauth2.resourceserver.jwt.private-key-location}")
  private RSAPrivateKey privateKey;

  @Value("${spring.security.oauth2.resourceserver.jwt.public-key-location}")
  private RSAPublicKey publicKey;

  @Value("#{'${app.cors-allowed-origins:}'.split(',')}")
  private List<String> allowedOrigins;

  @Bean
  public PasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public JwtDecoder jwtDecoder() {
    return NimbusJwtDecoder.withPublicKey(publicKey).build();
  }

  @Bean
  public JwtEncoder jwtEncoder() {
    JWK jwk = new RSAKey.Builder(publicKey).privateKey(privateKey).build();
    JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));
    return new NimbusJwtEncoder(jwkSource);
  }

  /**
   * Handle cross-origin
   *
   * @return CorsConfigurationSource object
   */
  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration corsConfiguration = new CorsConfiguration();
    corsConfiguration.setAllowCredentials(true);
    corsConfiguration.setAllowedOrigins(allowedOrigins);
    corsConfiguration.setAllowedMethods(List.of("*"));
    corsConfiguration.setAllowedHeaders(List.of("*"));

    UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource =
        new UrlBasedCorsConfigurationSource();
    urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
    return urlBasedCorsConfigurationSource;
  }

  @Bean
  public AuthenticationManager authenticationManager(UserDetailsService userDetailsService) {
    DaoAuthenticationProvider daoAuthenticationProvider =
        new DaoAuthenticationProvider(userDetailsService);
    daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
    return new ProviderManager(daoAuthenticationProvider);
  }

  @Bean
  @Deprecated
  public JwtAuthenticationConverter jwtAuthenticationConverter() {
    // for role-based jwt. config for @Pre and @Post Authorize annotations.
    // config for oAuth2ResourceServer - oauth2.jwt(converter) in SecFilterChain.
    JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter =
        new JwtGrantedAuthoritiesConverter();
    jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
    jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

    JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
    jwtConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
    return jwtConverter;
  }

  @Bean
  public SecurityFilterChain filterChain(
      HttpSecurity http, AuthenticationSuccessHandler oAuth2SuccessHandler) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .cors(AbstractHttpConfigurer::disable)
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
        .requestCache(RequestCacheConfigurer::disable)
        .authorizeHttpRequests(
            auth -> {
              auth.requestMatchers(adminRoutes()).hasRole(UserType.ADMIN.name());
              auth.requestMatchers(customerRoutes()).hasRole(UserType.CUSTOMER.name());
              auth.requestMatchers(unprotectedRoute()).permitAll();
              auth.anyRequest().authenticated();
            })
        .oauth2Login(oauth -> oauth.successHandler(oAuth2SuccessHandler))
        .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
        .exceptionHandling(
            exception ->
                exception.authenticationEntryPoint(
                    (request, response, authException) ->
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")))
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    return http.build();
  }

  /*
   * Route settings
   */

  private static PathPatternRequestMatcher[] unprotectedRoute() {
    return new PathPatternRequestMatcher[] {
      PathPatternRequestMatcher.withDefaults().matcher("/api/v1/auth/login"),
      PathPatternRequestMatcher.withDefaults().matcher("/api/v1/auth/logout"),
      PathPatternRequestMatcher.withDefaults().matcher("/api/v1/auth/admin/login"),
      PathPatternRequestMatcher.withDefaults().matcher("/api/v1/auth/register"),
      PathPatternRequestMatcher.withDefaults().matcher("/api/v1/auth/refresh"),
      PathPatternRequestMatcher.withDefaults().matcher("/api/v1/auth/reset-password"),
      PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.GET, "/api/v1/books/**"),
      PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.GET, "/api/v1/categories/**"),
      PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.GET, "/api/v1/proxy/**"),
      PathPatternRequestMatcher.withDefaults().matcher("/login/oauth2/**"),
      PathPatternRequestMatcher.withDefaults().matcher("/oauth2/**"),
    };
  }

  private static PathPatternRequestMatcher[] customerRoutes() {
    return new PathPatternRequestMatcher[] {
      PathPatternRequestMatcher.withDefaults().matcher("/api/v1/customers/**"),
      PathPatternRequestMatcher.withDefaults().matcher("/api/v1/carts/**"),
      PathPatternRequestMatcher.withDefaults().matcher("/api/v1/orders/**"),
      PathPatternRequestMatcher.withDefaults().matcher("/api/v1/payment/**"),
      PathPatternRequestMatcher.withDefaults().matcher("/api/v1/coupons/apply"),
      PathPatternRequestMatcher.withDefaults().matcher("/api/v1/orders/user"),
    };
  }

  private static PathPatternRequestMatcher[] adminRoutes() {
    return new PathPatternRequestMatcher[] {
      PathPatternRequestMatcher.withDefaults().matcher("/api/v1/admin/**"),
      PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.POST, "/api/v1/books/**"),
      PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.DELETE, "/api/v1/books/**"),
      PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.PUT, "/api/v1/books/**"),
      PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.POST, "/api/v1/categories/**"),
      PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.DELETE, "/api/v1/categories/**"),
      PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.PUT, "/api/v1/categories/**"),
      PathPatternRequestMatcher.withDefaults().matcher("/api/v1/images/upload"),
      PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.GET, "/api/v1/orders"),
      PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.GET, "/api/v1/coupons/**"),
      PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.POST, "/api/v1/coupons/**"),
      PathPatternRequestMatcher.withDefaults().matcher("/api/v1/collections/**"),
      PathPatternRequestMatcher.withDefaults().matcher("/api/v1/orders/update-status-id"),
    };
  }
}
