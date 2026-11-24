# WebTraSua - Website Quản Lý & Đặt Trà Sữa Online

Hệ thống website hỗ trợ đặt hàng, quản lý người dùng, xử lý đơn hàng và thanh toán trực tuyến cho thương hiệu trà sữa Soranchi.

---

## Tài liệu Dự án
- 📑 **Báo cáo Phân tích & Đặc tả hệ thống:** [Xem chi tiết tại đây (Google Docs)](https://docs.google.com/document/d/1PcpjUmP_1mXwFjDe1kQH4tophg7eeSV7pJp5_s0XeyU/edit?tab=t.0)

---

## Công nghệ sử dụng
- **Backend:** Java Spring Boot, Spring Data JPA, Maven
- **Frontend:** Thymeleaf, HTML5/CSS3, Bootstrap 5, JavaScript (Fetch API)
- **Cơ sở dữ liệu:** MySQL (`webtrasua_vn.sql`)

---

## Hướng dẫn cài đặt & Chạy MVP
1. **Khởi tạo cơ sở dữ liệu:** Mở MySQL (Navicat / MySQL Workbench) và chạy file `webtrasua_vn.sql`.
2. **Cấu hình kết nối:** Mở tệp `src/main/resources/application.properties`, cập nhật `spring.datasource.username` và `spring.datasource.password` theo cấu hình máy cục bộ.
3. **Khởi chạy ứng dụng:** Chạy lớp `WebTraSuaApplication.java` (hoặc chạy lệnh `./mvnw spring-boot:run`).
4. **Truy cập hệ thống:** Mở trình duyệt tại địa chỉ `http://localhost:8080`.

---

## Tài khoản đăng nhập mẫu
- **Quản trị viên (Admin):** `admin` / `1233`
- **Khách hàng:** `khach1` / `1233`