package com.nazar.grynko.learningcourses.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtProvider {

    @Value("${security.key}")
    private String SECRET_KEY;
    @Value("${security.authorities.key}")
    private String AUTHORITIES_KEY;
    @Value("${security.token.validity}")
    private Integer TOKEN_VALIDITY;
    private static final String ROLE_PREFIX = "ROLE_";

    public String generateToken(UserDetails userDetails) {
        var authorities = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        var claims = new HashMap<String, Object>();
        claims.put(AUTHORITIES_KEY, authorities);

        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY * 1000))
                //.setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(365L)))
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .compact();
    }

    private <T> T getClaim(String token, Function<? super Claims, T> extractor) {
        var claims = getAllClaims(token);
        return extractor.apply(claims);
    }

    private Claims getAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractLogin(String token) {
        return getClaim(token, Claims::getSubject);
    }

    public Date extractExpirationDate(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    public Collection<? extends GrantedAuthority> extractAuthorities(String token) {
        var claims = getAllClaims(token);
        return Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .map(s -> ROLE_PREFIX + s)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    private Boolean isExpired(String token) {
        var expiration = extractExpirationDate(token);
        return expiration.before(new Date());
    }

    //TODO check if roles are the same (maybe we need it after admin changed someone's roles)
    public Boolean validateToken(String token, UserDetails userDetails) {
        var userLogin = extractLogin(token);
        return userLogin.equals(userDetails.getUsername()) && !isExpired(token);
    }

    UsernamePasswordAuthenticationToken getAuthenticationToken(String token, UserDetails userDetails) {
        return new UsernamePasswordAuthenticationToken(userDetails, "", extractAuthorities(token));
    }

}
