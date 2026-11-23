package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ChiTietDonHang")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_ctdh")
    private int id;

    @Column(name = "so_luong", nullable = false)
    private int quantity;

    @Column(name = "don_gia", nullable = false)
    private double price;

    @Column(name = "ghi_chu")
    private String note;

    @Column(name = "ma_donhang", nullable = false)
    private int orderId;

    @Column(name = "ma_sp", nullable = false)
    private int productId;

    @Column(name = "ma_size", nullable = false)
    private int sizeId = 1; // Mặc định Size S (id=1)

    @Transient
    private String productName;

    @Transient
    private String imageUrl;

    @Transient
    private boolean isReviewed = false;

    public OrderDetail() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    public int getSizeId() { return sizeId; }
    public void setSizeId(int sizeId) { this.sizeId = sizeId; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public boolean isReviewed() { return isReviewed; }
    public void setReviewed(boolean reviewed) { isReviewed = reviewed; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}