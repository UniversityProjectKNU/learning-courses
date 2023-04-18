package com.nazar.grynko.learningcourses.security;

import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${security.token.prefix}")
    private String TOKEN_PREFIX;
    @Value("${security.authorization.header}")
    private String AUTHORIZATION_HEADER;

    private final JwtProvider jwtProvider;
    private final JwtUserDetailsService jwtUserDetailsService;


    public JwtAuthenticationFilter(JwtProvider jwtProvider, JwtUserDetailsService jwtUserDetailsService) {
        this.jwtProvider = jwtProvider;
        this.jwtUserDetailsService = jwtUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        var header = request.getHeader(AUTHORIZATION_HEADER);

        if (header != null && header.startsWith(TOKEN_PREFIX)) {
            var jwtToken = header.replace(TOKEN_PREFIX, "").trim();

            try {
                var login = jwtProvider.extractLogin(jwtToken);
                var userDetails = jwtUserDetailsService.loadUserByUsername(login);

                if (!jwtProvider.validateToken(jwtToken, userDetails)) {
                    throw new JwtException(String.format("Token %s is invalid", jwtToken));
                }

                var authentication = jwtProvider.getAuthenticationToken(jwtToken, userDetails);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (JwtException e) {
                throw new IllegalStateException(String.format("Token %s cannot be trusted", jwtToken));
            }
        }

        filterChain.doFilter(request, response);
    }
}
