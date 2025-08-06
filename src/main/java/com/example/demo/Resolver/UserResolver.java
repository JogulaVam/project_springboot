package com.example.demo.Resolver;

import java.util.List;

import org.springframework.graphql.data.method.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

import com.example.demo.Entity.User;
import com.example.demo.Repository.UserRepo;

@Controller
public class UserResolver {
	
	final private UserRepo userRepo;
	final private PasswordEncoder passwordEncoder;
	
	public UserResolver(UserRepo userRepo,PasswordEncoder passwordEncoder) {
		this.userRepo = userRepo;
		this.passwordEncoder = passwordEncoder;
	}
	
	@QueryMapping
	public User getUserById(@Argument Long id) {
		return userRepo.findById(id).orElse(null);
	}
	
	@QueryMapping
	public List<User> getAllUsers() {
		return userRepo.findAll();
	}
	
	@MutationMapping
	public User createUser(@Argument String name, @Argument String email, @Argument String password,@Argument String role) {
		User user = new User();
		user.setName(name);
		user.setEmail(email);
		user.setPassword(passwordEncoder.encode(password));
		user.setRole(role);
		return userRepo.save(user);
	}
	
	@MutationMapping
	public String loginUser(@Argument String email, @Argument String password) {
		return userRepo.findByEmail(email)
			.filter(user -> user.getPassword().equals(passwordEncoder.encode(password)))
			.map(user -> "Login successful for user: " + user.getName())
			.orElse("Invalid email or password");
	}
}
