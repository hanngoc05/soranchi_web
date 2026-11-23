package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ChiTietDonHang_Topping")
@IdClass(ChiTietDonHangToppingId.class)
public class ChiTietDonHangTopping {

    @Id
    @Column(name = "ma_ctdh", nullable = false)
    private int orderDetailId;

    @Id
    @Column(name = "ma_topping", nullable = false)
    private int toppingId;

    @Column(name = "so_luong", nullable = false)
    private int quantity = 1;

    @Column(name = "gia_topping", nullable = false)
    private double price;

    public ChiTietDonHangTopping() {}

    public ChiTietDonHangTopping(int orderDetailId, int toppingId, int quantity, double price) {
        this.orderDetailId = orderDetailId;
        this.toppingId = toppingId;
        this.quantity = quantity;
        this.price = price;
    }

    public int getOrderDetailId() { return orderDetailId; }
    public void setOrderDetailId(int orderDetailId) { this.orderDetailId = orderDetailId; }

    public int getToppingId() { return toppingId; }
    public void setToppingId(int toppingId) { this.toppingId = toppingId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}