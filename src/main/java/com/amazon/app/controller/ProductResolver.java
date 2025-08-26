package com.amazon.app.controller;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import com.amazon.app.repository.ProductRepository;
import com.amazon.app.Dto.ProductInput;
import com.amazon.app.model.Category;
import com.amazon.app.model.Product;
import com.amazon.app.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ProductResolver {
	
	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;
	
	@QueryMapping
	public List<Product> products(){
		return productRepository.findAll();
	}
	
	@QueryMapping
	public Product productById(@Argument long id) {
		return productRepository.findById(id).orElse(null);
	}
	
	@MutationMapping
	@PreAuthorize("hasRole('ADMIN')")
	public Category addCategory(@Argument String name) {
		if(categoryRepository.findByName(name).isPresent()) {
			throw new RuntimeException("Category already exists");
		}
		return categoryRepository.save(Category.builder().name(name).build());
	}
	
	@QueryMapping
	public List<Category> productCategories(){
		return categoryRepository.findAll();
	}
	
	@MutationMapping
	@PreAuthorize("hasRole('ADMIN')")
	public Product addProduct(@Argument ProductInput input) {
		Category cat = categoryRepository.findById(Long.valueOf(input.getCategoryId()))
				.orElseThrow(()-> new RuntimeException("Category not found"));
		Product p = Product.builder()
				.name(input.getName())
				.description(input.getDescription())
				.imageUrl(input.getImageUrl())
				.price(input.getPrice())
				.stock(input.getStock())
				.category(cat)
				.build();
		return productRepository.save(p);
	}
	
	@MutationMapping
	@PreAuthorize("hasRole('ADMIN')")
	public Product updateProduct(@Argument Long id, @Argument ProductInput input) {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Product not found"));
		Category category = categoryRepository.findById(Long.valueOf(input.getCategoryId()))
				.orElseThrow(()-> new RuntimeException("Category not found"));
		
		product.setName(input.getName());
		product.setDescription(input.getDescription());
		product.setImageUrl(input.getImageUrl());
		product.setPrice(input.getPrice());
		product.setStock(input.getStock());
		product.setCategory(category);
		
		return productRepository.save(product);
				
	}
	
	@MutationMapping
	@PreAuthorize("hasRole('ADMIN')")
	public String deleteProduct(@Argument Long id) {
		if(!productRepository.existsById(id))
			throw new RuntimeException("Product not Found");
		productRepository.deleteById(id);
		
		return "Product Removed";
	}
	
	
}
