package com.amazon.app.Dto;
import lombok.Data;

@Data
public class ProductInput {
	private String name;
	private String description;
	private String imageUrl;
	private double price;
	private int stock;
	private int categoryId;
}
