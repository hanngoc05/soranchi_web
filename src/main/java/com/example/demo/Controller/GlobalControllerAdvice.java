package com.example.demo.Controller;

import com.example.demo.model.Category;
import com.example.demo.model.User;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import java.util.Collections;
import java.util.List;

@ControllerAdvice(basePackages = "com.example.demo.Controller")
public class GlobalControllerAdvice {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CartService cartService;

    @ModelAttribute("globalCategories")
    public List<Category> getGlobalCategories(HttpServletRequest request) {
        String uri = request.getRequestURI();
        // Bỏ qua nếu là request tĩnh, ảnh, css, js hoặc trang error
        if (uri.startsWith("/css") || uri.startsWith("/js") || uri.startsWith("/avatar") 
                || uri.startsWith("/image") || uri.equals("/error")) {
            return Collections.emptyList();
        }
        return categoryRepository.findAll();
    }

    @ModelAttribute("cartCount")
    public int getCartCount(HttpSession session, HttpServletRequest request) {
        String uri = request.getRequestURI();
        if (uri.startsWith("/css") || uri.startsWith("/js") || uri.startsWith("/avatar") 
                || uri.startsWith("/image") || uri.equals("/error")) {
            return 0;
        }

        User user = (User) session.getAttribute("loggedInUser");
        if (user != null && !"ADMIN".equalsIgnoreCase(user.getRole())) {
            return cartService.getCount(user.getId());
        }
        return 0;
    }
}