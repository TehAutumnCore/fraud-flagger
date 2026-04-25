package com.gary.fraudflagger.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    //This bean tells Spring Security How to hash passwords/
    // BCrypt is the industry standard - it's slowon purpose, making brute-force attacks expensive
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/h2-console/**").permitAll() //public pages
                        .requestMatchers("/admin/**").hasRole("ADMIN") //admin only
                        .requestMatchers("/flags/**").hasAnyRole("MANAGER", "ADMIN") //day 6
                        .anyRequest().authenticated() //everything else : must be logged in
                )
                .formLogin(form -> form
                        .loginPage("/login") //use OUR login page, not springs default
                        .defaultSuccessUrl("/dashboard", true) //after login, go there
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout") //after logout, go to login with logout in url
                        .permitAll()
                )
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin()) //lests H2 console load in a frame
                );

        return http.build();
    }
}
