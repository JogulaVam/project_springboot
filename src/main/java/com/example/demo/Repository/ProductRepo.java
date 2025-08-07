package com.example.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Entity.Product;

public interface ProductRepo extends JpaRepository<Product, Long> {
	// Additional query methods can be defined here if needed
	List<Product> findByNameContainingIgnoreCase(String name); 

}
