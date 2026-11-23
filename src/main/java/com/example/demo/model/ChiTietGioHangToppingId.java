package com.example.demo.model;

import java.io.Serializable;
import java.util.Objects;

public class ChiTietGioHangToppingId implements Serializable {
    private int cartItemId;
    private int toppingId;

    public ChiTietGioHangToppingId() {}

    public ChiTietGioHangToppingId(int cartItemId, int toppingId) {
        this.cartItemId = cartItemId;
        this.toppingId = toppingId;
    }

    public int getCartItemId() { return cartItemId; }
    public void setCartItemId(int cartItemId) { this.cartItemId = cartItemId; }

    public int getToppingId() { return toppingId; }
    public void setToppingId(int toppingId) { this.toppingId = toppingId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChiTietGioHangToppingId that)) return false;
        return cartItemId == that.cartItemId && toppingId == that.toppingId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cartItemId, toppingId);
    }
}