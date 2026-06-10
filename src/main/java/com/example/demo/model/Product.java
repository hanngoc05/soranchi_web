package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    private String name;
    private double price;
    
    @Column(name = "image_url", length = 1000)
    private String imageUrl;
    
    private String category;

    public Product() {}

    public Product(int id, String name, double price, String imageUrl, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
    }
    
    // ==========================================
    // GETTERS (Lấy dữ liệu từ object ra)
    // ==========================================
    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getImageUrl() { return imageUrl; }
    public String getCategory() { return category; } 

    // ==========================================
    // SETTERS (Nhét dữ liệu từ HTML form vào object)
    // ==========================================
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setPrice(double price) { this.price = price; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setCategory(String category) { this.category = category; }
}