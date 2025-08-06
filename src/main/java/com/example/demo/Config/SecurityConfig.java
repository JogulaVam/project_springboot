package com.example.demo.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // Disable CSRF for simplicity, consider enabling it in production
            .authorizeHttpRequests()
            .requestMatchers("/*").permitAll() // Allow access to GraphQL and GraphiQL endpoints
            .anyRequest().authenticated() // Require authentication for all other requests
            .and()
            .httpBasic();

        return http.build();
    }

}
