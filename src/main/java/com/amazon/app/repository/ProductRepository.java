package com.amazon.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amazon.app.model.Product;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long>{
	List<Product> findByCategoryName(String name);
}
