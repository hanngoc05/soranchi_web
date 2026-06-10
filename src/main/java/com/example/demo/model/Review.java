package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "reviews")
public class Review {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name = "product_id")
    private int productId;
    
    @Column(name = "customer_name")
    private String customerName;
    
    private int rating;
    private String comment;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    // Hàm bổ trợ để hiển thị ngày tháng ra HTML cho đẹp (dd/MM/yyyy)
    public String getFormattedDate() {
        if (this.createdAt == null) return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return this.createdAt.format(formatter);
    }

    // Các hàm Constructor
    public Review() {}

    public Review(int productId, String customerName, int rating, String comment) {
        this.productId = productId;
        this.customerName = customerName;
        this.rating = rating;
        this.comment = comment;
    }

    // Getter và Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}