package com.lucas.mastermind.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;


@Configuration
@EnableWebSecurity
public class SecurityConfig{

    private PublicKey getPublicKeyFromString(String key) throws Exception {
        // Remove the first and last lines (the BEGIN/END lines)
        String publicKeyPEM = key.replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s+", "");

        // Decode the Base64 string
        byte[] decoded = Base64.getDecoder().decode(publicKeyPEM);

        // Create a KeyFactory for RSA
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        // Generate the PublicKey
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
        return keyFactory.generatePublic(keySpec);
    }

    @Bean
    public JwtDecoder jwtDecoder() throws Exception {
        String publicKeyKeycloak = "-----BEGIN PUBLIC KEY-----\nMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqixlZkHFwtZVPcHknC0yOawR4jZiVYhvNQoJZNVxUmrpp3JZ4zoQFk34WsViT/u+11o2xvM2KCUca4WHb/RIIz8LYdAJkdV9qJSiz0Drk5EzJHPMLLNp6uVDMiT+idf5XNNjES7E7lLGzp7rRktHurcpch2wBWqWFKNol3e5oSas6Qo4Paj4Wjbhp4GHdWW0on+b8KBKOfdSQ6SXZzeaZYjbmqzTBC2sZhxyJR4DR/FTXH71GR6TGK4kunDq7buguiqeUQeTzNAt1CSKLqFBueprumOHk6OxTsyxH+B0IWXDEq5KYaNgtCBm33Zj2MbndHhJnZ2CmpTIzOOOGytffwIDAQAB\n-----END PUBLIC KEY-----";
        PublicKey publicKey = getPublicKeyFromString(publicKeyKeycloak);
        return NimbusJwtDecoder.withPublicKey((RSAPublicKey) publicKey).build();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/users/save").permitAll()
                        .requestMatchers("/users/*/verify-password").permitAll()
                        .requestMatchers("/users/**").permitAll()
                        .requestMatchers("/users/get/**").authenticated() // Secure GET by user ID
                        .requestMatchers("/users/getAll").authenticated() // Secure GET all users
                        .requestMatchers("/users/delete/**").authenticated()
                        .requestMatchers("/users/update/**").authenticated()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                ).csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        // Optionally set a role converter or other configurations here
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            // Convert JWT claims to granted authorities here if needed
            return new ArrayList<>(); // Modify according to your needs
        });
        return converter;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
