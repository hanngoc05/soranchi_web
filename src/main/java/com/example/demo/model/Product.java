package com.example.demo.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "SanPham")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_sp")
    private int id;

    @Column(name = "ten_sp", nullable = false)
    private String name;

    @Column(name = "gia", nullable = false)
    private double price;

    @Column(name = "mo_ta", columnDefinition = "TEXT")
    private String description;

    @Column(name = "so_luong_ton", nullable = false)
    private int quantity;

    @Column(name = "trang_thai", nullable = false)
    private String status = "ACTIVE";

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ma_dm", nullable = false)
    private Category categoryObj;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<HinhAnh> images = new ArrayList<>();

    @Transient
    private String productCode;

    @Transient
    private String imageUrl;

    @Transient
    private String category;

    public Product() {}

    // Tự động lấy URL ảnh đầu tiên từ bảng HinhAnh nếu imageUrl chưa gán tay
    public String getImageUrl() {
        if (this.imageUrl != null && !this.imageUrl.isEmpty()) return this.imageUrl;
        if (this.images != null && !this.images.isEmpty()) {
            return this.images.get(0).getHinhanhUrl();
        }
        return "/images/default-drink.png";
    }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    // Tự động lấy tên danh mục từ bảng DanhMuc
    public String getCategory() {
        if (this.category != null && !this.category.isEmpty()) return this.category;
        return (this.categoryObj != null) ? this.categoryObj.getName() : "";
    }
    public void setCategory(String category) { this.category = category; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getProductCode() { return productCode; }
    public void setProductCode(String productCode) { this.productCode = productCode; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Category getCategoryObj() { return categoryObj; }
    public void setCategoryObj(Category categoryObj) { this.categoryObj = categoryObj; }
    public List<HinhAnh> getImages() { return images; }
    public void setImages(List<HinhAnh> images) { this.images = images; }
}