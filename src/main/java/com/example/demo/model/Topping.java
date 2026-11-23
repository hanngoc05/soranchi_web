package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Topping")
public class Topping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_topping")
    private int id;

    @Column(name = "ten_topping", nullable = false, length = 100)
    private String name;

    @Column(name = "gia", nullable = false)
    private double price;

    @Column(name = "mo_ta")
    private String description;

    @Column(name = "trang_thai", nullable = false)
    private String status = "ACTIVE";

    public Topping() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}