package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ChiTietGioHang_Topping")
@IdClass(ChiTietGioHangToppingId.class)
public class ChiTietGioHangTopping {

    @Id
    @Column(name = "ma_ctgh", nullable = false)
    private int cartItemId;

    @Id
    @Column(name = "ma_topping", nullable = false)
    private int toppingId;

    @Column(name = "so_luong", nullable = false)
    private int quantity = 1;

    public ChiTietGioHangTopping() {}

    public ChiTietGioHangTopping(int cartItemId, int toppingId, int quantity) {
        this.cartItemId = cartItemId;
        this.toppingId = toppingId;
        this.quantity = quantity;
    }

    public int getCartItemId() { return cartItemId; }
    public void setCartItemId(int cartItemId) { this.cartItemId = cartItemId; }

    public int getToppingId() { return toppingId; }
    public void setToppingId(int toppingId) { this.toppingId = toppingId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}