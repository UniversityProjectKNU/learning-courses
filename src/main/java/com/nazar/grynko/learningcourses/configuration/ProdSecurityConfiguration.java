package com.nazar.grynko.learningcourses.configuration;

import com.nazar.grynko.learningcourses.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@Order(1)
@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true)
@Profile("prod")
public class ProdSecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public ProdSecurityConfiguration(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
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
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    private String[] permittedEndpoints() {
        return new String[]{
                "/learning-courses/api/v1/sign-in",
                "/learning-courses/api/v1/sign-up"
        };
    }

    private String[] authorizedEndpoints() {
        return new String[]{
                "/learning-courses/api/v1/courses-templates/**",
                "/learning-courses/api/v1/courses/**",
                "/learning-courses/api/v1/users/**",
        };
    }

}
