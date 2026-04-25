package com.gary.fraudflagger.config;

import com.gary.fraudflagger.model.User;
import com.gary.fraudflagger.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
public class DataInitializer {

    //CommandLineRuner runs once, right after the app starts
    //Spring passes in whatever beans you declare as parameters.
    @Bean
    public CommandLineRunner seedUsers(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, BCryptPasswordEncoder passwordEncoder) {
        return args -> {
            //only seed if the table is empty - prevents duplicates on restart
            if (userRepository.count() == 0) {

                User analyst = new User();
                analyst.setUsername("analyst1");
                analyst.setPassword(passwordEncoder.encode("password"));
                analyst.setRole("ROLE_ANALYST"); //Spring convention internally looks for ROLE_
                userRepository.save(analyst);

                User manager = new User();
                manager.setUsername("manager1");
                manager.setPassword(passwordEncoder.encode("password"));
                manager.setRole("ROLE_MANAGER");
                userRepository.save(manager);

                User admin = new User();
                admin.setUsername("admin1");
                admin.setPassword(passwordEncoder.encode("password"));
                admin.setRole("ROLE_ADMIN");
                userRepository.save(admin);

                System.out.println(">>> Seeded 3 test users: analyst1, manager1, admin1 (password: password)");
            }
        };
    }
}
