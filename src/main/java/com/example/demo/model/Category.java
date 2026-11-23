package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "DanhMuc")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_dm")
    private int id;

    @Column(name = "ten_dm", nullable = false)
    private String name;

    @Transient
    private String alias;

    @Transient
    private String status = "ACTIVE";

    public Category() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAlias() { return alias; }
    public void setAlias(String alias) { this.alias = alias; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}