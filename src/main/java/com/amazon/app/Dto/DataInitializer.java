package com.amazon.app.Dto;

import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.amazon.app.model.Role;
import com.amazon.app.model.RoleName;
import com.amazon.app.model.User;
import com.amazon.app.repository.*;

@Component
public class DataInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RoleRepository rr, UserRepository ur, PasswordEncoder pe) {
        this.roleRepository = rr; this.userRepository = ur; this.passwordEncoder = pe;
    }

    @Override
    public void run(String... args) {
        if (roleRepository.findByName(RoleName.ROLE_ADMIN).isEmpty())
            roleRepository.save(Role.builder().name(RoleName.ROLE_ADMIN).build());
        if (roleRepository.findByName(RoleName.ROLE_CUSTOMER).isEmpty())
            roleRepository.save(Role.builder().name(RoleName.ROLE_CUSTOMER).build());

        if (!userRepository.existsByUsername("admin")) {
            Role admin = roleRepository.findByName(RoleName.ROLE_ADMIN).get();
            User u = User.builder()
                         .username("admin")
                         .email("admin@example.com")
                         .password(passwordEncoder.encode("admin123"))
                         .roles(Set.of(admin))
                         .build();
            userRepository.save(u);
        }
    }
}

