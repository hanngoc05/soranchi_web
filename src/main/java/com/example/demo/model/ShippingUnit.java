package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "shipping_units")
public class ShippingUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "unit_code", unique = true)
    private String unitCode; // Mã đơn vị (VD: DVVC01)

    @Column(nullable = false, unique = true)
    private String name; // Tên đơn vị vận chuyển

    @Column(name = "shipping_fee", nullable = false)
    private double shippingFee; // Phí vận chuyển (>= 0)

    @Column(name = "estimated_time")
    private String estimatedTime; // Thời gian giao dự kiến (VD: 30 - 45 phút)

    @Column(columnDefinition = "TEXT")
    private String description; // Mô tả

    private String status = "HOẠT ĐỘNG"; // "HOẠT ĐỘNG" hoặc "NGỪNG HOẠT ĐỘNG"

    public ShippingUnit() {}

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUnitCode() { return unitCode; }
    public void setUnitCode(String unitCode) { this.unitCode = unitCode; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getShippingFee() { return shippingFee; }
    public void setShippingFee(double shippingFee) { this.shippingFee = shippingFee; }

    public String getEstimatedTime() { return estimatedTime; }
    public void setEstimatedTime(String estimatedTime) { this.estimatedTime = estimatedTime; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}