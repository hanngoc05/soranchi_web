package com.example.demo.repository;

import com.example.demo.model.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Integer> {
    // Hàm này để lấy danh sách khuyến mãi đang bật
    List<Promotion> findByIsActiveTrue();
}