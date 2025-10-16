package com.foodwaste.repository;

import com.foodwaste.model.Order;
import com.foodwaste.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    List<Order> findByNgoOrderByCreatedAtDesc(User ngo);
    
    List<Order> findByStatus(Order.OrderStatus status);
    
    List<Order> findByOrderId(String orderId);
}