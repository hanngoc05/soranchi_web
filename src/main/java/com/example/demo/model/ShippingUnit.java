package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "PhuongThucVanChuyen")
public class ShippingUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_ptvc")
    private int id;

    @Column(name = "ten_ptvc", nullable = false)
    private String name;

    @Column(name = "phi_vanchuyen", nullable = false)
    private double shippingFee;

    @Column(name = "thoi_gian_du_kien")
    private String estimatedTime;

    @Column(name = "mo_ta")
    private String description;

    @Column(name = "trang_thai", nullable = false)
    private String status = "ACTIVE";

    @Transient
    private String unitCode;

    public ShippingUnit() {}

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