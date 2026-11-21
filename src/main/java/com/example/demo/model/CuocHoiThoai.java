package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "cuoc_hoi_thoai")
public class CuocHoiThoai {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_cuoc_hoi_thoai")
    private int maCuocHoiThoai;

    @Column(name = "tieu_de")
    private String tieuDe;

    @Column(name = "thoi_gian_tao")
    private LocalDateTime thoiGianTao = LocalDateTime.now();

    @Column(name = "trang_thai")
    private String trangThai = "ACTIVE";

    @Column(name = "ma_nguoi_dung")
    private Integer maNguoiDung;

    @Column(name = "session_id")
    private String sessionId;

    public CuocHoiThoai() {}

    public int getMaCuocHoiThoai() { return maCuocHoiThoai; }
    public void setMaCuocHoiThoai(int maCuocHoiThoai) { this.maCuocHoiThoai = maCuocHoiThoai; }

    public String getTieuDe() { return tieuDe; }
    public void setTieuDe(String tieuDe) { this.tieuDe = tieuDe; }

    public LocalDateTime getThoiGianTao() { return thoiGianTao; }
    public void setThoiGianTao(LocalDateTime thoiGianTao) { this.thoiGianTao = thoiGianTao; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public Integer getMaNguoiDung() { return maNguoiDung; }
    public void setMaNguoiDung(Integer maNguoiDung) { this.maNguoiDung = maNguoiDung; }

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }
}