package com.ebook.multbooks.app.security.jwt;

import com.ebook.multbooks.global.util.Util;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class JwtProvider {
    private final SecretKey jwtSecretKey;

    private SecretKey getSecretKey(){
        return jwtSecretKey;
    }

    /*
    *
    * jwt token 생성
    *
    * */
    public String generateAccessToken(Map<String,Object> claims,int seconds){
        long now=new Date().getTime();
        Date accessTokenExpiresIn=new Date(now+1000L*seconds);
        return Jwts.builder()
                .claim("body", Util.json.toStr(claims))
                .setExpiration(accessTokenExpiresIn)
                .signWith(getSecretKey(), SignatureAlgorithm.HS512)
                .compact();
    }
}
