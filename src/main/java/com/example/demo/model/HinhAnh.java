package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "HinhAnh")
public class HinhAnh {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_hinhanh")
    private int id;

    @Column(name = "hinhanh_url", nullable = false, length = 1000)
    private String hinhanhUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_sp", nullable = false)
    private Product product;

    public HinhAnh() {}

    public HinhAnh(String hinhanhUrl, Product product) {
        this.hinhanhUrl = hinhanhUrl;
        this.product = product;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getHinhanhUrl() { return hinhanhUrl; }
    public void setHinhanhUrl(String hinhanhUrl) { this.hinhanhUrl = hinhanhUrl; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
}