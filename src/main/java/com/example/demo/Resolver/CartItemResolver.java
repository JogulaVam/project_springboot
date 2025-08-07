package com.example.demo.Resolver;

import java.util.List;
import java.util.Optional;

import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;

import com.example.demo.Entity.CartItem;
import com.example.demo.Entity.Product;
import com.example.demo.Entity.User;
import com.example.demo.Repository.CartItemRepo;
import com.example.demo.Repository.ProductRepo;
import com.example.demo.Repository.UserRepo;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class CartItemResolver {

	final private CartItemRepo cartItemRepo;
	final private UserRepo userRepo;
	final private ProductRepo productRepo;
	
	@QueryMapping
	public List<CartItem> getCartItemsByUserId(@Argument Long userId) {
		return cartItemRepo.findByUserId(userId);
	}
	
	@QueryMapping
	public CartItem getCartItemByUserIdAndProductId(@Argument Long userId, @Argument Long productId) {
		return cartItemRepo.findByUserIdAndProductId(userId, productId).orElse(null);
	}
	@QueryMapping
	public List<CartItem> getAllCartItems() {
		return cartItemRepo.findAll();
	}
	
	@MutationMapping
	public CartItem addCartItem(@Argument Long userId, @Argument Long productId, @Argument int quantity) {
		User user = userRepo.findById(userId).orElseThrow();
		Product product = productRepo.findById(productId).orElseThrow();
		
		Optional<CartItem> existingCartItem = cartItemRepo.findByUserIdAndProductId(userId, productId);
		
		if(existingCartItem.isPresent()) {
			// If the cart item already exists, update the quantity
			CartItem cartItem = existingCartItem.get();
			cartItem.setQuantity(cartItem.getQuantity() + quantity);
			return cartItemRepo.save(cartItem);
		}
		
		CartItem cartItem = new CartItem();
		cartItem.setUser(user);
		cartItem.setProduct(product);
		cartItem.setQuantity(quantity);
		
		return cartItemRepo.save(cartItem);
	}
	
	@MutationMapping
	public CartItem updateCartItem(@Argument Long userId, @Argument Long productId, @Argument int quantity) {
		CartItem cartItem = cartItemRepo.findByUserIdAndProductId(userId, productId).orElse(null);
		if (cartItem != null) {
			cartItem.setQuantity(quantity);
			return cartItemRepo.save(cartItem);
		}
		return null;
	}
	
	@MutationMapping
	public String deleteCartItem(@Argument Long userId, @Argument Long productId) {
		cartItemRepo.deleteByUserIdAndProductId(userId, productId);
		return "Cart item deleted successfully";
	}
	
	
	
}
