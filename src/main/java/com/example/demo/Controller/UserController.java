package com.example.demo.Controller;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // 1. Mở trang đăng nhập
    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("pageTitle", "Đăng nhập hệ thống");
        model.addAttribute("contentTemplate", "trasua/pages/login");
        model.addAttribute("contentFragment", "content");
        return "layout";
    }

    // 2. Xử lý khi bấm nút Đăng nhập
    @PostMapping("/login")
    public String processLogin(@RequestParam("username") String username,
                               @RequestParam("password") String password,
                               HttpSession session, Model model) {
        
        // Tìm user trong DB
        User user = userRepository.findByUsername(username);
        
        // Kiểm tra pass (Nếu đúng thì lưu vào phiên làm việc Session)
        if (user != null && user.getPassword().equals(password)) {
            session.setAttribute("loggedInUser", user);
            return "redirect:/"; // Thành công thì về trang chủ
        }
        
        // Thất bại thì báo lỗi
        model.addAttribute("error", "Sai tên đăng nhập hoặc mật khẩu!");
        model.addAttribute("pageTitle", "Đăng nhập");
        model.addAttribute("contentTemplate", "trasua/pages/login");
        model.addAttribute("contentFragment", "content");
        return "layout"; 
    }

    // 3. Đăng xuất
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("loggedInUser"); // Xóa phiên làm việc
        return "redirect:/"; // Về trang chủ
    }
}