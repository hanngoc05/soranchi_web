// CuocHoiThoai.java
package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "CuocHoiThoai")
public class CuocHoiThoai {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_cuochoithoai")
    private int maCuocHoiThoai;

    @Column(name = "tieu_de")
    private String tieuDe;

    @Column(name = "trang_thai", nullable = false)
    private String trangThai = "ACTIVE";

    @Column(name = "thoi_gian_tao", nullable = false)
    private LocalDateTime thoiGianTao = LocalDateTime.now();

    @Column(name = "ma_tk", nullable = false)
    private Integer maNguoiDung;

    @Transient
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