package com.example.demo.repository;

import com.example.demo.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByUserIdOrderByCreatedAtDesc(int userId); // Truy vấn bản đặc tả yêu cầu
}