package com.example.demo.repository;

import com.example.demo.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    CartItem findByProductId(int productId);
    List<CartItem> findByUserId(int userId);
    CartItem findByUserIdAndProductId(int userId, int productId);
    void deleteByUserId(int userId);
}