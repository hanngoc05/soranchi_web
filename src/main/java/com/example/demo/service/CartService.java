package com.example.demo.service;

import com.example.demo.model.CartItem;
import com.example.demo.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    // 1. Thêm vào giỏ
    public void addItem(CartItem newItem) {
        // Kiểm tra xem món này có trong MySQL chưa
        CartItem existingItem = cartItemRepository.findByProductId(newItem.getProductId());
        
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
    
    // 3. Tính tổng tiền
    public double getTotalPrice() {
        double total = 0;
        for (CartItem item : cartItemRepository.findAll()) {
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

    // 5. Tính năng XÓA 1 món khỏi giỏ (Mới thêm)
    public void removeItem(int id) {
        cartItemRepository.deleteById(id);
    }
    // 6. CẬP NHẬT SỐ LƯỢNG (+ / -) -> ĐÂY CHÍNH LÀ HÀM BẠN ĐANG THIẾU
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
        cartItemRepository.deleteAll(); // Xóa sạch toàn bộ cốc trà sữa đang có trong bảng giỏ hàng
    }
    
}