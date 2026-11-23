package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "PhuongThucThanhToan")
public class PhuongThucThanhToan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_pttt")
    private int id;

    @Column(name = "ten_pttt", nullable = false)
    private String name;

    @Column(name = "trang_thai", nullable = false)
    private String status = "ACTIVE";

    public PhuongThucThanhToan() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}