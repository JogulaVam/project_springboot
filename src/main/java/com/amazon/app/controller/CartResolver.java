package com.amazon.app.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import com.amazon.app.model.Cart;
import com.amazon.app.model.CartItem;
import com.amazon.app.model.Order;
import com.amazon.app.model.OrderItem;
import com.amazon.app.model.Product;
import com.amazon.app.model.User;
import com.amazon.app.repository.*;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CartResolver {
	
	private final CartRepository cartRepository;
	private final ProductRepository productRepository;
	private final UserRepository userRepository;
	private final OrderRepository orderRepository;
	
	@QueryMapping
	@PreAuthorize("hasRole('CUSTOMER')")
	public Cart myCart(Authentication authentication) {
		User user = userRepository.findByUsername(authentication.getName()).orElseThrow();
		
		return cartRepository.findByUser(user).orElseGet(()->{
			Cart cart = new Cart();
			cart.setUser(user);
			return cartRepository.save(cart);
		});
	}
	
	@MutationMapping
	@PreAuthorize("hasRole('CUSTOMER')")
	public Cart addToCart(@Argument Long productId, @Argument int quantity, Authentication authentication) {
		User user = userRepository.findByUsername(authentication.getName()).orElseThrow(() -> new EntityNotFoundException("User not found"));
		Cart cart = cartRepository.findByUser(user).orElseGet(()->{
			Cart newCart = new Cart();
			newCart.setUser(user);
			return cartRepository.save(newCart);
		});		
		Product product = productRepository.findById(productId).orElseThrow(() -> new EntityNotFoundException("Product not found"));;
		
		CartItem existingItem = cart.getItems().stream()
				.filter(i -> i.getProduct().getId() == productId)
				.findFirst().orElse(null);
		
		if(existingItem != null) {
			existingItem.setQuantity(quantity);
		}
		else {
			CartItem item = new CartItem();
			item.setCart(cart);
			item.setProduct(product);
			item.setQuantity(quantity);
			cart.getItems().add(item);
		}
		
		return cartRepository.save(cart);
	}
	
	@MutationMapping
	@PreAuthorize("hasRole('CUSTOMER')")
	public Cart removeFromCart(@Argument Long productId,Authentication authentication) {
		User user = userRepository.findByUsername(authentication.getName()).orElseThrow();
		Cart cart = cartRepository.findByUser(user).orElseThrow();
		cart.getItems().removeIf(item -> item.getProduct().getId() == productId);
		return cartRepository.save(cart);

	}
	
	@MutationMapping
	@PreAuthorize("hasRole('CUSTOMER')")
	public Order checkoutCart(@Argument List<Long> productIds, Authentication authentication) {
		User user = userRepository.findByUsername(authentication.getName()).orElseThrow();
		Cart cart = cartRepository.findByUser(user).orElseThrow();
		
		List<CartItem> selectedItems = cart.getItems().stream()
				.filter(i-> productIds == null || productIds.contains(i.getProduct().getId()))
				.toList();
		
		if(selectedItems.isEmpty()) {
			throw new RuntimeException("No items selected for checkout");
		}
		
		Order order = new Order();
		order.setUser(user);
		order.setOrderDate(LocalDateTime.now());
		
		List<OrderItem> orderItems = new ArrayList<>();
		double total = 0;
		
		for(CartItem item : selectedItems) {
			OrderItem oi = new OrderItem();
			oi.setOrder(order);
			oi.setProductName(item.getProduct().getName());
			oi.setPrice(item.getProduct().getPrice());
			oi.setQuantity(item.getQuantity());
			orderItems.add(oi);
			
			total += item.getProduct().getPrice() * item.getQuantity();
		}
		
		order.setItems(orderItems);
		order.setTotal(total);
		
		cart.getItems().removeIf(i -> selectedItems.contains(i));
		
		cartRepository.save(cart);
		
		return orderRepository.save(order);
	}
	
	
}
