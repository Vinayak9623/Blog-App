package com.vsd.security;

import com.vsd.configuration.JwtProperties;
import com.vsd.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final JwtProperties jwtProperties;


    private Key getSignKey(){
        return Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }

    public String genrateAccessToken(User user){
        return Jwts.builder()
                .subject(user.getEmail())
                .issuer(jwtProperties.getIssuer())
                .claim("role",user.getRole().name())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+jwtProperties.getAccessTokenExpiration()))
                .signWith(getSignKey())
                .compact();
    }

    public String genrateRefreshToken(User user){
        return Jwts.builder()
                .subject(user.getEmail())
                .issuer(jwtProperties.getIssuer())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+jwtProperties
                        .getRefreshTokenExpiration()))
                .signWith(getSignKey())
                .compact();
    }

    private Claims extractAllClaims(String token){
       return Jwts.parser()
                .verifyWith((SecretKey) getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getUsernameFromToken(String token){
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username=getUsernameFromToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token){
        return extractAllClaims(token).getExpiration().before(new Date());
    }
}
