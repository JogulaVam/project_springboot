package com.example.demo.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Entity.CartItem;

public interface CartItemRepo extends JpaRepository<CartItem, Long> {
	
	// Method to find all cart items by user ID
	List<CartItem> findByUserId(Long userId);
	
	// Method to delete a cart item by user ID and product ID
	void deleteByUserIdAndProductId(Long userId, Long productId);
	
	// Method to find a cart item by user ID and product ID
	Optional<CartItem> findByUserIdAndProductId(Long userId, Long productId);

}
