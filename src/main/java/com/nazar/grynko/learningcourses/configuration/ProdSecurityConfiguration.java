package com.nazar.grynko.learningcourses.configuration;

import com.nazar.grynko.learningcourses.security.JwtRequestTokenVerifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@Order(1)
@EnableWebSecurity
@Profile("prod")
public class ProdSecurityConfiguration {

    private final JwtRequestTokenVerifier jwtRequestTokenVerifier;

    public ProdSecurityConfiguration(JwtRequestTokenVerifier jwtRequestTokenVerifier) {
        this.jwtRequestTokenVerifier = jwtRequestTokenVerifier;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(permittedEndpoints()).permitAll()
                .antMatchers(authorizedEndpoints()).authenticated()
                .and()
                .addFilterBefore(jwtRequestTokenVerifier, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    //TODO add /learning-courses/api/v1/ to all endpoints
    private String[] permittedEndpoints() {
        return new String[]{
                "/sign-in",
                "/sign-up"
        };
    }

    //TODO add /learning-courses/api/v1/ to all endpoints
    private String[] authorizedEndpoints() {
        return new String[]{
                "/courses-templates/*",
                "/chapters-templates/*",
                "/lessons-templates/*",
                "/courses/*",
                "/chapters/*",
                "/lessons/*",
        };
    }

}
