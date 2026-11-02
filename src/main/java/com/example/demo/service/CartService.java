package com.example.demo.service;

import com.example.demo.model.CartItem;
import com.example.demo.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    // 1. Thêm vào giỏ (Tự nhận diện theo userId hoặc gọi cơ bản)
    public void addItem(CartItem newItem) {
        CartItem existingItem = null;
        if (newItem.getUserId() > 0) {
            existingItem = cartItemRepository.findByUserIdAndProductId(newItem.getUserId(), newItem.getProductId());
        } else {
            existingItem = cartItemRepository.findByProductId(newItem.getProductId());
        }
        
        if (existingItem != null) {
            // Có rồi thì cộng dồn số lượng
            existingItem.setQuantity(existingItem.getQuantity() + newItem.getQuantity());
            cartItemRepository.save(existingItem); // Lệnh UPDATE
        } else {
            // Chưa có thì lưu mới
            cartItemRepository.save(newItem); // Lệnh INSERT
        }
    }

    // 2. Lấy toàn bộ giỏ hàng
    public List<CartItem> getItems() {
        return cartItemRepository.findAll();
    }

    public List<CartItem> getItems(int userId) {
        return cartItemRepository.findByUserId(userId);
    }
    
    // 3. Tính tổng tiền
    public double getTotalPrice() {
        double total = 0;
        for (CartItem item : cartItemRepository.findAll()) {
            total += item.getPrice() * item.getQuantity();
        }
        return total;
    }

    public double getTotalPrice(int userId) {
        double total = 0;
        for (CartItem item : cartItemRepository.findByUserId(userId)) {
            total += item.getPrice() * item.getQuantity();
        }
        return total;
    }
    
    // 4. Đếm tổng số ly trà sữa
    public int getCount() {
        int count = 0;
        for (CartItem item : cartItemRepository.findAll()) {
            count += item.getQuantity();
        }
        return count;
    }

    public int getCount(int userId) {
        int count = 0;
        for (CartItem item : cartItemRepository.findByUserId(userId)) {
            count += item.getQuantity();
        }
        return count;
    }

    // 5. Tính năng XÓA 1 món khỏi giỏ
    public void removeItem(int id) {
        cartItemRepository.deleteById(id);
    }

    // 6. CẬP NHẬT SỐ LƯỢNG (+ / -)
    public void updateQuantity(int id, int qty) {
        CartItem item = cartItemRepository.findById(id).orElse(null);
        if (item != null) {
            if (qty <= 0) {
                cartItemRepository.deleteById(id); // Giảm về 0 thì tự động xóa luôn
            } else {
                item.setQuantity(qty);
                cartItemRepository.save(item);
            }
        }
    }

    // 7. XÓA SẠCH GIỎ HÀNG
    public void clear() {
        cartItemRepository.deleteAll();
    }

    @Transactional
    public void clear(int userId) {
        cartItemRepository.deleteByUserId(userId);
    }
}