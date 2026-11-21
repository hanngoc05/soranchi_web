package com.example.demo.repository;

import com.example.demo.model.CuocHoiThoai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CuocHoiThoaiRepository extends JpaRepository<CuocHoiThoai, Integer> {
    CuocHoiThoai findFirstByMaNguoiDungAndTrangThaiOrderByThoiGianTaoDesc(Integer maNguoiDung, String trangThai);
    CuocHoiThoai findFirstBySessionIdAndMaNguoiDungIsNullAndTrangThaiOrderByThoiGianTaoDesc(String sessionId, String trangThai);
}