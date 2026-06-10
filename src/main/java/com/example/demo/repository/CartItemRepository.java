package com.example.demo.repository;

import com.example.demo.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    // Hàm tự chế: Tìm xem món trà sữa này đã nằm trong giỏ chưa
    CartItem findByProductId(int productId); 
}