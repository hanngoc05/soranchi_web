package com.example.demo.model;

import java.io.Serializable;
import java.util.Objects;

public class ChiTietDonHangToppingId implements Serializable {
    private int orderDetailId;
    private int toppingId;

    public ChiTietDonHangToppingId() {}

    public ChiTietDonHangToppingId(int orderDetailId, int toppingId) {
        this.orderDetailId = orderDetailId;
        this.toppingId = toppingId;
    }

    public int getOrderDetailId() { return orderDetailId; }
    public void setOrderDetailId(int orderDetailId) { this.orderDetailId = orderDetailId; }

    public int getToppingId() { return toppingId; }
    public void setToppingId(int toppingId) { this.toppingId = toppingId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChiTietDonHangToppingId that)) return false;
        return orderDetailId == that.orderDetailId && toppingId == that.toppingId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderDetailId, toppingId);
    }
}