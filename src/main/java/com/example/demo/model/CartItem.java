package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "cart_items") // Tạo bảng tên là cart_items trong MySQL
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // Bắt buộc phải có ID riêng cho dòng trong giỏ hàng
    
    @Column(name = "user_id")
    private int userId; // Thêm để phân biệt giỏ hàng của từng khách hàng
    
    private int productId;
    private String name;
    
    @Column(length = 1000)
    private String imageUrl;
    
    private double price;
    private int quantity;

    public CartItem() {}

    public CartItem(int productId, String name, String imageUrl, double price, int quantity) {
        this.productId = productId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
        this.quantity = quantity;
    }

    public CartItem(int userId, int productId, String name, String imageUrl, double price, int quantity) {
        this.userId = userId;
        this.productId = productId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
        this.quantity = quantity;
    }

    // --- GETTERS ---
    public int getId() { return id; }
    public int getUserId() { return userId; }
    public int getProductId() { return productId; }
    public String getName() { return name; }
    public String getImageUrl() { return imageUrl; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }

    // --- SETTERS ---
    public void setId(int id) { this.id = id; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setProductId(int productId) { this.productId = productId; }
    public void setName(String name) { this.name = name; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setPrice(double price) { this.price = price; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}