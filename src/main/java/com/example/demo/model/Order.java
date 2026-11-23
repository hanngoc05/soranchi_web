package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "DonHang")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_donhang")
    private int id;

    @Column(name = "ten_nguoi_nhan", nullable = false)
    private String fullName;

    @Column(name = "sdt", nullable = false)
    private String phone;

    @Column(name = "dia_chi", nullable = false)
    private String address;

    @Column(name = "tong_gia", nullable = false)
    private double totalPrice;

    @Column(name = "tinh_trang_dh", nullable = false)
    private String status = "CHỜ XÁC NHẬN";

    @Column(name = "ngay_dathang", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "ma_tk", nullable = false)
    private int userId;

    @Column(name = "ma_ptvc", nullable = false)
    private Integer shippingUnitId;

    @Column(name = "ma_km")
    private Integer promotionId;

    @Transient
    private String paymentMethod = "COD";

    public Order() {}

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
    public Integer getShippingUnitId() { return shippingUnitId; }
    public void setShippingUnitId(Integer shippingUnitId) { this.shippingUnitId = shippingUnitId; }
    public Integer getPromotionId() { return promotionId; }
    public void setPromotionId(Integer promotionId) { this.promotionId = promotionId; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}