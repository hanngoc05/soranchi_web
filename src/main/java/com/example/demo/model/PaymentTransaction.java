package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "giaodichthanhtoan")
public class PaymentTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_giaodich")
    private int id;

    @Column(name = "ma_donhang")
    private int orderId;

    @Column(name = "so_tien")
    private double amount;

    @Column(name = "thoi_gian")
    private LocalDateTime transactionTime = LocalDateTime.now();

    @Column(name = "trang_thai")
    private String status; // PENDING, SUCCESS, FAILED

    @Column(name = "ma_phanhoi")
    private String responseCode;

    @Column(name = "ma_pttt")
    private int paymentMethodId; // 1: COD, 2: BANK (VietQR)

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