package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "DanhGia")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_dg")
    private int id;

    @Column(name = "rating", nullable = false)
    private int rating;

    @Column(name = "nhan_xet")
    private String comment;

    @Column(name = "ngay_dg", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "ma_tk", nullable = false)
    private int userId;

    @Column(name = "ma_sp", nullable = false)
    private int productId;

    @Column(name = "ma_ctdh", nullable = false, unique = true)
    private Integer orderDetailId;

    @Transient
    private String customerName;

    public Review() {}

    public String getFormattedDate() {
        if (this.createdAt == null) return "";
        return this.createdAt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    public Integer getOrderDetailId() { return orderDetailId; }
    public void setOrderDetailId(Integer orderDetailId) { this.orderDetailId = orderDetailId; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}