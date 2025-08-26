package com.amazon.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amazon.app.model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem,Long>{

}
