package com.nazar.grynko.learningcourses.security;

import io.jsonwebtoken.JwtException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestTokenVerifier extends OncePerRequestFilter {

    private final static String BEARER_PREFIX = "Bearer ";
    private final static String AUTHORIZATION_HEADER = "Authorization";

    private final JwtProvider jwtProvider;

    public JwtRequestTokenVerifier(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_PREFIX)) {
            final String jwtToken = authorizationHeader.replace(BEARER_PREFIX, "");

            try {
                String userLogin = jwtProvider.extractUserLogin(jwtToken);
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        userLogin, null, null);

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (JwtException e) {
                throw new IllegalStateException(String.format("Token %s cannot be trusted", jwtToken));
            }
        }
        filterChain.doFilter(request, response);
    }
}

