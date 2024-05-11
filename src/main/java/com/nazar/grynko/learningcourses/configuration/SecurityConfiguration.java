package com.nazar.grynko.learningcourses.configuration;

import com.nazar.grynko.learningcourses.exception.filter.FilterChainExceptionHandler;
import com.nazar.grynko.learningcourses.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@Order(1)
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true)
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final FilterChainExceptionHandler filterChainExceptionHandler;

    public SecurityConfiguration(JwtAuthenticationFilter jwtAuthenticationFilter,
                                 FilterChainExceptionHandler filterChainExceptionHandler) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.filterChainExceptionHandler = filterChainExceptionHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(permittedEndpoints()).permitAll()
                        .requestMatchers(authorizedEndpoints()).authenticated()) //todo replace with anyRequest().authenticated()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(filterChainExceptionHandler, LogoutFilter.class)
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
                "/learning-courses/api/v1/self/**",
                "/learning-courses/api/v1/my-courses/**",
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(5);
    }

}
