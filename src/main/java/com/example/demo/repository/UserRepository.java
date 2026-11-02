package com.example.demo.repository;

import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
    
    // Lấy toàn bộ danh sách tài khoản theo vai trò (để lọc chỉ lấy USER, bỏ qua ADMIN)
    List<User> findByRole(String role);

    // Tìm kiếm khách hàng theo tên (không phân biệt hoa/thường)
    List<User> findByRoleAndFullNameContainingIgnoreCase(String role, String fullName);
}