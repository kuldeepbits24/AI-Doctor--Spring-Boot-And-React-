package com.jdoc.hospital.config;

import com.jdoc.hospital.model.AppUser;
import com.jdoc.hospital.model.Role;
import com.jdoc.hospital.repo.AppUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {

  @Bean
  CommandLineRunner initAdmin(AppUserRepository userRepo, PasswordEncoder encoder) {
    return args -> {
      if (!userRepo.existsByUsername("admin")) {
        AppUser admin = new AppUser("admin", encoder.encode("admin123"), "Admin", "User", null, Role.ADMIN);
        userRepo.save(admin);
      }
    };
  }
}
