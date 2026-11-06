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

    // 1. Thêm vào giỏ
    public void addItem(CartItem newItem) {
        CartItem existingItem = null;
        if (newItem.getUserId() > 0) {
            existingItem = cartItemRepository.findByUserIdAndProductId(newItem.getUserId(), newItem.getProductId());
        } else {
            existingItem = cartItemRepository.findByProductId(newItem.getProductId());
        }
        
        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + newItem.getQuantity());
            cartItemRepository.save(existingItem);
        } else {
            cartItemRepository.save(newItem);
        }
    }

    // 2. Lấy danh sách món trong giỏ
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
    
    // 4. Đếm số loại đồ uống (trả về số dòng sản phẩm, ví dụ: 2 loại trả về 2)
    public int getCount() {
        return cartItemRepository.findAll().size();
    }

    public int getCount(int userId) {
        return cartItemRepository.findByUserId(userId).size();
    }

    // 5. Xóa 1 món khỏi giỏ
    public void removeItem(int id) {
        cartItemRepository.deleteById(id);
    }

    // 6. Cập nhật số lượng (+ / -)
    public void updateQuantity(int id, int qty) {
        CartItem item = cartItemRepository.findById(id).orElse(null);
        if (item != null) {
            if (qty <= 0) {
                cartItemRepository.deleteById(id);
            } else {
                item.setQuantity(qty);
                cartItemRepository.save(item);
            }
        }
    }

    // 7. Xóa sạch giỏ hàng
    public void clear() {
        cartItemRepository.deleteAll();
    }

    @Transactional
    public void clear(int userId) {
        cartItemRepository.deleteByUserId(userId);
    }
}