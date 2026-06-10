package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "user") // ĐÃ SỬA: Đổi từ "users" thành "user" để đồng bộ dữ liệu
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    private String username;
    private String password;
    private String phone;
    private String address;
    @Column(name = "full_name")
    private String fullName;
    
    private String role; // Lưu 2 giá trị: "ADMIN" hoặc "USER" để khớp hệ thống sidebar

    @Column(name = "avatar_url")
    private String avatarUrl = "default-avatar.png"; // ĐÃ THÊM: Thuộc tính bốc file ảnh từ máy khách


    // Constructor rỗng
    public User() {}

    // Getters và Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getPhone() { return phone; }
public void setPhone(String phone) { this.phone = phone; }
public String getAddress() { return address; }
public void setAddress(String address) { this.address = address; }
    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
}