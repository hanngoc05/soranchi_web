package com.example.demo.repository;

import com.example.demo.model.ChiTietDonHangTopping;
import com.example.demo.model.ChiTietDonHangToppingId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChiTietDonHangToppingRepository extends JpaRepository<ChiTietDonHangTopping, ChiTietDonHangToppingId> {
    List<ChiTietDonHangTopping> findByOrderDetailId(int orderDetailId);
}