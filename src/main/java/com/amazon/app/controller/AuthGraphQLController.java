package com.amazon.app.controller;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import com.amazon.app.Dto.AuthPayLoad;
import com.amazon.app.Dto.LoginInput;
import com.amazon.app.Dto.RegisterInput;
import com.amazon.app.model.User;
import com.amazon.app.repository.UserRepository;
import com.amazon.app.security.JwtUtils;
import com.amazon.app.services.AuthService;

@Controller
public class AuthGraphQLController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    public AuthGraphQLController(AuthService authService,
                                 AuthenticationManager authenticationManager,
                                 JwtUtils jwtUtils,
                                 UserRepository userRepository) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
    }

    @MutationMapping
    public AuthPayLoad register(@Argument RegisterInput input) {
        User saved = authService.registerCustomer(input);

        // authenticate to produce JWT
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(input.getUsername(), input.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtils.generateToken(authentication);

        return new AuthPayLoad(token, saved);
    }

    @MutationMapping
    public AuthPayLoad login(@Argument LoginInput input) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(input.getUsername(), input.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtils.generateToken(authentication);
        User u = userRepository.findByUsername(input.getUsername()).orElseThrow();
        return new AuthPayLoad(token, u);
    }
    
    @QueryMapping
    public String hello() {
        return "GraphQL is alive!";
    }
    
//    @MutationMapping
//    @PreAuthorize("hasRole('ADMIN')")
//    public Product addProduct(@Argument ProductInput input) { ... }

}

