package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name = "user_id")
    private int userId;
    
    private int productId;
    private String name;
    private String size = "M";       // Mặc định Size M
    private String toppings = "";    // Mặc định không có topping
    private double basePrice;        // Giá gốc chưa tính size/topping
    
    @Column(length = 1000)
    private String imageUrl;
    
    private double price;            // Đơn giá thực tế (đã cộng size/topping)
    private int quantity;

    public CartItem() {}

    public CartItem(int productId, String name, String imageUrl, double price, int quantity) {
        this.productId = productId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.basePrice = price;
        this.price = price;
        this.quantity = quantity;
        this.size = "M";
        this.toppings = "";
    }

    public CartItem(int userId, int productId, String name, String imageUrl, double price, int quantity) {
        this.userId = userId;
        this.productId = productId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.basePrice = price;
        this.price = price;
        this.quantity = quantity;
        this.size = "M";
        this.toppings = "";
    }

    public CartItem(int userId, int productId, String name, String size, String toppings, String imageUrl, double basePrice, double price, int quantity) {
        this.userId = userId;
        this.productId = productId;
        this.name = name;
        this.size = (size != null && !size.isEmpty()) ? size : "M";
        this.toppings = (toppings != null) ? toppings : "";
        this.imageUrl = imageUrl;
        this.basePrice = basePrice;
        this.price = price;
        this.quantity = quantity;
    }

    // --- GETTERS ---
    public int getId() { return id; }
    public int getUserId() { return userId; }
    public int getProductId() { return productId; }
    public String getName() { return name; }
    public String getSize() { return size; }
    public String getToppings() { return toppings; }
    public double getBasePrice() { return basePrice; }
    public String getImageUrl() { return imageUrl; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }

    // --- SETTERS ---
    public void setId(int id) { this.id = id; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setProductId(int productId) { this.productId = productId; }
    public void setName(String name) { this.name = name; }
    public void setSize(String size) { this.size = size; }
    public void setToppings(String toppings) { this.toppings = toppings; }
    public void setBasePrice(double basePrice) { this.basePrice = basePrice; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setPrice(double price) { this.price = price; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}