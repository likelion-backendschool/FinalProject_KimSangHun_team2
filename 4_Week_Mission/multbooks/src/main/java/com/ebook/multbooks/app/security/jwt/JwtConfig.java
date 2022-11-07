package com.ebook.multbooks.app.security.jwt;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.util.Base64;

/*
* Jwt 에 사용되는 시크릿키관련 설정
* */
@Configuration
public class JwtConfig {
    @Value("${custom.jwt.secretKey}")
    private String secretKeyPlain;

    @Bean
    public SecretKey jwtSecretKey(){
        String keyBase64Encoded= Base64.getEncoder().encodeToString(secretKeyPlain.getBytes());
        return Keys.hmacShaKeyFor(keyBase64Encoded.getBytes());
    }
}
