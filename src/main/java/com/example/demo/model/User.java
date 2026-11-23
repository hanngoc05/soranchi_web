package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "TaiKhoan")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_tk")
    private int id;

    @Column(name = "ten_dangnhap", nullable = false, unique = true)
    private String username;

    @Column(name = "mat_khau", nullable = false)
    private String password;

    @Column(name = "ho_ten", nullable = false)
    private String fullName;

    @Column(name = "sdt")
    private String phone;

    @Column(name = "dia_chi")
    private String address;

    @Column(name = "vai_tro", nullable = false)
    private String role = "USER";

    @Column(name = "trang_thai", nullable = false)
    private String status = "ACTIVE";

    @Transient
    private String avatarUrl = "default-avatar.png";

    @Transient
    private boolean active = true;

    public User() {}

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
    public void setActive(boolean active) { this.active = active; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public boolean isActive() { return !"LOCKED".equalsIgnoreCase(this.status); }
}