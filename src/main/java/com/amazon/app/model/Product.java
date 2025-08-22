package com.amazon.app.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(nullable = false)
	private String name;
	
	private String description;
	
	private String imageUrl;
	
	private double price;
	
	private int stock;
	
	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;
}
