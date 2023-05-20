package org.server.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.server.pojo.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtService {
    static final String CLAIM_KEY_USERNAME = "username";
    static final String CLAIM_KEY_PASSWORD = "password";
    static final String CLAIM_KEY_CREATTIME = "created";

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String getUsernameFromToken(String token) {
        String username;
        try {
            final Claims claims = getClaimsFromToken(token);
            username = (String) claims.get(CLAIM_KEY_USERNAME);
        } catch (Exception e) {
            e.printStackTrace();
            username = null;
        }
        return username;
    }


    public Date getCreatedDateFromToken(String token) {
        Date created;
        try {
            final Claims claims = getClaimsFromToken(token);
            created = new Date((Long) claims.get(CLAIM_KEY_CREATTIME));
        } catch (Exception e) {
            e.printStackTrace();
            created = null;
        }
        return created;
    }

    public Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            final Claims claims = getClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            e.printStackTrace();
            expiration = null;
        }
        return expiration;
    }

    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, user.getUsername());
        claims.put(CLAIM_KEY_PASSWORD, user.getPassword());
        claims.put(CLAIM_KEY_CREATTIME, new Date());

        return generateToken(claims);
    }

    private String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
            .compact();
    }

    public String refreshToken(String token) {
        String refreshedToken;
        try {
            final Claims claims = getClaimsFromToken(token);
            claims.put(CLAIM_KEY_CREATTIME, new Date());
            refreshedToken = generateToken(claims);
        } catch (Exception e) {
            e.printStackTrace();
            refreshedToken = null;
        }
        return refreshedToken;
    }

    public Boolean validateToken(String token, User user) {
        final String username = getUsernameFromToken(token);
        final Date created = getCreatedDateFromToken(token);

        //final Date expiration = getExpirationDateFromToken(token);
        return (username.equals(user.getUsername()) && !isTokenExpired(token));
    }

    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            e.printStackTrace();
            claims = null;
        }
        return claims;
    }
}