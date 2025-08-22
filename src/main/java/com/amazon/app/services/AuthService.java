package com.amazon.app.services;

import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.amazon.app.Dto.RegisterInput;
import com.amazon.app.model.Role;
import com.amazon.app.model.RoleName;
import com.amazon.app.model.User;
import com.amazon.app.repository.RoleRepository;
import com.amazon.app.repository.UserRepository;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository ur, RoleRepository rr, PasswordEncoder pe) {
        this.userRepository = ur;
        this.roleRepository = rr;
        this.passwordEncoder = pe;
    }

    public User registerCustomer(RegisterInput input) {
        if (userRepository.existsByUsername(input.getUsername()))
            throw new RuntimeException("Username already taken");
        if (userRepository.existsByEmail(input.getEmail()))
            throw new RuntimeException("Email already in use");

        Role customerRole = roleRepository.findByName(RoleName.ROLE_CUSTOMER)
            .orElseThrow(() -> new RuntimeException("Customer role not configured"));

        User u = User.builder()
                     .username(input.getUsername())
                     .email(input.getEmail())
                     .password(passwordEncoder.encode(input.getPassword()))
                     .roles(Set.of(customerRole))
                     .build();

        return userRepository.save(u);
    }
}

