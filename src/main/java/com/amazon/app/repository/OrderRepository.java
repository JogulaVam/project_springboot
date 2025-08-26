package com.amazon.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amazon.app.model.Order;

public interface OrderRepository extends JpaRepository<Order,Long> {

}
