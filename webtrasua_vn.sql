CREATE DATABASE IF NOT EXISTS webtrasua_vn CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE webtrasua_vn;
/*
 Navicat Premium Dump SQL

 Source Server         : vunghan
 Source Server Type    : MySQL
 Source Server Version : 90600 (9.6.0)
 Source Host           : localhost:3306
 Source Schema         : webtrasua_vn

 Target Server Type    : MySQL
 Target Server Version : 90600 (9.6.0)
 File Encoding         : 65001

 Date: 24/11/2026 13:51:11
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for ChiTietDonHang
-- ----------------------------
DROP TABLE IF EXISTS `ChiTietDonHang`;
CREATE TABLE `ChiTietDonHang` (
  `ma_ctdh` int NOT NULL AUTO_INCREMENT,
  `so_luong` int NOT NULL,
  `don_gia` double NOT NULL,
  `ghi_chu` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ma_donhang` int NOT NULL,
  `ma_sp` int NOT NULL,
  `ma_size` int NOT NULL,
  PRIMARY KEY (`ma_ctdh`),
  KEY `ma_donhang` (`ma_donhang`),
  KEY `ma_sp` (`ma_sp`),
  KEY `ma_size` (`ma_size`),
  CONSTRAINT `chitietdonhang_ibfk_1` FOREIGN KEY (`ma_donhang`) REFERENCES `DonHang` (`ma_donhang`) ON DELETE CASCADE,
  CONSTRAINT `chitietdonhang_ibfk_2` FOREIGN KEY (`ma_sp`) REFERENCES `SanPham` (`ma_sp`),
  CONSTRAINT `chitietdonhang_ibfk_3` FOREIGN KEY (`ma_size`) REFERENCES `KichCo` (`ma_size`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of ChiTietDonHang
-- ----------------------------
BEGIN;
INSERT INTO `ChiTietDonHang` (`ma_ctdh`, `so_luong`, `don_gia`, `ghi_chu`, `ma_donhang`, `ma_sp`, `ma_size`) VALUES (4, 1, 42000, '', 2, 8, 1);
INSERT INTO `ChiTietDonHang` (`ma_ctdh`, `so_luong`, `don_gia`, `ghi_chu`, `ma_donhang`, `ma_sp`, `ma_size`) VALUES (5, 1, 25000, '', 2, 2, 1);
INSERT INTO `ChiTietDonHang` (`ma_ctdh`, `so_luong`, `don_gia`, `ghi_chu`, `ma_donhang`, `ma_sp`, `ma_size`) VALUES (6, 2, 38000, '100% Đường, Đá bình thường', 2, 24, 1);
INSERT INTO `ChiTietDonHang` (`ma_ctdh`, `so_luong`, `don_gia`, `ghi_chu`, `ma_donhang`, `ma_sp`, `ma_size`) VALUES (7, 2, 38000, '', 3, 6, 1);
INSERT INTO `ChiTietDonHang` (`ma_ctdh`, `so_luong`, `don_gia`, `ghi_chu`, `ma_donhang`, `ma_sp`, `ma_size`) VALUES (8, 1, 25000, '', 4, 2, 1);
INSERT INTO `ChiTietDonHang` (`ma_ctdh`, `so_luong`, `don_gia`, `ghi_chu`, `ma_donhang`, `ma_sp`, `ma_size`) VALUES (9, 2, 48000, '70% Đường, Không đá', 4, 24, 3);
COMMIT;

-- ----------------------------
-- Table structure for ChiTietDonHang_Topping
-- ----------------------------
DROP TABLE IF EXISTS `ChiTietDonHang_Topping`;
CREATE TABLE `ChiTietDonHang_Topping` (
  `ma_ctdh` int NOT NULL,
  `ma_topping` int NOT NULL,
  `so_luong` int NOT NULL DEFAULT '1',
  `gia_topping` double NOT NULL,
  PRIMARY KEY (`ma_ctdh`,`ma_topping`),
  KEY `ma_topping` (`ma_topping`),
  CONSTRAINT `chitietdonhang_topping_ibfk_1` FOREIGN KEY (`ma_ctdh`) REFERENCES `ChiTietDonHang` (`ma_ctdh`) ON DELETE CASCADE,
  CONSTRAINT `chitietdonhang_topping_ibfk_2` FOREIGN KEY (`ma_topping`) REFERENCES `Topping` (`ma_topping`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of ChiTietDonHang_Topping
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for ChiTietGioHang
-- ----------------------------
DROP TABLE IF EXISTS `ChiTietGioHang`;
CREATE TABLE `ChiTietGioHang` (
  `ma_ctgh` int NOT NULL AUTO_INCREMENT,
  `so_luong` int NOT NULL DEFAULT '1',
  `ghi_chu` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ma_gh` int NOT NULL,
  `ma_sp` int NOT NULL,
  `ma_size` int NOT NULL,
  PRIMARY KEY (`ma_ctgh`),
  KEY `ma_gh` (`ma_gh`),
  KEY `ma_sp` (`ma_sp`),
  KEY `ma_size` (`ma_size`),
  CONSTRAINT `chitietgiohang_ibfk_1` FOREIGN KEY (`ma_gh`) REFERENCES `GioHang` (`ma_gh`) ON DELETE CASCADE,
  CONSTRAINT `chitietgiohang_ibfk_2` FOREIGN KEY (`ma_sp`) REFERENCES `SanPham` (`ma_sp`),
  CONSTRAINT `chitietgiohang_ibfk_3` FOREIGN KEY (`ma_size`) REFERENCES `KichCo` (`ma_size`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of ChiTietGioHang
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for ChiTietGioHang_Topping
-- ----------------------------
DROP TABLE IF EXISTS `ChiTietGioHang_Topping`;
CREATE TABLE `ChiTietGioHang_Topping` (
  `ma_ctgh` int NOT NULL,
  `ma_topping` int NOT NULL,
  `so_luong` int NOT NULL DEFAULT '1',
  PRIMARY KEY (`ma_ctgh`,`ma_topping`),
  KEY `ma_topping` (`ma_topping`),
  CONSTRAINT `chitietgiohang_topping_ibfk_1` FOREIGN KEY (`ma_ctgh`) REFERENCES `ChiTietGioHang` (`ma_ctgh`) ON DELETE CASCADE,
  CONSTRAINT `chitietgiohang_topping_ibfk_2` FOREIGN KEY (`ma_topping`) REFERENCES `Topping` (`ma_topping`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of ChiTietGioHang_Topping
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for CuocHoiThoai
-- ----------------------------
DROP TABLE IF EXISTS `CuocHoiThoai`;
CREATE TABLE `CuocHoiThoai` (
  `ma_cuochoithoai` int NOT NULL AUTO_INCREMENT,
  `tieu_de` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `trang_thai` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'ACTIVE',
  `thoi_gian_tao` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `ma_tk` int NOT NULL,
  PRIMARY KEY (`ma_cuochoithoai`),
  KEY `ma_tk` (`ma_tk`),
  CONSTRAINT `cuochoithoai_ibfk_1` FOREIGN KEY (`ma_tk`) REFERENCES `TaiKhoan` (`ma_tk`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of CuocHoiThoai
-- ----------------------------
BEGIN;
INSERT INTO `CuocHoiThoai` (`ma_cuochoithoai`, `tieu_de`, `trang_thai`, `thoi_gian_tao`, `ma_tk`) VALUES (1, 'Tư vấn trà sữa Soranchi', 'ACTIVE', '2026-11-23 21:41:16', 2);
INSERT INTO `CuocHoiThoai` (`ma_cuochoithoai`, `tieu_de`, `trang_thai`, `thoi_gian_tao`, `ma_tk`) VALUES (2, 'Tư vấn trà sữa Soranchi', 'ACTIVE', '2026-11-23 23:06:30', 1);
COMMIT;

-- ----------------------------
-- Table structure for DanhGia
-- ----------------------------
DROP TABLE IF EXISTS `DanhGia`;
CREATE TABLE `DanhGia` (
  `ma_dg` int NOT NULL AUTO_INCREMENT,
  `rating` int NOT NULL,
  `nhan_xet` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ngay_dg` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `ma_tk` int NOT NULL,
  `ma_sp` int NOT NULL,
  `ma_ctdh` int NOT NULL,
  PRIMARY KEY (`ma_dg`),
  UNIQUE KEY `ma_ctdh` (`ma_ctdh`),
  KEY `ma_tk` (`ma_tk`),
  KEY `ma_sp` (`ma_sp`),
  CONSTRAINT `danhgia_ibfk_1` FOREIGN KEY (`ma_tk`) REFERENCES `TaiKhoan` (`ma_tk`),
  CONSTRAINT `danhgia_ibfk_2` FOREIGN KEY (`ma_sp`) REFERENCES `SanPham` (`ma_sp`),
  CONSTRAINT `danhgia_ibfk_3` FOREIGN KEY (`ma_ctdh`) REFERENCES `ChiTietDonHang` (`ma_ctdh`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of DanhGia
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for DanhMuc
-- ----------------------------
DROP TABLE IF EXISTS `DanhMuc`;
CREATE TABLE `DanhMuc` (
  `ma_dm` int NOT NULL AUTO_INCREMENT,
  `ten_dm` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`ma_dm`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of DanhMuc
-- ----------------------------
BEGIN;
INSERT INTO `DanhMuc` (`ma_dm`, `ten_dm`) VALUES (1, 'Đồ ăn');
INSERT INTO `DanhMuc` (`ma_dm`, `ten_dm`) VALUES (2, 'Cà phê');
INSERT INTO `DanhMuc` (`ma_dm`, `ten_dm`) VALUES (3, 'Nước ép');
INSERT INTO `DanhMuc` (`ma_dm`, `ten_dm`) VALUES (4, 'Trà sữa');
INSERT INTO `DanhMuc` (`ma_dm`, `ten_dm`) VALUES (5, 'Trà trái cây');
COMMIT;

-- ----------------------------
-- Table structure for DonHang
-- ----------------------------
DROP TABLE IF EXISTS `DonHang`;
CREATE TABLE `DonHang` (
  `ma_donhang` int NOT NULL AUTO_INCREMENT,
  `ten_nguoi_nhan` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `sdt` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `dia_chi` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `tong_gia` double NOT NULL,
  `tinh_trang_dh` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `ngay_dathang` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `ma_tk` int NOT NULL,
  `ma_ptvc` int NOT NULL,
  `ma_km` int DEFAULT NULL,
  PRIMARY KEY (`ma_donhang`),
  KEY `ma_tk` (`ma_tk`),
  KEY `ma_ptvc` (`ma_ptvc`),
  KEY `ma_km` (`ma_km`),
  CONSTRAINT `donhang_ibfk_1` FOREIGN KEY (`ma_tk`) REFERENCES `TaiKhoan` (`ma_tk`),
  CONSTRAINT `donhang_ibfk_2` FOREIGN KEY (`ma_ptvc`) REFERENCES `PhuongThucVanChuyen` (`ma_ptvc`),
  CONSTRAINT `donhang_ibfk_3` FOREIGN KEY (`ma_km`) REFERENCES `KhuyenMai` (`ma_km`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of DonHang
-- ----------------------------
BEGIN;
INSERT INTO `DonHang` (`ma_donhang`, `ten_nguoi_nhan`, `sdt`, `dia_chi`, `tong_gia`, `tinh_trang_dh`, `ngay_dathang`, `ma_tk`, `ma_ptvc`, `ma_km`) VALUES (2, 'Mat troi nho', '0912345671', '12 Xuân Thủy, Cầu Giấy, Hà Nội', 158000, 'HOÀN THÀNH', '2026-11-23 22:23:24', 2, 1, NULL);
INSERT INTO `DonHang` (`ma_donhang`, `ten_nguoi_nhan`, `sdt`, `dia_chi`, `tong_gia`, `tinh_trang_dh`, `ngay_dathang`, `ma_tk`, `ma_ptvc`, `ma_km`) VALUES (3, 'Mat troi nho', '0912345671', '12 Xuân Thủy, Cầu Giấy, Hà Nội', 91000, 'ĐÃ THANH TOÁN', '2026-11-23 22:23:46', 2, 1, NULL);
INSERT INTO `DonHang` (`ma_donhang`, `ten_nguoi_nhan`, `sdt`, `dia_chi`, `tong_gia`, `tinh_trang_dh`, `ngay_dathang`, `ma_tk`, `ma_ptvc`, `ma_km`) VALUES (4, 'Mat troi nho', '0912345671', '12 Xuân Thủy, Cầu Giấy, Hà Nội', 136000, 'ĐÃ THANH TOÁN', '2026-11-23 22:48:26', 2, 1, NULL);
COMMIT;

-- ----------------------------
-- Table structure for GiaoDichThanhToan
-- ----------------------------
DROP TABLE IF EXISTS `GiaoDichThanhToan`;
CREATE TABLE `GiaoDichThanhToan` (
  `ma_giaodich` int NOT NULL AUTO_INCREMENT,
  `so_tien` double NOT NULL,
  `thoi_gian` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `trang_thai` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `ma_phanhoi` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ma_donhang` int NOT NULL,
  `ma_pttt` int NOT NULL,
  PRIMARY KEY (`ma_giaodich`),
  KEY `ma_donhang` (`ma_donhang`),
  KEY `ma_pttt` (`ma_pttt`),
  CONSTRAINT `giaodichthanhtoan_ibfk_1` FOREIGN KEY (`ma_donhang`) REFERENCES `DonHang` (`ma_donhang`),
  CONSTRAINT `giaodichthanhtoan_ibfk_2` FOREIGN KEY (`ma_pttt`) REFERENCES `PhuongThucThanhToan` (`ma_pttt`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of GiaoDichThanhToan
-- ----------------------------
BEGIN;
INSERT INTO `GiaoDichThanhToan` (`ma_giaodich`, `so_tien`, `thoi_gian`, `trang_thai`, `ma_phanhoi`, `ma_donhang`, `ma_pttt`) VALUES (1, 158000, '2026-11-23 22:23:24', 'SUCCESS', 'TRANS_1795447403786', 2, 1);
INSERT INTO `GiaoDichThanhToan` (`ma_giaodich`, `so_tien`, `thoi_gian`, `trang_thai`, `ma_phanhoi`, `ma_donhang`, `ma_pttt`) VALUES (2, 91000, '2026-11-23 22:23:57', 'SUCCESS', 'TRANS_1795447426339', 3, 2);
INSERT INTO `GiaoDichThanhToan` (`ma_giaodich`, `so_tien`, `thoi_gian`, `trang_thai`, `ma_phanhoi`, `ma_donhang`, `ma_pttt`) VALUES (3, 136000, '2026-11-23 22:48:48', 'SUCCESS', 'TRANS_1795448905714', 4, 2);
COMMIT;

-- ----------------------------
-- Table structure for GioHang
-- ----------------------------
DROP TABLE IF EXISTS `GioHang`;
CREATE TABLE `GioHang` (
  `ma_gh` int NOT NULL AUTO_INCREMENT,
  `ngay_tao` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `ma_tk` int NOT NULL,
  PRIMARY KEY (`ma_gh`),
  UNIQUE KEY `ma_tk` (`ma_tk`),
  CONSTRAINT `giohang_ibfk_1` FOREIGN KEY (`ma_tk`) REFERENCES `TaiKhoan` (`ma_tk`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of GioHang
-- ----------------------------
BEGIN;
INSERT INTO `GioHang` (`ma_gh`, `ngay_tao`, `ma_tk`) VALUES (1, '2026-11-23 21:44:20', 2);
COMMIT;

-- ----------------------------
-- Table structure for HinhAnh
-- ----------------------------
DROP TABLE IF EXISTS `HinhAnh`;
CREATE TABLE `HinhAnh` (
  `ma_hinhanh` int NOT NULL AUTO_INCREMENT,
  `hinhanh_url` varchar(1000) COLLATE utf8mb4_unicode_ci NOT NULL,
  `ma_sp` int NOT NULL,
  PRIMARY KEY (`ma_hinhanh`),
  KEY `ma_sp` (`ma_sp`),
  CONSTRAINT `hinhanh_ibfk_1` FOREIGN KEY (`ma_sp`) REFERENCES `SanPham` (`ma_sp`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=63 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of HinhAnh
-- ----------------------------
BEGIN;
INSERT INTO `HinhAnh` (`ma_hinhanh`, `hinhanh_url`, `ma_sp`) VALUES (1, 'DoAn/BanhKemCuon.jpg', 1);
INSERT INTO `HinhAnh` (`ma_hinhanh`, `hinhanh_url`, `ma_sp`) VALUES (2, 'DoAn/BanhSuKem.jpg', 2);
INSERT INTO `HinhAnh` (`ma_hinhanh`, `hinhanh_url`, `ma_sp`) VALUES (3, 'DoAn/BanhTrangCuon.jpg', 3);
INSERT INTO `HinhAnh` (`ma_hinhanh`, `hinhanh_url`, `ma_sp`) VALUES (4, 'DoAn/BanhTrangNuong.webp', 4);
INSERT INTO `HinhAnh` (`ma_hinhanh`, `hinhanh_url`, `ma_sp`) VALUES (5, 'DoAn/BanhTrangTron.jpg', 5);
INSERT INTO `HinhAnh` (`ma_hinhanh`, `hinhanh_url`, `ma_sp`) VALUES (6, 'DoAn/CheesecakeCam.jpg', 6);
INSERT INTO `HinhAnh` (`ma_hinhanh`, `hinhanh_url`, `ma_sp`) VALUES (7, 'DoAn/Croissant.jpg', 7);
INSERT INTO `HinhAnh` (`ma_hinhanh`, `hinhanh_url`, `ma_sp`) VALUES (8, 'DoAn/HokkaidoYuKi_BanhPhoMaiNhatBan.jpg', 8);
INSERT INTO `HinhAnh` (`ma_hinhanh`, `hinhanh_url`, `ma_sp`) VALUES (9, 'DoAn/HuongDuong.jpg', 9);
INSERT INTO `HinhAnh` (`ma_hinhanh`, `hinhanh_url`, `ma_sp`) VALUES (11, 'DoAn/MetAnVat.jpg', 11);
INSERT INTO `HinhAnh` (`ma_hinhanh`, `hinhanh_url`, `ma_sp`) VALUES (12, 'DoAn/MousseDauTay.jpg', 12);
INSERT INTO `HinhAnh` (`ma_hinhanh`, `hinhanh_url`, `ma_sp`) VALUES (13, 'DoAn/MousseXoaiChanhLeo.jpg', 13);
INSERT INTO `HinhAnh` (`ma_hinhanh`, `hinhanh_url`, `ma_sp`) VALUES (14, 'DoAn/NemChuaRan.jpg', 14);
INSERT INTO `HinhAnh` (`ma_hinhanh`, `hinhanh_url`, `ma_sp`) VALUES (15, 'DoAn/NgoChien.jpg', 15);
INSERT INTO `HinhAnh` (`ma_hinhanh`, `hinhanh_url`, `ma_sp`) VALUES (16, 'DoAn/SnackCacLoai.jpg', 16);
INSERT INTO `HinhAnh` (`ma_hinhanh`, `hinhanh_url`, `ma_sp`) VALUES (17, 'DoAn/StrawberryCake.jpg', 17);
INSERT INTO `HinhAnh` (`ma_hinhanh`, `hinhanh_url`, `ma_sp`) VALUES (18, 'DoAn/Tiramisu.jpg', 18);
INSERT INTO `HinhAnh` (`ma_hinhanh`, `hinhanh_url`, `ma_sp`) VALUES (19, 'DoAn/XucXich.webp', 19);
INSERT INTO `HinhAnh` (`ma_hinhanh`, `hinhanh_url`, `ma_sp`) VALUES (20, 'DoUong/Caphe/BacXiu.jpg', 20);
INSERT INTO `HinhAnh` (`ma_hinhanh`, `hinhanh_url`, `ma_sp`) VALUES (21, 'DoUong/Caphe/CacaoNong.jpg', 21);
INSERT INTO `HinhAnh` (`ma_hinhanh`, `hinhanh_url`, `ma_sp`) VALUES (22, 'DoUong/Caphe/CapheKemTrungMuoi.jpg', 22);
INSERT INTO `HinhAnh` (`ma_hinhanh`, `hinhanh_url`, `ma_sp`) VALUES (23, 'DoUong/Caphe/CaramelAmericano.jpg', 23);
INSERT INTO `HinhAnh` (`ma_hinhanh`, `hinhanh_url`, `ma_sp`) VALUES (24, 'DoUong/Caphe/ColBrewMo.jpg', 24);
INSERT INTO `HinhAnh` (`ma_hinhanh`, `hinhanh_url`, `ma_sp`) VALUES (25, 'DoUong/Caphe/ColdBrewCam.jpg', 25);
INSERT INTO `HinhAnh` (`ma_hinhanh`, `hinhanh_url`, `ma_sp`) VALUES (26, 'DoUong/Caphe/Conan.jpg', 26);
INSERT INTO `HinhAnh` (`ma_hinhanh`, `hinhanh_url`, `ma_sp`) VALUES (27, 'DoUong/Caphe/DenDa.webp', 27);
INSERT INTO `HinhAnh` (`ma_hinhanh`, `hinhanh_url`, `ma_sp`) VALUES (28, 'DoUong/Caphe/Mocha_Espresso.jpg', 28);
INSERT INTO `HinhAnh` (`ma_hinhanh`, `hinhanh_url`, `ma_sp`) VALUES (29, 'DoUong/Caphe/SoranCoffee.jpg', 29);
INSERT INTO `HinhAnh` (`ma_hinhanh`, `hinhanh_url`, `ma_sp`) VALUES (30, 'DoUong/Caphe/SuaDa.jpg', 30);
INSERT INTO `HinhAnh` (`ma_hinhanh`, `hinhanh_url`, `ma_sp`) VALUES (31, 'DoUong/NuocEp/BoGiaDuaNon.jpg', 31);
INSERT INTO `HinhAnh` (`ma_hinhanh`, `hinhanh_url`, `ma_sp`) VALUES (32, 'DoUong/NuocEp/ChanhTuoiMatLanh.png', 32);
INSERT INTO `HinhAnh` (`ma_hinhanh`, `hinhanh_url`, `ma_sp`) VALUES (33, 'DoUong/NuocEp/EpDua.jpg', 33);
INSERT INTO `HinhAnh` (`ma_hinhanh`, `hinhanh_url`, `ma_sp`) VALUES (34, 'DoUong/NuocEp/MangoSmoothie.jpg', 34);
INSERT INTO `HinhAnh` (`ma_hinhanh`, `hinhanh_url`, `ma_sp`) VALUES (35, 'DoUong/NuocEp/NuocEpNhaDam.webp', 35);
INSERT INTO `HinhAnh` (`ma_hinhanh`, `hinhanh_url`, `ma_sp`) VALUES (36, 'DoUong/NuocEp/SinhToBo.jpg', 36);
INSERT INTO `HinhAnh` (`ma_hinhanh`, `hinhanh_url`, `ma_sp`) VALUES (37, 'DoUong/NuocEp/SinhToDau.jpg', 37);
INSERT INTO `HinhAnh` (`ma_hinhanh`, `hinhanh_url`, `ma_sp`) VALUES (38, 'DoUong/NuocEp/SinhToLuu.webp', 38);
INSERT INTO `HinhAnh` (`ma_hinhanh`, `hinhanh_url`, `ma_sp`) VALUES (39, 'DoUong/NuocEp/SinhToMangCau.jpg', 39);
INSERT INTO `HinhAnh` (`ma_hinhanh`, `hinhanh_url`, `ma_sp`) VALUES (40, 'DoUong/NuocEp/SinhToTao.png', 40);
INSERT INTO `HinhAnh` (`ma_hinhanh`, `hinhanh_url`, `ma_sp`) VALUES (41, 'DoUong/TraSua/CinnamonMatcha.jpg', 41);
INSERT INTO `HinhAnh` (`ma_hinhanh`, `hinhanh_url`, `ma_sp`) VALUES (42, 'DoUong/TraSua/MatchaKemMuoi.jpg', 42);
INSERT INTO `HinhAnh` (`ma_hinhanh`, `hinhanh_url`, `ma_sp`) VALUES (43, 'DoUong/TraSua/MatchaLatte.jpg', 43);
INSERT INTO `HinhAnh` (`ma_hinhanh`, `hinhanh_url`, `ma_sp`) VALUES (44, 'DoUong/TraSua/MatchaSuaGao.jpg', 44);
INSERT INTO `HinhAnh` (`ma_hinhanh`, `hinhanh_url`, `ma_sp`) VALUES (45, 'DoUong/TraSua/OLongNhaiSua.jpeg', 45);
INSERT INTO `HinhAnh` (`ma_hinhanh`, `hinhanh_url`, `ma_sp`) VALUES (46, 'DoUong/TraSua/OLongTuQuy.jpg', 46);
INSERT INTO `HinhAnh` (`ma_hinhanh`, `hinhanh_url`, `ma_sp`) VALUES (47, 'DoUong/TraSua/OreoChocolateCream.jpg', 47);
INSERT INTO `HinhAnh` (`ma_hinhanh`, `hinhanh_url`, `ma_sp`) VALUES (48, 'DoUong/TraSua/TraDuongNhan.jpg', 48);
INSERT INTO `HinhAnh` (`ma_hinhanh`, `hinhanh_url`, `ma_sp`) VALUES (49, 'DoUong/TraTraiCay/TraCamQue.jpg', 49);
INSERT INTO `HinhAnh` (`ma_hinhanh`, `hinhanh_url`, `ma_sp`) VALUES (50, 'DoUong/TraTraiCay/TraDaoCamXa.jpg', 50);
INSERT INTO `HinhAnh` (`ma_hinhanh`, `hinhanh_url`, `ma_sp`) VALUES (51, 'DoUong/TraTraiCay/TraHoaQuaNhietDoi.jpg', 51);
INSERT INTO `HinhAnh` (`ma_hinhanh`, `hinhanh_url`, `ma_sp`) VALUES (52, 'DoUong/TraTraiCay/TraMamXoi.jpg', 52);
INSERT INTO `HinhAnh` (`ma_hinhanh`, `hinhanh_url`, `ma_sp`) VALUES (53, 'DoUong/TraTraiCay/TraMan.jpg', 53);
INSERT INTO `HinhAnh` (`ma_hinhanh`, `hinhanh_url`, `ma_sp`) VALUES (54, 'DoUong/TraTraiCay/TraMoSoRiHatChia.jpg', 54);
INSERT INTO `HinhAnh` (`ma_hinhanh`, `hinhanh_url`, `ma_sp`) VALUES (55, 'DoUong/TraTraiCay/TraNhaiQuyt.jpg', 55);
INSERT INTO `HinhAnh` (`ma_hinhanh`, `hinhanh_url`, `ma_sp`) VALUES (56, 'DoUong/TraTraiCay/TraNhan.jpg', 56);
INSERT INTO `HinhAnh` (`ma_hinhanh`, `hinhanh_url`, `ma_sp`) VALUES (57, 'DoUong/TraTraiCay/TraOi.jpg', 57);
INSERT INTO `HinhAnh` (`ma_hinhanh`, `hinhanh_url`, `ma_sp`) VALUES (58, 'DoUong/TraTraiCay/TraVaiDuaHau.jpg', 58);
INSERT INTO `HinhAnh` (`ma_hinhanh`, `hinhanh_url`, `ma_sp`) VALUES (59, 'DoUong/TraTraiCay/TraVaiHoaHong.jpg', 59);
INSERT INTO `HinhAnh` (`ma_hinhanh`, `hinhanh_url`, `ma_sp`) VALUES (60, 'DoUong/TraTraiCay/TraVaiNhaDam.jpg', 60);
INSERT INTO `HinhAnh` (`ma_hinhanh`, `hinhanh_url`, `ma_sp`) VALUES (61, 'DoUong/TraTraiCay/TraXoaiKemChesse.jpg', 61);
INSERT INTO `HinhAnh` (`ma_hinhanh`, `hinhanh_url`, `ma_sp`) VALUES (62, 'DoUong/TraTraiCay/TraXoaiMachiato.jpg', 62);
COMMIT;

-- ----------------------------
-- Table structure for KhuyenMai
-- ----------------------------
DROP TABLE IF EXISTS `KhuyenMai`;
CREATE TABLE `KhuyenMai` (
  `ma_km` int NOT NULL AUTO_INCREMENT,
  `code` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `ten_km` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `mo_ta` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `phan_tram_giam_gia` int NOT NULL,
  `ngay_bat_dau` datetime NOT NULL,
  `ngay_ket_thuc` datetime NOT NULL,
  `tinh_trang` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`ma_km`),
  UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of KhuyenMai
-- ----------------------------
BEGIN;
INSERT INTO `KhuyenMai` (`ma_km`, `code`, `ten_km`, `mo_ta`, `phan_tram_giam_gia`, `ngay_bat_dau`, `ngay_ket_thuc`, `tinh_trang`) VALUES (1, 'TEST1', 'Giảm giá mùa tựu trường', 'Giảm giá mùa tựu trường', 20, '2026-11-23 00:00:00', '2026-12-06 23:59:59', 1);
INSERT INTO `KhuyenMai` (`ma_km`, `code`, `ten_km`, `mo_ta`, `phan_tram_giam_gia`, `ngay_bat_dau`, `ngay_ket_thuc`, `tinh_trang`) VALUES (2, 'NAMMOI20', 'Giảm giá năm mới', 'Giảm giá năm mới', 50, '2026-11-24 00:00:00', '2026-12-18 23:59:59', 1);
INSERT INTO `KhuyenMai` (`ma_km`, `code`, `ten_km`, `mo_ta`, `phan_tram_giam_gia`, `ngay_bat_dau`, `ngay_ket_thuc`, `tinh_trang`) VALUES (3, 'SORANCHI50', 'SORANCHI50', 'SORANCHI50', 1, '2026-11-02 00:00:00', '2026-11-17 23:59:59', 1);
INSERT INTO `KhuyenMai` (`ma_km`, `code`, `ten_km`, `mo_ta`, `phan_tram_giam_gia`, `ngay_bat_dau`, `ngay_ket_thuc`, `tinh_trang`) VALUES (4, 'D', 'd', 'd', 55, '2026-11-19 00:00:00', '2026-12-02 23:59:59', 1);
COMMIT;

-- ----------------------------
-- Table structure for KichCo
-- ----------------------------
DROP TABLE IF EXISTS `KichCo`;
CREATE TABLE `KichCo` (
  `ma_size` int NOT NULL AUTO_INCREMENT,
  `ten_size` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `gia_phu_thu` double NOT NULL DEFAULT '0',
  PRIMARY KEY (`ma_size`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of KichCo
-- ----------------------------
BEGIN;
INSERT INTO `KichCo` (`ma_size`, `ten_size`, `gia_phu_thu`) VALUES (1, 'S', 0);
INSERT INTO `KichCo` (`ma_size`, `ten_size`, `gia_phu_thu`) VALUES (2, 'M', 5000);
INSERT INTO `KichCo` (`ma_size`, `ten_size`, `gia_phu_thu`) VALUES (3, 'L', 10000);
COMMIT;

-- ----------------------------
-- Table structure for PhuongThucThanhToan
-- ----------------------------
DROP TABLE IF EXISTS `PhuongThucThanhToan`;
CREATE TABLE `PhuongThucThanhToan` (
  `ma_pttt` int NOT NULL AUTO_INCREMENT,
  `ten_pttt` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `trang_thai` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'ACTIVE',
  PRIMARY KEY (`ma_pttt`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of PhuongThucThanhToan
-- ----------------------------
BEGIN;
INSERT INTO `PhuongThucThanhToan` (`ma_pttt`, `ten_pttt`, `trang_thai`) VALUES (1, 'Thanh toán khi nhận hàng (COD)', 'HOẠT ĐỘNG');
INSERT INTO `PhuongThucThanhToan` (`ma_pttt`, `ten_pttt`, `trang_thai`) VALUES (2, 'Thanh toán trực tuyến (VietQR)', 'HOẠT ĐỘNG');
COMMIT;

-- ----------------------------
-- Table structure for PhuongThucVanChuyen
-- ----------------------------
DROP TABLE IF EXISTS `PhuongThucVanChuyen`;
CREATE TABLE `PhuongThucVanChuyen` (
  `ma_ptvc` int NOT NULL AUTO_INCREMENT,
  `ten_ptvc` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `phi_vanchuyen` double NOT NULL,
  `thoi_gian_du_kien` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `mo_ta` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `trang_thai` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'ACTIVE',
  PRIMARY KEY (`ma_ptvc`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of PhuongThucVanChuyen
-- ----------------------------
BEGIN;
INSERT INTO `PhuongThucVanChuyen` (`ma_ptvc`, `ten_ptvc`, `phi_vanchuyen`, `thoi_gian_du_kien`, `mo_ta`, `trang_thai`) VALUES (1, 'Giao hàng tiêu chuẩn', 15000, '30 - 45 phút', 'Giao hàng nội thành tiêu chuẩn', 'HOẠT ĐỘNG');
INSERT INTO `PhuongThucVanChuyen` (`ma_ptvc`, `ten_ptvc`, `phi_vanchuyen`, `thoi_gian_du_kien`, `mo_ta`, `trang_thai`) VALUES (2, 'Giao hàng hỏa tốc (GrabExpress)', 25000, '15 - 25 phút', 'Giao ngay trong vòng 25 phút', 'HOẠT ĐỘNG');
COMMIT;

-- ----------------------------
-- Table structure for SanPham
-- ----------------------------
DROP TABLE IF EXISTS `SanPham`;
CREATE TABLE `SanPham` (
  `ma_sp` int NOT NULL AUTO_INCREMENT,
  `ten_sp` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `gia` double NOT NULL,
  `mo_ta` text COLLATE utf8mb4_unicode_ci,
  `so_luong_ton` int NOT NULL DEFAULT '0',
  `trang_thai` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'ACTIVE',
  `ma_dm` int NOT NULL,
  PRIMARY KEY (`ma_sp`),
  KEY `ma_dm` (`ma_dm`),
  CONSTRAINT `sanpham_ibfk_1` FOREIGN KEY (`ma_dm`) REFERENCES `DanhMuc` (`ma_dm`)
) ENGINE=InnoDB AUTO_INCREMENT=63 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of SanPham
-- ----------------------------
BEGIN;
INSERT INTO `SanPham` (`ma_sp`, `ten_sp`, `gia`, `mo_ta`, `so_luong_ton`, `trang_thai`, `ma_dm`) VALUES (1, 'Bánh kem cuộn', 28000, 'Bánh bông lan cuộn kem tươi mềm mịn, ngọt thanh', 50, 'ĐANG BÁN', 1);
INSERT INTO `SanPham` (`ma_sp`, `ten_sp`, `gia`, `mo_ta`, `so_luong_ton`, `trang_thai`, `ma_dm`) VALUES (2, 'Bánh su kem', 25000, 'Vỏ bánh giòn nhẹ nhân kem vani béo ngậy', 48, 'ĐANG BÁN', 1);
INSERT INTO `SanPham` (`ma_sp`, `ten_sp`, `gia`, `mo_ta`, `so_luong_ton`, `trang_thai`, `ma_dm`) VALUES (3, 'Bánh tráng cuốn', 22000, 'Bánh tráng cuốn sốt me, trứng cút và khô bò đậm đà', 50, 'ĐANG BÁN', 1);
INSERT INTO `SanPham` (`ma_sp`, `ten_sp`, `gia`, `mo_ta`, `so_luong_ton`, `trang_thai`, `ma_dm`) VALUES (4, 'Bánh tráng nướng', 25000, 'Bánh tráng nướng giòn rụm đầy ắp topping', 50, 'ĐANG BÁN', 1);
INSERT INTO `SanPham` (`ma_sp`, `ten_sp`, `gia`, `mo_ta`, `so_luong_ton`, `trang_thai`, `ma_dm`) VALUES (5, 'Bánh tráng trộn', 22000, 'Bánh tráng trộn chua cay mặn ngọt chuẩn vị', 50, 'ĐANG BÁN', 1);
INSERT INTO `SanPham` (`ma_sp`, `ten_sp`, `gia`, `mo_ta`, `so_luong_ton`, `trang_thai`, `ma_dm`) VALUES (6, 'Cheesecake cam', 38000, 'Bánh phô mai lạnh vị cam tươi mát dịu', 48, 'ĐANG BÁN', 1);
INSERT INTO `SanPham` (`ma_sp`, `ten_sp`, `gia`, `mo_ta`, `so_luong_ton`, `trang_thai`, `ma_dm`) VALUES (7, 'Bánh sừng bò Croissant', 30000, 'Bánh sừng bò ngàn lớp nướng bơ Pháp thơm lừng', 50, 'ĐANG BÁN', 1);
INSERT INTO `SanPham` (`ma_sp`, `ten_sp`, `gia`, `mo_ta`, `so_luong_ton`, `trang_thai`, `ma_dm`) VALUES (8, 'Hokkaido Yuki - Bánh phô mai Nhật Bản', 42000, 'Bánh phô mai tan chảy chuẩn vị Hokkaido', 49, 'ĐANG BÁN', 1);
INSERT INTO `SanPham` (`ma_sp`, `ten_sp`, `gia`, `mo_ta`, `so_luong_ton`, `trang_thai`, `ma_dm`) VALUES (9, 'Hạt hướng dương', 15000, 'Hạt hướng dương rang giòn thơm bùi', 100, 'ĐANG BÁN', 1);
INSERT INTO `SanPham` (`ma_sp`, `ten_sp`, `gia`, `mo_ta`, `so_luong_ton`, `trang_thai`, `ma_dm`) VALUES (11, 'Mẹt ăn vặt tổng hợp', 55000, 'Mẹt ăn vặt đầy đủ nem chua rán, xúc xích, khoai chiên', 40, 'ĐANG BÁN', 1);
INSERT INTO `SanPham` (`ma_sp`, `ten_sp`, `gia`, `mo_ta`, `so_luong_ton`, `trang_thai`, `ma_dm`) VALUES (12, 'Mousse dâu tây', 38000, 'Bánh mousse dâu tây chua ngọt thanh mát', 50, 'ĐANG BÁN', 1);
INSERT INTO `SanPham` (`ma_sp`, `ten_sp`, `gia`, `mo_ta`, `so_luong_ton`, `trang_thai`, `ma_dm`) VALUES (13, 'Mousse xoài chanh leo', 38000, 'Bánh mousse nhiệt đới vị xoài và chanh leo', 50, 'ĐANG BÁN', 1);
INSERT INTO `SanPham` (`ma_sp`, `ten_sp`, `gia`, `mo_ta`, `so_luong_ton`, `trang_thai`, `ma_dm`) VALUES (14, 'Nem chua rán', 30000, 'Nem chua rán vàng giòn kèm tương ớt cay nồng', 60, 'ĐANG BÁN', 1);
INSERT INTO `SanPham` (`ma_sp`, `ten_sp`, `gia`, `mo_ta`, `so_luong_ton`, `trang_thai`, `ma_dm`) VALUES (15, 'Ngô chiên bơ', 25000, 'Ngô ngọt chiên bơ giòn rụm thơm phức', 60, 'ĐANG BÁN', 1);
INSERT INTO `SanPham` (`ma_sp`, `ten_sp`, `gia`, `mo_ta`, `so_luong_ton`, `trang_thai`, `ma_dm`) VALUES (16, 'Snack các loại', 18000, 'Combo snack giòn tan nhâm nhi cùng trà sữa', 80, 'ĐANG BÁN', 1);
INSERT INTO `SanPham` (`ma_sp`, `ten_sp`, `gia`, `mo_ta`, `so_luong_ton`, `trang_thai`, `ma_dm`) VALUES (17, 'Bánh kem dâu tây', 40000, 'Bánh kem tươi phủ dâu tây Đà Lạt mọng nước', 45, 'ĐANG BÁN', 1);
INSERT INTO `SanPham` (`ma_sp`, `ten_sp`, `gia`, `mo_ta`, `so_luong_ton`, `trang_thai`, `ma_dm`) VALUES (18, 'Tiramisu', 39000, 'Bánh Tiramisu vị cà phê cacao Ý quyến rũ', 50, 'ĐANG BÁN', 1);
INSERT INTO `SanPham` (`ma_sp`, `ten_sp`, `gia`, `mo_ta`, `so_luong_ton`, `trang_thai`, `ma_dm`) VALUES (19, 'Xúc xích rán', 20000, 'Xúc xích Đức rán giòn thơm ngon', 70, 'ĐANG BÁN', 1);
INSERT INTO `SanPham` (`ma_sp`, `ten_sp`, `gia`, `mo_ta`, `so_luong_ton`, `trang_thai`, `ma_dm`) VALUES (20, 'Bạc xỉu', 29000, 'Cà phê sữa nhiều sữa tươi béo ngậy, dịu nhẹ', 60, 'ĐANG BÁN', 2);
INSERT INTO `SanPham` (`ma_sp`, `ten_sp`, `gia`, `mo_ta`, `so_luong_ton`, `trang_thai`, `ma_dm`) VALUES (21, 'Cacao nóng', 32000, 'Cacao nguyên chất pha nóng ấm áp, đậm đà', 60, 'ĐANG BÁN', 2);
INSERT INTO `SanPham` (`ma_sp`, `ten_sp`, `gia`, `mo_ta`, `so_luong_ton`, `trang_thai`, `ma_dm`) VALUES (22, 'Cà phê kem trứng muối', 39000, 'Cà phê phin đậm vị phủ lớp kem trứng muối béo mặn', 60, 'ĐANG BÁN', 2);
INSERT INTO `SanPham` (`ma_sp`, `ten_sp`, `gia`, `mo_ta`, `so_luong_ton`, `trang_thai`, `ma_dm`) VALUES (23, 'Caramel Americano', 35000, 'Espresso pha loãng thanh mát kết hợp sốt caramel', 60, 'ĐANG BÁN', 2);
INSERT INTO `SanPham` (`ma_sp`, `ten_sp`, `gia`, `mo_ta`, `so_luong_ton`, `trang_thai`, `ma_dm`) VALUES (24, 'Cold Brew mơ', 38000, 'Cà phê ủ lạnh kết hợp vị mơ ngâm chua ngọt thanh mát', 56, 'ĐANG BÁN', 2);
INSERT INTO `SanPham` (`ma_sp`, `ten_sp`, `gia`, `mo_ta`, `so_luong_ton`, `trang_thai`, `ma_dm`) VALUES (25, 'Cold Brew cam', 38000, 'Cà phê ủ lạnh thanh khiết hòa quyện cùng cam vàng tươi', 60, 'ĐANG BÁN', 2);
INSERT INTO `SanPham` (`ma_sp`, `ten_sp`, `gia`, `mo_ta`, `so_luong_ton`, `trang_thai`, `ma_dm`) VALUES (26, 'Cà phê cốt dừa Conan', 39000, 'Cà phê xay cùng cốt dừa béo mịn thơm lừng', 60, 'ĐANG BÁN', 2);
INSERT INTO `SanPham` (`ma_sp`, `ten_sp`, `gia`, `mo_ta`, `so_luong_ton`, `trang_thai`, `ma_dm`) VALUES (27, 'Cà phê đen đá', 25000, 'Cà phê phin truyền thống đậm đà tỉnh táo', 80, 'ĐANG BÁN', 2);
INSERT INTO `SanPham` (`ma_sp`, `ten_sp`, `gia`, `mo_ta`, `so_luong_ton`, `trang_thai`, `ma_dm`) VALUES (28, 'Mocha Espresso', 38000, 'Sự kết hợp hài hòa giữa Espresso, sữa nóng và socola', 60, 'ĐANG BÁN', 2);
INSERT INTO `SanPham` (`ma_sp`, `ten_sp`, `gia`, `mo_ta`, `so_luong_ton`, `trang_thai`, `ma_dm`) VALUES (29, 'Soran Coffee đặc biệt', 36000, 'Công thức cà phê độc quyền tại Soranchi', 60, 'ĐANG BÁN', 2);
INSERT INTO `SanPham` (`ma_sp`, `ten_sp`, `gia`, `mo_ta`, `so_luong_ton`, `trang_thai`, `ma_dm`) VALUES (30, 'Cà phê sữa đá', 27000, 'Cà phê phin hòa quyện sữa đặc ngọt ngào truyền thống', 80, 'ĐANG BÁN', 2);
INSERT INTO `SanPham` (`ma_sp`, `ten_sp`, `gia`, `mo_ta`, `so_luong_ton`, `trang_thai`, `ma_dm`) VALUES (31, 'Bơ già dừa non', 42000, 'Sinh tố bơ sáp dẻo mịn kết hợp dừa non giòn sần sật', 50, 'ĐANG BÁN', 3);
INSERT INTO `SanPham` (`ma_sp`, `ten_sp`, `gia`, `mo_ta`, `so_luong_ton`, `trang_thai`, `ma_dm`) VALUES (32, 'Chanh tươi mát lạnh', 25000, 'Nước chanh tươi vắt giải nhiệt sảng khoái tức thì', 70, 'ĐANG BÁN', 3);
INSERT INTO `SanPham` (`ma_sp`, `ten_sp`, `gia`, `mo_ta`, `so_luong_ton`, `trang_thai`, `ma_dm`) VALUES (33, 'Nước ép dứa', 32000, 'Nước ép dứa tươi nguyên chất giàu vitamin C', 60, 'ĐANG BÁN', 3);
INSERT INTO `SanPham` (`ma_sp`, `ten_sp`, `gia`, `mo_ta`, `so_luong_ton`, `trang_thai`, `ma_dm`) VALUES (34, 'Sinh tố xoài (Mango Smoothie)', 38000, 'Xoài chín xay mịn mát lạnh, ngọt thơm tự nhiên', 50, 'ĐANG BÁN', 3);
INSERT INTO `SanPham` (`ma_sp`, `ten_sp`, `gia`, `mo_ta`, `so_luong_ton`, `trang_thai`, `ma_dm`) VALUES (35, 'Nước ép nha đam', 30000, 'Nước ép nha đam thanh mát, đẹp da giải nhiệt', 60, 'ĐANG BÁN', 3);
INSERT INTO `SanPham` (`ma_sp`, `ten_sp`, `gia`, `mo_ta`, `so_luong_ton`, `trang_thai`, `ma_dm`) VALUES (36, 'Sinh tố bơ', 39000, 'Bơ sáp Đắk Lắk xay sánh mịn, béo thơm tự nhiên', 50, 'ĐANG BÁN', 3);
INSERT INTO `SanPham` (`ma_sp`, `ten_sp`, `gia`, `mo_ta`, `so_luong_ton`, `trang_thai`, `ma_dm`) VALUES (37, 'Sinh tố dâu', 39000, 'Dâu tây tươi xay nhuyễn chua ngọt hấp dẫn', 50, 'ĐANG BÁN', 3);
INSERT INTO `SanPham` (`ma_sp`, `ten_sp`, `gia`, `mo_ta`, `so_luong_ton`, `trang_thai`, `ma_dm`) VALUES (38, 'Sinh tố lựu', 40000, 'Nước ép & sinh tố lựu đỏ tươi mát, chống oxy hóa', 50, 'ĐANG BÁN', 3);
INSERT INTO `SanPham` (`ma_sp`, `ten_sp`, `gia`, `mo_ta`, `so_luong_ton`, `trang_thai`, `ma_dm`) VALUES (39, 'Sinh tố mãng cầu', 38000, 'Mãng cầu xiêm xay chua ngọt thanh mát giải nhiệt', 50, 'ĐANG BÁN', 3);
INSERT INTO `SanPham` (`ma_sp`, `ten_sp`, `gia`, `mo_ta`, `so_luong_ton`, `trang_thai`, `ma_dm`) VALUES (40, 'Sinh tố táo', 35000, 'Táo tươi xay mịn thanh ngọt, giàu chất xơ', 50, 'ĐANG BÁN', 3);
INSERT INTO `SanPham` (`ma_sp`, `ten_sp`, `gia`, `mo_ta`, `so_luong_ton`, `trang_thai`, `ma_dm`) VALUES (41, 'Trà sữa quế Matcha (Cinnamon Matcha)', 39000, 'Hương quế ấm áp hòa quyện cùng bột trà xanh Nhật Bản', 60, 'ĐANG BÁN', 4);
INSERT INTO `SanPham` (`ma_sp`, `ten_sp`, `gia`, `mo_ta`, `so_luong_ton`, `trang_thai`, `ma_dm`) VALUES (42, 'Matcha kem muối', 42000, 'Trà xanh matcha đậm vị phủ lớp kem phô mai mặn béo', 60, 'ĐANG BÁN', 4);
INSERT INTO `SanPham` (`ma_sp`, `ten_sp`, `gia`, `mo_ta`, `so_luong_ton`, `trang_thai`, `ma_dm`) VALUES (43, 'Matcha Latte', 38000, 'Bột trà xanh nguyên chất kết hợp sữa tươi thanh trùng', 60, 'ĐANG BÁN', 4);
INSERT INTO `SanPham` (`ma_sp`, `ten_sp`, `gia`, `mo_ta`, `so_luong_ton`, `trang_thai`, `ma_dm`) VALUES (44, 'Matcha sữa gấu', 40000, 'Trà xanh matcha pha cùng sữa gấu béo ngậy đặc trưng', 60, 'ĐANG BÁN', 4);
INSERT INTO `SanPham` (`ma_sp`, `ten_sp`, `gia`, `mo_ta`, `so_luong_ton`, `trang_thai`, `ma_dm`) VALUES (45, 'Trà sữa Ô Long nhài', 35000, 'Trà Ô Long ủ hoa nhài thơm ngát kết hợp sữa béo dịu', 70, 'ĐANG BÁN', 4);
INSERT INTO `SanPham` (`ma_sp`, `ten_sp`, `gia`, `mo_ta`, `so_luong_ton`, `trang_thai`, `ma_dm`) VALUES (46, 'Trà sữa Ô Long Tứ Quý', 35000, 'Hương trà Ô Long Tứ Quý thanh tao, hậu vị ngọt sâu', 70, 'ĐANG BÁN', 4);
INSERT INTO `SanPham` (`ma_sp`, `ten_sp`, `gia`, `mo_ta`, `so_luong_ton`, `trang_thai`, `ma_dm`) VALUES (47, 'Trà sữa Oreo Chocolate Cream', 42000, 'Trà sữa socola phủ kem tươi và vụn bánh Oreo giòn tan', 60, 'ĐANG BÁN', 4);
INSERT INTO `SanPham` (`ma_sp`, `ten_sp`, `gia`, `mo_ta`, `so_luong_ton`, `trang_thai`, `ma_dm`) VALUES (48, 'Trà sữa đường nhạn', 36000, 'Trà sữa nấu cùng đường nâu và nhãn nhục thơm ngọt', 60, 'ĐANG BÁN', 4);
INSERT INTO `SanPham` (`ma_sp`, `ten_sp`, `gia`, `mo_ta`, `so_luong_ton`, `trang_thai`, `ma_dm`) VALUES (49, 'Trà cam quế', 35000, 'Trà đen ủ cùng cam vàng và thanh quế thơm dịu', 60, 'ĐANG BÁN', 5);
INSERT INTO `SanPham` (`ma_sp`, `ten_sp`, `gia`, `mo_ta`, `so_luong_ton`, `trang_thai`, `ma_dm`) VALUES (50, 'Trà đào cam sả', 36000, 'Thức uống quốc dân thanh mát từ đào miếng, cam tươi và sả', 80, 'ĐANG BÁN', 5);
INSERT INTO `SanPham` (`ma_sp`, `ten_sp`, `gia`, `mo_ta`, `so_luong_ton`, `trang_thai`, `ma_dm`) VALUES (51, 'Trà hoa quả nhiệt đới', 39000, 'Trà xanh kết hợp các loại trái cây tươi nhiệt đới giòn ngọt', 70, 'ĐANG BÁN', 5);
INSERT INTO `SanPham` (`ma_sp`, `ten_sp`, `gia`, `mo_ta`, `so_luong_ton`, `trang_thai`, `ma_dm`) VALUES (52, 'Trà mâm xôi', 38000, 'Trà thanh mát hòa quyện vị quả mâm xôi đỏ chua ngọt', 60, 'ĐANG BÁN', 5);
INSERT INTO `SanPham` (`ma_sp`, `ten_sp`, `gia`, `mo_ta`, `so_luong_ton`, `trang_thai`, `ma_dm`) VALUES (53, 'Trà mận', 36000, 'Trà ủ mận hậu chua ngọt giòn tan giải nhiệt mùa hè', 60, 'ĐANG BÁN', 5);
INSERT INTO `SanPham` (`ma_sp`, `ten_sp`, `gia`, `mo_ta`, `so_luong_ton`, `trang_thai`, `ma_dm`) VALUES (54, 'Trà mơ sơ ri hạt chia', 38000, 'Trà mơ sơ ri thanh mát kết hợp hạt chia tốt cho sức khỏe', 60, 'ĐANG BÁN', 5);
INSERT INTO `SanPham` (`ma_sp`, `ten_sp`, `gia`, `mo_ta`, `so_luong_ton`, `trang_thai`, `ma_dm`) VALUES (55, 'Trà nhài quýt', 36000, 'Trà xanh hoa nhài thơm dịu kết hợp nước ép quýt tươi', 60, 'ĐANG BÁN', 5);
INSERT INTO `SanPham` (`ma_sp`, `ten_sp`, `gia`, `mo_ta`, `so_luong_ton`, `trang_thai`, `ma_dm`) VALUES (56, 'Trà nhãn', 36000, 'Trà thanh khiết kết hợp cùi nhãn lồng giòn ngọt', 60, 'ĐANG BÁN', 5);
INSERT INTO `SanPham` (`ma_sp`, `ten_sp`, `gia`, `mo_ta`, `so_luong_ton`, `trang_thai`, `ma_dm`) VALUES (57, 'Trà ổi hồng', 35000, 'Trà xanh hòa quyện nước ép ổi hồng thơm lừng, chua ngọt', 70, 'ĐANG BÁN', 5);
INSERT INTO `SanPham` (`ma_sp`, `ten_sp`, `gia`, `mo_ta`, `so_luong_ton`, `trang_thai`, `ma_dm`) VALUES (58, 'Trà vải dưa hấu', 38000, 'Sự kết hợp tươi mát giữa vải thiều ngọt ngào và dưa hấu đỏ', 60, 'ĐANG BÁN', 5);
INSERT INTO `SanPham` (`ma_sp`, `ten_sp`, `gia`, `mo_ta`, `so_luong_ton`, `trang_thai`, `ma_dm`) VALUES (59, 'Trà vải hoa hồng', 38000, 'Trà hương hoa hồng quyến rũ kết hợp trái vải mọng nước', 60, 'ĐANG BÁN', 5);
INSERT INTO `SanPham` (`ma_sp`, `ten_sp`, `gia`, `mo_ta`, `so_luong_ton`, `trang_thai`, `ma_dm`) VALUES (60, 'Trà vải nha đam', 36000, 'Trà vải thanh ngọt thêm nha đam giòn sần sật', 60, 'ĐANG BÁN', 5);
INSERT INTO `SanPham` (`ma_sp`, `ten_sp`, `gia`, `mo_ta`, `so_luong_ton`, `trang_thai`, `ma_dm`) VALUES (61, 'Trà xoài kem cheese', 42000, 'Trà xoài tươi chua ngọt phủ lớp kem cheese béo ngậy', 60, 'ĐANG BÁN', 5);
INSERT INTO `SanPham` (`ma_sp`, `ten_sp`, `gia`, `mo_ta`, `so_luong_ton`, `trang_thai`, `ma_dm`) VALUES (62, 'Trà xoài Macchiato', 40000, 'Trà xoài nhiệt đới kết hợp lớp kem sữa Macchiato mịn màng', 60, 'ĐANG BÁN', 5);
COMMIT;

-- ----------------------------
-- Table structure for TaiKhoan
-- ----------------------------
DROP TABLE IF EXISTS `TaiKhoan`;
CREATE TABLE `TaiKhoan` (
  `ma_tk` int NOT NULL AUTO_INCREMENT,
  `ten_dangnhap` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `mat_khau` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `ho_ten` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `sdt` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `dia_chi` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `vai_tro` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'USER',
  `trang_thai` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'ACTIVE',
  PRIMARY KEY (`ma_tk`),
  UNIQUE KEY `ten_dangnhap` (`ten_dangnhap`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of TaiKhoan
-- ----------------------------
BEGIN;
INSERT INTO `TaiKhoan` (`ma_tk`, `ten_dangnhap`, `mat_khau`, `ho_ten`, `sdt`, `dia_chi`, `vai_tro`, `trang_thai`) VALUES (1, 'admin', '1233', 'Quản Trị Viên Soranchi', '0901000001', 'Cầu Giấy, Hà Nội', 'ADMIN', 'ACTIVE');
INSERT INTO `TaiKhoan` (`ma_tk`, `ten_dangnhap`, `mat_khau`, `ho_ten`, `sdt`, `dia_chi`, `vai_tro`, `trang_thai`) VALUES (2, 'khach1', '1233', 'Mat troi nho', '0912345671', '12 Xuân Thủy, Cầu Giấy, Hà Nội', 'USER', 'ACTIVE');
INSERT INTO `TaiKhoan` (`ma_tk`, `ten_dangnhap`, `mat_khau`, `ho_ten`, `sdt`, `dia_chi`, `vai_tro`, `trang_thai`) VALUES (3, 'khach2', '123456', 'Trần Hoàng Nam', '0912345672', '45 Hồ Tùng Mậu, Bắc Từ Liêm, Hà Nội', 'USER', 'ACTIVE');
INSERT INTO `TaiKhoan` (`ma_tk`, `ten_dangnhap`, `mat_khau`, `ho_ten`, `sdt`, `dia_chi`, `vai_tro`, `trang_thai`) VALUES (4, 'khach3', '123456', 'Lê Thu Hà', '0912345673', '88 Trần Duy Hưng, Cầu Giấy, Hà Nội', 'USER', 'LOCKED');
COMMIT;

-- ----------------------------
-- Table structure for TinNhan
-- ----------------------------
DROP TABLE IF EXISTS `TinNhan`;
CREATE TABLE `TinNhan` (
  `ma_tinnhan` int NOT NULL AUTO_INCREMENT,
  `noi_dung` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `vai_tro` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `thoi_gian_gui` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `ma_cuochoithoai` int NOT NULL,
  PRIMARY KEY (`ma_tinnhan`),
  KEY `ma_cuochoithoai` (`ma_cuochoithoai`),
  CONSTRAINT `tinnhan_ibfk_1` FOREIGN KEY (`ma_cuochoithoai`) REFERENCES `CuocHoiThoai` (`ma_cuochoithoai`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of TinNhan
-- ----------------------------
BEGIN;
INSERT INTO `TinNhan` (`ma_tinnhan`, `noi_dung`, `vai_tro`, `thoi_gian_gui`, `ma_cuochoithoai`) VALUES (1, 'Tra cứu thông tin thực đơn', 'USER', '2026-11-23 23:06:34', 2);
INSERT INTO `TinNhan` (`ma_tinnhan`, `noi_dung`, `vai_tro`, `thoi_gian_gui`, `ma_cuochoithoai`) VALUES (2, 'Dưới đây là thực đơn hiện đang kinh doanh tại Soranchi:\n\n**Đồ ăn**\n- Bánh kem cuộn – 28.000 VNĐ – Bánh bông lan cuộn kem tươi mềm mịn, ngọt thanh\n- Bánh su kem – 25.000 VNĐ – Vỏ bánh giòn nhẹ nhân kem vani béo ngậy\n- Bánh tráng cuốn – 22.000 VNĐ – Bánh tráng cuốn sốt me, trứng cút và khô bò đậm đà\n- Bánh tráng nướng – 25.000 VNĐ – Bánh tráng nướng giòn rụm đầy ắp topping\n- Bánh tráng trộn – 22.000 VNĐ – Bánh tráng trộn chua cay mặn ngọt chuẩn vị\n- Cheesecake cam – 38.000 VNĐ – Bánh phô mai lạnh vị cam tươi mát dịu\n- Bánh sừng bò Croissant – 30.000 VNĐ – Bánh sừng bò ngàn lớp nướng bơ Pháp thơm lừng\n- Hokkaido Yuki – Bánh phô mai Nhật Bản – 42.000 VNĐ – Bánh phô mai tan chảy chuẩn vị Hokkaido\n- Hạt hướng dương – 15.000 VNĐ – Hạt hướng dương rang giòn thơm bùi\n- Mẹt ăn vặt tổng hợp – 55.000 VNĐ – Mẹt ăn vặt đầy đủ nem chua rán, xúc xích, khoai chiên\n- Mousse dâu tây – 38.000 VNĐ – Bánh mousse dâu tây chua ngọt thanh mát\n- Mousse xoài chanh leo – 38.000 VNĐ – Bánh mousse nhiệt đới vị xoài và chanh leo\n- Nem chua rán – 30.000 VNĐ – Nem chua rán vàng giòn kèm tương ớt cay nồng\n- Ngô chiên bơ – 25.000 VNĐ – Ngô ngọt chiên bơ giòn rụm thơm phức\n- Snack các loại – 18.000 VNĐ – Combo snack giòn tan nhâm nhi cùng trà sữa\n- Bánh kem dâu tây – 40.000 VNĐ – Bánh kem tươi phủ dâu tây Đà Lạt mọng nước\n- Tiramisu – 39.000 VNĐ – Bánh Tiramisu vị cà phê cacao Ý quyến rũ\n- Xúc xích rán – 20.000 VNĐ – Xúc xích Đức rán giòn thơm ngon\n\n**Cà phê**\n- Bạc xỉu – 29.000 VNĐ – Cà phê sữa nhiều sữa tươi béo ngậy, dịu nhẹ\n- Cacao nóng – 32.000 VNĐ – Cacao nguyên chất pha nóng ấm áp, đậm đà\n- Cà phê kem trứng muối – 39.000 VNĐ – Cà phê phin đậm vị phủ lớp kem trứng muối béo mặn\n- Caramel Americano – 35.000 VNĐ – Espresso pha loãng thanh mát kết hợp sốt caramel\n- Cold Brew mơ – 38.000 VNĐ – Cà phê ủ lạnh kết hợp vị mơ ngâm chua ngọt thanh mát\n- Cold Brew cam – 38.000 VNĐ – Cà phê ủ lạnh thanh khiết hòa quyện cùng cam vàng tươi\n- Cà phê cốt dừa Conan – 39.000 VNĐ – Cà phê xay cùng cốt dừa béo mịn thơm lừng\n- Cà phê đen đá – 25.000 VNĐ – Cà phê phin truyền thống đậm đà tỉnh táo\n- Mocha Espresso – 38.000 VNĐ – Sự kết hợp hài hòa giữa Espresso, sữa nóng và socola\n- Soran Coffee đặc biệt – 36.000 VNĐ – Công thức cà phê độc quyền tại Soranchi\n- Cà phê sữa đá – 27.000 VNĐ – Cà phê phin hòa quyện sữa đặc ngọt ngào truyền thống\n\n**Nước ép & Sinh tố**\n- Bơ già dừa non – 42.000 VNĐ – Sinh tố bơ sáp dẻo mịn kết hợp dừa non giòn sần sật\n- Chanh tươi mát lạnh – 25.000 VNĐ – Nước chanh tươi vắt giải nhiệt sảng khoái tức thì\n- Nước ép dứa – 32.000 VNĐ – Nước ép dứa tươi nguyên chất giàu vitamin C\n- Sinh tố xoài (Mango Smoothie) – 38.000 VNĐ – Xoài chín xay mịn mát lạnh, ngọt thơm tự nhiên\n- Nước ép nha đam – 30.000 VNĐ – Nước ép nha đam thanh mát, đẹp da giải nhiệt\n- Sinh tố bơ – 39.000 VNĐ – Bơ sáp Đắk Lắk xay sánh mịn, béo thơm tự nhiên\n- Sinh tố dâu – 39.000 VNĐ – Dâu tây tươi xay nhuyễn chua ngọt hấp dẫn\n- Sinh tố lựu – 40.000 VNĐ – Nước ép & sinh tố lựu đỏ tươi mát, chống oxy hóa\n- Sinh tố mãng cầu – 38.000 VNĐ – Mãng cầu xiêm xay chua ngọt thanh mát giải nhiệt\n- Sinh tố táo – 35.000 VNĐ – Táo tươi xay mịn thanh ngọt, giàu chất xơ\n\n**Trà sữa**\n- Trà sữa quế Matcha (Cinnamon Matcha) – 39.000 VNĐ – Hương quế ấm áp hòa quyện cùng bột trà xanh Nhật Bản\n- Matcha kem muối – 42.000 VNĐ – Trà xanh matcha đậm vị phủ lớp kem phô mai mặn béo\n- Matcha Latte – 38.000 VNĐ – Bột trà xanh nguyên chất kết hợp sữa tươi thanh trùng\n- Matcha sữa gấu – 40.000 VNĐ – Trà xanh matcha pha cùng sữa gấu béo ngậy đặc trưng\n- Trà sữa Ô Long nhài – 35.000 VNĐ – Trà Ô Long ủ hoa nhài thơm ngát kết hợp sữa béo dịu\n- Trà sữa Ô Long Tứ Quý – 35.000 VNĐ – Hương trà Ô Long Tứ Quý thanh tao, hậu vị ngọt sâu\n- Trà sữa Oreo Chocolate Cream – 42.000 VNĐ – Trà sữa socola phủ kem tươi và vụn bánh Oreo giòn tan\n- Trà sữa đường nhạn – 36.000 VNĐ – Trà sữa nấu cùng đường nâu và nhãn nhục thơm ngọt\n\n**Trà trái cây**\n- Trà cam quế – 35.000 VNĐ – Trà đen ủ cùng cam vàng và thanh quế thơm dịu\n- Trà đào cam sả – 36.000 VNĐ – Thức uống quốc dân thanh mát từ đào miếng, cam tươi và sả\n- Trà hoa quả nhiệt đới – 39.000 VNĐ – Trà xanh kết hợp các loại trái cây tươi nhiệt đới giòn ngọt\n- Trà mâm xôi – 38.000 VNĐ – Trà thanh mát hòa quyện vị quả mâm xôi đỏ chua ngọt\n- Trà mận – 36.000 VNĐ – Trà ủ mận hậu chua ngọt giòn tan giải nhiệt mùa hè\n- Trà mơ sơ ri hạt chia – 38.000 VNĐ – Trà mơ sơ ri thanh mát kết hợp hạt chia tốt cho sức khỏe\n- Trà nhài quýt – 36.000 VNĐ – Trà xanh hoa nhài thơm dịu kết hợp nước ép quýt tươi\n- Trà nhãn – 36.000 VNĐ – Trà thanh khiết kết hợp cùi nhãn lồng giòn ngọt\n- Trà ổi hồng – 35.000 VNĐ – Trà xanh hòa quyện nước ép ổi hồng thơm lừng, chua ngọt\n- Trà vải dưa hấu – 38.000 VNĐ – Sự kết hợp tươi mát giữa vải thiều ngọt ngào và dưa hấu đỏ\n- Trà vải hoa hồng – 38.000 VNĐ – Trà hương hoa hồng quyến rũ kết hợp trái vải mọng nước\n- Trà vải nha đam – 36.000 VNĐ – Trà vải thanh ngọt thêm nha đam giòn sần sật\n- Trà xoài kem cheese – 42.000 VNĐ – Trà xoài tươi chua ngọt phủ lớp kem cheese béo ngậy\n- Trà xoài Macchiato – 40.000 VNĐ – Trà xoài nhiệt đới kết hợp lớp kem sữa Macchiato mịn màng\n\n**Mã giảm giá hiện có**\n- TEST1: Giảm 20 % cho toàn bộ đơn hàng\n- NAMMOI20: Giảm 50 % cho toàn bộ đơn hàng\n\nBạn có muốn đặt món nào hoặc cần tư vấn thêm không?', 'BOT', '2026-11-23 23:06:40', 2);
INSERT INTO `TinNhan` (`ma_tinnhan`, `noi_dung`, `vai_tro`, `thoi_gian_gui`, `ma_cuochoithoai`) VALUES (3, 'có loại nước nào giá dưới 40000 không', 'USER', '2026-11-23 23:07:48', 2);
INSERT INTO `TinNhan` (`ma_tinnhan`, `noi_dung`, `vai_tro`, `thoi_gian_gui`, `ma_cuochoithoai`) VALUES (4, 'Dưới 40.000 VNĐ, cửa hàng có các loại nước sau:\n\n**Cà phê**\n- Bạc xỉu – 29.000 VNĐ\n- Cacao nóng – 32.000 VNĐ\n- Cà phê kem trứng muối – 39.000 VNĐ\n- Caramel Americano – 35.000 VNĐ\n- Cold Brew mơ – 38.000 VNĐ\n- Cold Brew cam – 38.000 VNĐ\n- Cà phê cốt dừa Conan – 39.000 VNĐ\n- Cà phê đen đá – 25.000 VNĐ\n- Mocha Espresso – 38.000 VNĐ\n- Soran Coffee đặc biệt – 36.000 VNĐ\n- Cà phê sữa đá – 27.000 VNĐ\n\n**Nước ép & Sinh tố**\n- Chanh tươi mát lạnh – 25.000 VNĐ\n- Nước ép dứa – 32.000 VNĐ\n- Sinh tố xoài (Mango Smoothie) – 38.000 VNĐ\n- Nước ép nha đam – 30.000 VNĐ\n- Sinh tố bơ – 39.000 VNĐ\n- Sinh tố dâu – 39.000 VNĐ\n- Sinh tố mãng cầu – 38.000 VNĐ\n- Sinh tố táo – 35.000 VNĐ\n\n**Trà sữa**\n- Trà sữa quế Matcha – 39.000 VNĐ\n- Matcha Latte – 38.000 VNĐ\n- Trà sữa Ô Long nhài – 35.000 VNĐ\n- Trà sữa Ô Long Tứ Quý – 35.000 VNĐ\n- Trà sữa đường nhạn – 36.000 VNĐ\n\n**Trà trái cây**\n- Trà cam quế – 35.000 VNĐ\n- Trà đào cam sả – 36.000 VNĐ\n- Trà hoa quả nhiệt đới – 39.000 VNĐ\n- Trà mâm xôi – 38.000 VNĐ\n- Trà mận – 36.000 VNĐ\n- Trà mơ sơ ri hạt chia – 38.000 VNĐ\n- Trà nhài quýt – 36.000 VNĐ\n- Trà nhãn – 36.000 VNĐ\n- Trà ổi hồng – 35.000 VNĐ\n- Trà vải dưa hấu – 38.000 VNĐ\n- Trà vải hoa hồng – 38.000 VNĐ\n- Trà vải nha đam – 36.000 VNĐ\n\nNếu cần thêm thông tin hoặc muốn đặt món, bạn cứ cho mình biết nhé!', 'BOT', '2026-11-23 23:07:52', 2);
INSERT INTO `TinNhan` (`ma_tinnhan`, `noi_dung`, `vai_tro`, `thoi_gian_gui`, `ma_cuochoithoai`) VALUES (5, 'thời tiết hom nay như nào', 'USER', '2026-11-23 23:09:31', 2);
INSERT INTO `TinNhan` (`ma_tinnhan`, `noi_dung`, `vai_tro`, `thoi_gian_gui`, `ma_cuochoithoai`) VALUES (6, 'Vui lòng hỏi các câu hỏi liên quan đến sản phẩm và dịch vụ của cửa hàng.', 'BOT', '2026-11-23 23:09:31', 2);
INSERT INTO `TinNhan` (`ma_tinnhan`, `noi_dung`, `vai_tro`, `thoi_gian_gui`, `ma_cuochoithoai`) VALUES (7, 'a', 'USER', '2026-11-23 23:10:14', 2);
INSERT INTO `TinNhan` (`ma_tinnhan`, `noi_dung`, `vai_tro`, `thoi_gian_gui`, `ma_cuochoithoai`) VALUES (8, 'Vui lòng hỏi các câu hỏi liên quan đến sản phẩm và dịch vụ của cửa hàng.', 'BOT', '2026-11-23 23:10:14', 2);
COMMIT;

-- ----------------------------
-- Table structure for Topping
-- ----------------------------
DROP TABLE IF EXISTS `Topping`;
CREATE TABLE `Topping` (
  `ma_topping` int NOT NULL AUTO_INCREMENT,
  `ten_topping` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `gia` double NOT NULL,
  `mo_ta` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `trang_thai` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'ACTIVE',
  PRIMARY KEY (`ma_topping`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of Topping
-- ----------------------------
BEGIN;
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
