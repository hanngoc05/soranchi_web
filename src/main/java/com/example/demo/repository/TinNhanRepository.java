package com.example.demo.repository;

import com.example.demo.model.TinNhan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TinNhanRepository extends JpaRepository<TinNhan, Integer> {
    List<TinNhan> findByMaCuocHoiThoaiOrderByThoiGianGuiAsc(int maCuocHoiThoai);
    List<TinNhan> findTop6ByMaCuocHoiThoaiOrderByThoiGianGuiDesc(int maCuocHoiThoai);
}