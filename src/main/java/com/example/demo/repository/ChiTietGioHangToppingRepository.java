package com.example.demo.repository;

import com.example.demo.model.ChiTietGioHangTopping;
import com.example.demo.model.ChiTietGioHangToppingId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChiTietGioHangToppingRepository extends JpaRepository<ChiTietGioHangTopping, ChiTietGioHangToppingId> {
    List<ChiTietGioHangTopping> findByCartItemId(int cartItemId);
    void deleteByCartItemId(int cartItemId);
}