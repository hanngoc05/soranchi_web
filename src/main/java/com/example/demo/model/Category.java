package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    private String name;  
    private String alias; 
    private String status = "ACTIVE"; // Mới thêm: ACTIVE hoặc INACTIVE

    public Category() {}

    // Getters và Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAlias() { return alias; }
    public void setAlias(String alias) { this.alias = alias; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}