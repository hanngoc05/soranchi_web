package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tin_nhan")
public class TinNhan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_tin_nhan")
    private int maTinNhan;

    @Column(name = "vai_tro", nullable = false)
    private String vaiTro; // "USER" hoặc "BOT"

    @Column(name = "noi_dung", columnDefinition = "TEXT", nullable = false)
    private String noiDung;

    @Column(name = "thoi_gian_gui")
    private LocalDateTime thoiGianGui = LocalDateTime.now();

    @Column(name = "ma_cuoc_hoi_thoai", nullable = false)
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