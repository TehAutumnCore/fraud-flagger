package com.gary.fraudflagger.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() //allows all routes will change later
                )
                .csrf(csrf -> csrf.disable()); //H2 console uses frames and old style form posts; CSRF protection breaks it

        return http.build();
    }
}
