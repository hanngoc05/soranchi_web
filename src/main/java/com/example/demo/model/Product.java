package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "product_code")
    private String productCode;
    
    private String name;
    private double price;
    
    @Column(name = "image_url", length = 1000)
    private String imageUrl;
    
    private String category;
    
    private int quantity;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "status")
    private String status = "ĐANG BÁN"; // "ĐANG BÁN" hoặc "NGỪNG BÁN"

    public Product() {}

    public Product(int id, String productCode, String name, double price, String imageUrl, String category, int quantity, String description, String status) {
        this.id = id;
        this.productCode = productCode;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
        this.quantity = quantity;
        this.description = description;
        this.status = status;
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getProductCode() { return productCode; }
    public void setProductCode(String productCode) { this.productCode = productCode; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getCategory() { return category; } 
    public void setCategory(String category) { this.category = category; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}