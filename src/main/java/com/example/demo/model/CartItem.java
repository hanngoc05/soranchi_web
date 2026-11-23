package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ChiTietGioHang")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_ctgh")
    private int id;

    @Column(name = "so_luong", nullable = false)
    private int quantity;

    @Column(name = "ghi_chu")
    private String note;

    @Column(name = "ma_gh", nullable = false)
    private int cartId;

    @Column(name = "ma_sp", nullable = false)
    private int productId;

    @Column(name = "ma_size", nullable = false)
    private int sizeId = 1;

    @Transient private int userId;
    @Transient private String name;
    @Transient private String size = "M";
    @Transient private String toppings = "";
    @Transient private double basePrice;
    @Transient private String imageUrl;
    @Transient private double price;

    public CartItem() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getCartId() { return cartId; }
    public void setCartId(int cartId) { this.cartId = cartId; }
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    public int getSizeId() { return sizeId; }
    public void setSizeId(int sizeId) { this.sizeId = sizeId; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }
    public String getToppings() { return toppings; }
    public void setToppings(String toppings) { this.toppings = toppings; }
    public double getBasePrice() { return basePrice; }
    public void setBasePrice(double basePrice) { this.basePrice = basePrice; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}