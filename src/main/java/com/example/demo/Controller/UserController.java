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

    @GetMapping("/login")
    public String loginPage(Model model, HttpSession session) {
        if (session.getAttribute("loggedInUser") != null) {
            return "redirect:/";
        }
        model.addAttribute("pageTitle", "Đăng nhập hệ thống");
        model.addAttribute("contentTemplate", "trasua/pages/login");
        model.addAttribute("contentFragment", "content");
        return "layout";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam("username") String username,
                               @RequestParam("password") String password,
                               HttpSession session, 
                               Model model) {
        
        User user = userRepository.findByUsername(username);
        
        if (user != null && user.getPassword().equals(password)) {
            // Chặn đăng nhập nếu tài khoản bị khóa
            if (!user.isActive()) {
                model.addAttribute("error", "Tài khoản của bạn đã bị khóa do vi phạm quy định!");
                model.addAttribute("pageTitle", "Đăng nhập hệ thống");
                model.addAttribute("contentTemplate", "trasua/pages/login");
                model.addAttribute("contentFragment", "content");
                return "layout";
            }

            session.setAttribute("loggedInUser", user);

            if ("ADMIN".equalsIgnoreCase(user.getRole())) {
                return "redirect:/admin/orders";
            }
            return "redirect:/";
        }
        
        model.addAttribute("error", "Sai tên đăng nhập hoặc mật khẩu!");
        model.addAttribute("pageTitle", "Đăng nhập hệ thống");
        model.addAttribute("contentTemplate", "trasua/pages/login");
        model.addAttribute("contentFragment", "content");
        return "layout"; 
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("loggedInUser");
        return "redirect:/";
    }
}