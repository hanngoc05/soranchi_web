package com.example.demo.Controller;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class AdminUserController {

    @Autowired
    private UserRepository userRepository;

    // Bước 1 & Luồng rẽ nhánh 1: Truy vấn danh sách và tìm kiếm tài khoản khách hàng
    @GetMapping("/admin/users")
    public String listUsers(@RequestParam(value = "keyword", required = false) String keyword,
                            Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null || !"ADMIN".equalsIgnoreCase(loggedInUser.getRole())) {
            return "redirect:/login";
        }

        try {
            List<User> customers;
            if (keyword != null && !keyword.trim().isEmpty()) {
                customers = userRepository.findByRoleAndFullNameContainingIgnoreCase("USER", keyword.trim());
                model.addAttribute("keyword", keyword.trim());
                if (customers.isEmpty()) {
                    model.addAttribute("searchNotFound", true);
                }
            } else {
                customers = userRepository.findByRole("USER");
            }
            model.addAttribute("customers", customers);
        } catch (Exception e) {
            // Luồng rẽ nhánh 3: Lỗi kết nối CSDL
            model.addAttribute("errorMessage", "Lỗi kết nối cơ sở dữ liệu! Không thể tải danh sách tài khoản.");
        }

        model.addAttribute("pageTitle", "Quản lý tài khoản khách hàng");
        model.addAttribute("contentTemplate", "trasua/pages/admin_users");
        model.addAttribute("contentFragment", "content");
        return "layout";
    }

    // Bước 3, 4 & 5: Khóa/Mở khóa, cập nhật DB và thông báo thành công
    @GetMapping("/admin/users/toggle-status/{id}")
    public String toggleUserStatus(@PathVariable("id") int id, 
                                   HttpSession session, 
                                   RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null || !"ADMIN".equalsIgnoreCase(loggedInUser.getRole())) {
            return "redirect:/login";
        }

        try {
            User user = userRepository.findById(id).orElse(null);
            if (user != null && "USER".equalsIgnoreCase(user.getRole())) {
                if (user.isActive()) {
                    user.setActive(false);
                    user.setStatus("LOCKED");
                    redirectAttributes.addFlashAttribute("successMessage", "Khóa tài khoản '" + user.getUsername() + "' thành công!");
                } else {
                    user.setActive(true);
                    user.setStatus("ACTIVE");
                    redirectAttributes.addFlashAttribute("successMessage", "Mở khóa tài khoản '" + user.getUsername() + "' thành công!");
                }
                userRepository.save(user);
            }
        } catch (Exception e) {
            // Luồng rẽ nhánh 3: Lỗi CSDL, không thay đổi dữ liệu
            redirectAttributes.addFlashAttribute("errorMessage", "Không thể kết nối cơ sở dữ liệu! Thao tác chưa được lưu.");
        }

        return "redirect:/admin/users";
    }
}