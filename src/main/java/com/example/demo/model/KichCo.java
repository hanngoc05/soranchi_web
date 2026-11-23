package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "KichCo")
public class KichCo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_size")
    private int id;

    @Column(name = "ten_size", nullable = false, length = 10)
    private String tenSize;

    @Column(name = "gia_phu_thu", nullable = false)
    private double giaPhuThu = 0;

    public KichCo() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTenSize() { return tenSize; }
    public void setTenSize(String tenSize) { this.tenSize = tenSize; }

    public double getGiaPhuThu() { return giaPhuThu; }
    public void setGiaPhuThu(double giaPhuThu) { this.giaPhuThu = giaPhuThu; }
}