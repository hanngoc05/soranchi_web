/*
 Navicat Premium Dump SQL

 Source Server         : vunghan
 Source Server Type    : MySQL
 Source Server Version : 90600 (9.6.0)
 Source Host           : localhost:3306
 Source Schema         : webtrasua

 Target Server Type    : MySQL
 Target Server Version : 90600 (9.6.0)
 File Encoding         : 65001

 Date: 11/06/2026 00:35:43
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for cart_items
-- ----------------------------
DROP TABLE IF EXISTS `cart_items`;
CREATE TABLE `cart_items` (
  `id` int NOT NULL AUTO_INCREMENT,
  `product_id` int DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `image_url` varchar(1000) DEFAULT NULL,
  `price` double DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of cart_items
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for categories
-- ----------------------------
DROP TABLE IF EXISTS `categories`;
CREATE TABLE `categories` (
  `id` int NOT NULL AUTO_INCREMENT,
  `alias` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of categories
-- ----------------------------
BEGIN;
INSERT INTO `categories` (`id`, `alias`, `name`, `status`) VALUES (1, 'trasua', 'Trà sữa', 'ACTIVE');
INSERT INTO `categories` (`id`, `alias`, `name`, `status`) VALUES (2, 'tra', 'Trà trái cây', 'ACTIVE');
INSERT INTO `categories` (`id`, `alias`, `name`, `status`) VALUES (3, 'topping', 'Topping', 'ACTIVE');
INSERT INTO `categories` (`id`, `alias`, `name`, `status`) VALUES (4, 'anvat', 'Đồ ăn vặt', 'ACTIVE');
INSERT INTO `categories` (`id`, `alias`, `name`, `status`) VALUES (5, 'banhngot', 'bánh ngọt', 'ACTIVE');
INSERT INTO `categories` (`id`, `alias`, `name`, `status`) VALUES (6, 'banhngot', 'Bình gốm men', 'ACTIVE');
COMMIT;

-- ----------------------------
-- Table structure for order_details
-- ----------------------------
DROP TABLE IF EXISTS `order_details`;
CREATE TABLE `order_details` (
  `id` int NOT NULL AUTO_INCREMENT,
  `order_id` int NOT NULL,
  `product_id` int NOT NULL,
  `product_name` varchar(255) NOT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `price` double NOT NULL,
  `quantity` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `order_id` (`order_id`),
  CONSTRAINT `order_details_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of order_details
-- ----------------------------
BEGIN;
INSERT INTO `order_details` (`id`, `order_id`, `product_id`, `product_name`, `image_url`, `price`, `quantity`) VALUES (1, 1, 1, 'Hồng Trà Sữa Soranchi (100% Đường, Đá bình thường, Topping: Trân châu hoàng kim, Thạch sương sáo)', 'trasua.jpg', 56000, 2);
INSERT INTO `order_details` (`id`, `order_id`, `product_id`, `product_name`, `image_url`, `price`, `quantity`) VALUES (2, 1, 2, 'Trà Sữa Matcha Đậu Đỏ (100% Đường, Đá bình thường, Topping: Trân châu hoàng kim)', 'matcha.jpg', 56000, 1);
INSERT INTO `order_details` (`id`, `order_id`, `product_id`, `product_name`, `image_url`, `price`, `quantity`) VALUES (3, 2, 1, 'Hồng Trà Sữa Soranchi (Thạch sương sáo)', 'trasua1.jpg', 56000, 2);
INSERT INTO `order_details` (`id`, `order_id`, `product_id`, `product_name`, `image_url`, `price`, `quantity`) VALUES (4, 3, 1, 'Hồng Trà Sữa Soranchi (Kem cheese)', 'trasua1.jpg', 62000, 2);
INSERT INTO `order_details` (`id`, `order_id`, `product_id`, `product_name`, `image_url`, `price`, `quantity`) VALUES (5, 3, 3, 'Trà Trái Cây Nhiệt Đới', 'tra1.jpg', 47000, 2);
COMMIT;

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `full_name` varchar(255) NOT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `total_price` double NOT NULL,
  `status` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `payment_method` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of orders
-- ----------------------------
BEGIN;
INSERT INTO `orders` (`id`, `user_id`, `full_name`, `phone`, `address`, `total_price`, `status`, `created_at`, `payment_method`) VALUES (1, 1, 'Vũ Ngọc Hân', '0987651234', 'a', 84000, 'ĐANG GIAO HÀNG', '2026-06-10 22:03:52', 'COD');
INSERT INTO `orders` (`id`, `user_id`, `full_name`, `phone`, `address`, `total_price`, `status`, `created_at`, `payment_method`) VALUES (2, 1, 'Vũ Ngọc Hân', '0987651234', 'Hà Nội', 112000, 'HOÀN THÀNH', '2026-06-09 14:30:00', 'COD');
INSERT INTO `orders` (`id`, `user_id`, `full_name`, `phone`, `address`, `total_price`, `status`, `created_at`, `payment_method`) VALUES (3, 1, 'Vũ Ngọc Hân', '0987651234', 'Hà Nội', 218000, 'HOÀN THÀNH', '2026-06-10 19:20:00', 'BANK');
COMMIT;

-- ----------------------------
-- Table structure for policies
-- ----------------------------
DROP TABLE IF EXISTS `policies`;
CREATE TABLE `policies` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `content` text NOT NULL,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of policies
-- ----------------------------
BEGIN;
INSERT INTO `policies` (`id`, `title`, `content`, `updated_at`) VALUES (1, 'Chính sách giao hàng', 'Soranchi cam kết giao trà sữa trong vòng 30 phút nội thành. Đảm bảo đá không tan, giữ nguyên vị kem béo.', '2026-06-10 22:33:32');
INSERT INTO `policies` (`id`, `title`, `content`, `updated_at`) VALUES (2, 'Chính sách hoàn tiền', 'Nếu sản phẩm bị lỗi hỏng do quá trình vận chuyển hoặc không đúng mẫu, Soranchi hỗ trợ đổi ly mới hoặc hoàn tiền 100%.', '2026-06-10 22:33:32');
COMMIT;

-- ----------------------------
-- Table structure for products
-- ----------------------------
DROP TABLE IF EXISTS `products`;
CREATE TABLE `products` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `price` double NOT NULL,
  `image_url` varchar(1000) DEFAULT NULL,
  `category` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of products
-- ----------------------------
BEGIN;
INSERT INTO `products` (`id`, `name`, `price`, `image_url`, `category`) VALUES (1, 'Hồng Trà Sữa Soranchi', 45000, 'trasua.jpg', 'Trà sữa');
INSERT INTO `products` (`id`, `name`, `price`, `image_url`, `category`) VALUES (2, 'Trà Sữa Matcha Đậu Đỏ', 50000, 'matcha.jpg', 'Trà sữa');
INSERT INTO `products` (`id`, `name`, `price`, `image_url`, `category`) VALUES (3, 'Trà Sữa Trân Châu Hoàng Kim', 48000, 'cfkem.jpg', 'Trà sữa');
INSERT INTO `products` (`id`, `name`, `price`, `image_url`, `category`) VALUES (4, 'Sữa Tươi Trân Châu Đường Đen', 55000, 'trasua.jpg', 'Trà sữa');
INSERT INTO `products` (`id`, `name`, `price`, `image_url`, `category`) VALUES (5, 'Trà Sữa Khoai Môn', 45000, 'trasorihatchia.jpg', 'Trà sữa');
INSERT INTO `products` (`id`, `name`, `price`, `image_url`, `category`) VALUES (6, 'Cà phê kem trứng', 55000, 'cfkem.jpg', 'Trà sữa');
INSERT INTO `products` (`id`, `name`, `price`, `image_url`, `category`) VALUES (7, 'Trà Sữa Sương Sáo', 42000, 'trasua.jpg', 'Trà sữa');
INSERT INTO `products` (`id`, `name`, `price`, `image_url`, `category`) VALUES (8, 'Trà Sữa Caramel', 48000, 'cf.jpg', 'Trà sữa');
INSERT INTO `products` (`id`, `name`, `price`, `image_url`, `category`) VALUES (9, 'Trà Sữa Socola', 48000, 'cacaodaxay.jpg', 'Trà sữa');
INSERT INTO `products` (`id`, `name`, `price`, `image_url`, `category`) VALUES (10, 'Trà Sữa Hạt Dẻ', 50000, 'comdaxay.jpg', 'Trà sữa');
INSERT INTO `products` (`id`, `name`, `price`, `image_url`, `category`) VALUES (11, 'Trà Đào Cam Sả', 45000, 'traxoaimachiato.jpg', 'Trà trái cây');
INSERT INTO `products` (`id`, `name`, `price`, `image_url`, `category`) VALUES (12, 'Trà Vải Sen', 45000, 'nuoc_ep.jpg', 'Trà trái cây');
INSERT INTO `products` (`id`, `name`, `price`, `image_url`, `category`) VALUES (13, 'Trà Dâu Tây Kem Cheese', 55000, 'nuoc_ep.jpg', 'Trà trái cây');
INSERT INTO `products` (`id`, `name`, `price`, `image_url`, `category`) VALUES (14, 'Trà Hoa Quả Nhiệt Đới', 50000, 'nuoc_cam.jpg', 'Trà trái cây');
INSERT INTO `products` (`id`, `name`, `price`, `image_url`, `category`) VALUES (15, 'Trà Việt Quất', 42000, 'nuoc_ep.jpg', 'Trà trái cây');
INSERT INTO `products` (`id`, `name`, `price`, `image_url`, `category`) VALUES (16, 'Trân Châu Đen', 5000, 'tranchauden.jpeg', 'Topping');
INSERT INTO `products` (`id`, `name`, `price`, `image_url`, `category`) VALUES (17, 'Trân Châu Trắng', 8000, 'tranchautrang.jpg', 'Topping');
INSERT INTO `products` (`id`, `name`, `price`, `image_url`, `category`) VALUES (18, 'Thạch Sương Sáo', 5000, 'suongsao.jpeg', 'Topping');
INSERT INTO `products` (`id`, `name`, `price`, `image_url`, `category`) VALUES (19, 'Kem Cheese', 10000, 'cfkem.jpg', 'Topping');
INSERT INTO `products` (`id`, `name`, `price`, `image_url`, `category`) VALUES (20, 'Pudding Trứng', 8000, 'puding.jpg', 'Topping');
INSERT INTO `products` (`id`, `name`, `price`, `image_url`, `category`) VALUES (21, 'Khoai Tây Chiên', 30000, 'khoai.webp', 'Đồ ăn vặt');
INSERT INTO `products` (`id`, `name`, `price`, `image_url`, `category`) VALUES (22, 'Nem Chua Rán', 35000, 'nem.webp', 'Đồ ăn vặt');
INSERT INTO `products` (`id`, `name`, `price`, `image_url`, `category`) VALUES (23, 'Cá Viên Chiên', 30000, 'cavien.jpg', 'Đồ ăn vặt');
INSERT INTO `products` (`id`, `name`, `price`, `image_url`, `category`) VALUES (24, 'Gà Xiên Que', 40000, 'set_banh.jpg', 'Đồ ăn vặt');
INSERT INTO `products` (`id`, `name`, `price`, `image_url`, `category`) VALUES (25, 'Xúc Xích Đức', 20000, 'xucxich.jpg', 'Đồ ăn vặt');
INSERT INTO `products` (`id`, `name`, `price`, `image_url`, `category`) VALUES (26, 'Bánh Tráng Trộn', 25000, 'banhtrang.webp', 'Đồ ăn vặt');
INSERT INTO `products` (`id`, `name`, `price`, `image_url`, `category`) VALUES (27, 'Ngô Chiên Bơ', 30000, 'ngo.jpg', 'Đồ ăn vặt');
INSERT INTO `products` (`id`, `name`, `price`, `image_url`, `category`) VALUES (28, 'Phô Mai Que', 35000, 'set_banh.jpg', 'Đồ ăn vặt');
INSERT INTO `products` (`id`, `name`, `price`, `image_url`, `category`) VALUES (29, 'Khô Gà Lá Chanh', 45000, 'set_banh.jpg', 'Đồ ăn vặt');
INSERT INTO `products` (`id`, `name`, `price`, `image_url`, `category`) VALUES (30, 'Bánh Kem mini', 20000, 'banh.jpg', 'bánh ngọt');
COMMIT;

-- ----------------------------
-- Table structure for promotions
-- ----------------------------
DROP TABLE IF EXISTS `promotions`;
CREATE TABLE `promotions` (
  `id` int NOT NULL AUTO_INCREMENT,
  `code` varchar(255) NOT NULL,
  `title` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `discount_percent` int NOT NULL DEFAULT '0',
  `image_url` varchar(255) DEFAULT NULL,
  `start_date` date NOT NULL,
  `end_date` date NOT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of promotions
-- ----------------------------
BEGIN;
INSERT INTO `promotions` (`id`, `code`, `title`, `description`, `discount_percent`, `image_url`, `start_date`, `end_date`, `is_active`) VALUES (1, 'SORANCHI50', 'MUA 1 TẶNG 1', 'Áp dụng cho tất cả trà sữa trân châu vào thứ 2 hàng tuần.', 50, 'noel.jpg', '2026-06-01', '2026-07-01', 1);
INSERT INTO `promotions` (`id`, `code`, `title`, `description`, `discount_percent`, `image_url`, `start_date`, `end_date`, `is_active`) VALUES (2, 'NAMMOI20', 'GIẢM 20% TỔNG BILL', 'Chào mừng bạn mới, giảm ngay 20% cho đơn hàng đầu tiên.', 20, 'noel.jpg', '2026-06-01', '2026-07-01', 1);
COMMIT;

-- ----------------------------
-- Table structure for reviews
-- ----------------------------
DROP TABLE IF EXISTS `reviews`;
CREATE TABLE `reviews` (
  `id` int NOT NULL AUTO_INCREMENT,
  `product_id` int NOT NULL,
  `customer_name` varchar(255) NOT NULL,
  `rating` int NOT NULL,
  `comment` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of reviews
-- ----------------------------
BEGIN;
INSERT INTO `reviews` (`id`, `product_id`, `customer_name`, `rating`, `comment`, `created_at`) VALUES (1, 1, 'Khách ẩn danh', 2, 'abc', '2026-06-10 14:53:42');
COMMIT;

-- ----------------------------
-- Table structure for supports
-- ----------------------------
DROP TABLE IF EXISTS `supports`;
CREATE TABLE `supports` (
  `id` int NOT NULL AUTO_INCREMENT,
  `customer_name` varchar(255) DEFAULT NULL,
  `customer_phone` varchar(255) DEFAULT NULL,
  `issue_type` varchar(255) DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of supports
-- ----------------------------
BEGIN;
INSERT INTO `supports` (`id`, `customer_name`, `customer_phone`, `issue_type`, `content`, `status`, `created_at`) VALUES (1, 'Nguyễn Văn A', '0912345678', 'Sai món', 'Tôi đặt Trà sữa Soranchi trân châu hoàng kim nhưng quán giao nhầm thạch dừa.', 'CHƯA XỬ LÝ', '2026-06-10 22:33:32');
COMMIT;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `full_name` varchar(255) NOT NULL,
  `role` varchar(255) DEFAULT NULL,
  `avatar_url` varchar(255) DEFAULT 'default-avatar.png',
  `address` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of user
-- ----------------------------
BEGIN;
INSERT INTO `user` (`id`, `username`, `password`, `full_name`, `role`, `avatar_url`, `address`, `phone`) VALUES (1, 'khachhang', '1233', ' mặt trời nhỏ', 'USER', '1781081278943_meo.jpg', '1', '1234567890');
INSERT INTO `user` (`id`, `username`, `password`, `full_name`, `role`, `avatar_url`, `address`, `phone`) VALUES (2, 'admin', '1233', 'Quản Trị Viên', 'ADMIN', 'default-avatar.png', NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `full_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of users
-- ----------------------------
BEGIN;
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
