package com.amazon.app.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.amazon.app.model.Product;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long>{
	
	List<Product> findByCategoryName(String name);

}
