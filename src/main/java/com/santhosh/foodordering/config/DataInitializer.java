package com.santhosh.foodordering.config;

import com.santhosh.foodordering.model.Role;
import com.santhosh.foodordering.model.Users;
import com.santhosh.foodordering.repo.RoleRepository;
import com.santhosh.foodordering.repo.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Seeds the three roles and a default ADMIN account on startup so the app is usable immediately.
 * Everything is idempotent - it only inserts what is missing.
 */
@Configuration
public class DataInitializer {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    CommandLineRunner initializeApplicationData(RoleRepository roleRepository,
                               UserRepository userRepository,
                               PasswordEncoder passwordEncoder) {
        return args -> {
            Role admin = createRoleIfMissing(roleRepository, "ADMIN", "Full system administration");
            createRoleIfMissing(roleRepository, "RESTAURANT_OWNER", "Manages own restaurants and menus");
            createRoleIfMissing(roleRepository, "CUSTOMER", "Browses restaurants and places orders");

            if (!userRepository.existsByUsername("admin")) {
                Users user = new Users();
                user.setUsername("admin");
                user.setEmail("admin@foodorder.local");
                user.setPassword(passwordEncoder.encode("admin@123"));
                user.setRole(admin);
                userRepository.save(user);
//                log.info("Seeded default ADMIN account -> username: admin / password: admin@123");
                log.info("Default ADMIN account created successfully.");
            }
        };
    }

    private Role createRoleIfMissing(RoleRepository repo, String name, String description) {
        return repo.findByRoleName(name).orElseGet(() -> repo.save(new Role(null, name, description)));
    }
}
