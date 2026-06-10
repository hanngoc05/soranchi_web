package com.example.demo.repository;

import com.example.demo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    // Tự động sinh hàm lọc theo category
    List<Product> findByCategory(String category);
    // Hàm mới: Tìm kiếm sản phẩm theo tên (không phân biệt hoa thường)
    List<Product> findByNameContainingIgnoreCase(String name);
}
