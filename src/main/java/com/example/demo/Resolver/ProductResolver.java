package com.example.demo.Resolver;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;

import com.example.demo.Entity.Product;
import com.example.demo.Repository.ProductRepo;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ProductResolver {
	
	private final ProductRepo productRepo;
	
	@QueryMapping
	public Product getProductById(@Argument Long id) {
		return productRepo.findById(id).orElse(null);
	}
	@QueryMapping
	public List<Product> getAllProducts() {
		return productRepo.findAll();
	}
	@QueryMapping
	public List<Product> getProductsByCategory(@Argument String category) {
		return productRepo.findAll().stream()
			.filter(product -> product.getCategory().equalsIgnoreCase(category))
			.collect(Collectors.toList());
	}
	
	@MutationMapping
	public Product createProduct(@Argument String name, 
								  @Argument String description, 
								  @Argument Double price, 
								  @Argument String category, 
								  @Argument String imageUrl) {
		Product product = new Product();
		product.setName(name);
		product.setDescription(description);
		product.setPrice(price);
		product.setCategory(category);
		product.setImageUrl(imageUrl);
		return productRepo.save(product);
	}
	@MutationMapping
	public Product updateProduct(@Argument Long id, 
								  @Argument String name, 
								  @Argument String description, 
								  @Argument Double price, 
								  @Argument String category, 
								  @Argument String imageUrl) {
		Product product = productRepo.findById(id).orElse(null);
		if (product != null) {
			product.setName(name);
			product.setDescription(description);
			product.setPrice(price);
			product.setCategory(category);
			product.setImageUrl(imageUrl);
			
		}
		return productRepo.save(product);
	}
	@MutationMapping
	public String deleteProduct(@Argument Long id) {
		if (productRepo.existsById(id)) {
			productRepo.deleteById(id);
			return "Product with ID " + id + " deleted successfully.";
		}
		return "Product with ID " + id + " not found.";
	}
}
