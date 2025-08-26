package com.amazon.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amazon.app.model.Cart;
import com.amazon.app.model.User;

public interface CartRepository extends JpaRepository<Cart,Long>{
	Optional<Cart> findByUser(User user);
}
