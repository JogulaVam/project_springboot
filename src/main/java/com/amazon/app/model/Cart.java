package com.amazon.app.model;

import jakarta.persistence.Entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private double id;
	
	@OneToOne
	private User user;
	
	@OneToMany(mappedBy="cart",cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CartItem> items = new ArrayList<>();
	
}
