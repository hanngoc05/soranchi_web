package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "GiaoDichThanhToan")
public class PaymentTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_giaodich")
    private int id;

    @Column(name = "so_tien", nullable = false)
    private double amount;

    @Column(name = "thoi_gian", nullable = false)
    private LocalDateTime transactionTime = LocalDateTime.now();

    @Column(name = "trang_thai", nullable = false)
    private String status;

    @Column(name = "ma_phanhoi")
    private String responseCode;

    @Column(name = "ma_donhang", nullable = false)
    private int orderId;

    @Column(name = "ma_pttt", nullable = false)
    private int paymentMethodId;

    public PaymentTransaction() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public LocalDateTime getTransactionTime() { return transactionTime; }
    public void setTransactionTime(LocalDateTime transactionTime) { this.transactionTime = transactionTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getResponseCode() { return responseCode; }
    public void setResponseCode(String responseCode) { this.responseCode = responseCode; }
    public int getPaymentMethodId() { return paymentMethodId; }
    public void setPaymentMethodId(int paymentMethodId) { this.paymentMethodId = paymentMethodId; }
}