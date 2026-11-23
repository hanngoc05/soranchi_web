// TinNhan.java
package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TinNhan")
public class TinNhan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_tinnhan")
    private int maTinNhan;

    @Column(name = "noi_dung", columnDefinition = "TEXT", nullable = false)
    private String noiDung;

    @Column(name = "vai_tro", nullable = false)
    private String vaiTro;

    @Column(name = "thoi_gian_gui", nullable = false)
    private LocalDateTime thoiGianGui = LocalDateTime.now();

    @Column(name = "ma_cuochoithoai", nullable = false)
    private int maCuocHoiThoai;

    public TinNhan() {}

    public TinNhan(String vaiTro, String noiDung, int maCuocHoiThoai) {
        this.vaiTro = vaiTro;
        this.noiDung = noiDung;
        this.maCuocHoiThoai = maCuocHoiThoai;
        this.thoiGianGui = LocalDateTime.now();
    }

    public int getMaTinNhan() { return maTinNhan; }
    public void setMaTinNhan(int maTinNhan) { this.maTinNhan = maTinNhan; }
    public String getVaiTro() { return vaiTro; }
    public void setVaiTro(String vaiTro) { this.vaiTro = vaiTro; }
    public String getNoiDung() { return noiDung; }
    public void setNoiDung(String noiDung) { this.noiDung = noiDung; }
    public LocalDateTime getThoiGianGui() { return thoiGianGui; }
    public void setThoiGianGui(LocalDateTime thoiGianGui) { this.thoiGianGui = thoiGianGui; }
    public int getMaCuocHoiThoai() { return maCuocHoiThoai; }
    public void setMaCuocHoiThoai(int maCuocHoiThoai) { this.maCuocHoiThoai = maCuocHoiThoai; }
}