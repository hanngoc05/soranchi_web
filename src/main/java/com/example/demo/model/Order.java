package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name = "user_id")
    private int userId;
    
    @Column(name = "full_name")
    private String fullName;
    
    private String phone;
    private String address;
    
    @Column(name = "total_price")
    private double totalPrice;
    
    private String status = "CHỜ XÁC NHẬN";
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public Order() {}

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    // Khai báo thêm thuộc tính này vào trong file Order.java của bạn
@Column(name = "payment_method")
private String paymentMethod = "COD";

// Getter và Setter cho paymentMethod
public String getPaymentMethod() { return paymentMethod; }
public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}