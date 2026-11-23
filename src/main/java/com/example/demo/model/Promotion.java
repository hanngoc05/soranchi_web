package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "KhuyenMai")
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_km")
    private int id;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "ten_km", nullable = false)
    private String title;

    @Column(name = "mo_ta")
    private String description;

    @Column(name = "phan_tram_giam_gia", nullable = false)
    private int discountPercent;

    @Column(name = "ngay_bat_dau", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "ngay_ket_thuc", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "tinh_trang", nullable = false)
    private boolean isActive = true;

    @Transient
    private String imageUrl;

    public boolean isExpired() {
        if (this.endDate == null) return false;
        return LocalDateTime.now().isAfter(this.endDate);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getDiscountPercent() { return discountPercent; }
    public void setDiscountPercent(int discountPercent) { this.discountPercent = discountPercent; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }
    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}