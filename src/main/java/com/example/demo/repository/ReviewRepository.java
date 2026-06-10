package com.example.demo.repository;

import com.example.demo.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    // Tìm danh sách đánh giá theo ID sản phẩm và sắp xếp cái mới nhất lên đầu
    List<Review> findByProductIdOrderByCreatedAtDesc(int productId);
}