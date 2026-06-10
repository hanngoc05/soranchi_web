package com.example.demo.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    
    @RequestMapping("/home")
    public String sayHello(Model model) {
        model.addAttribute("title", "Hello page test");
        model.addAttribute("subject", "Spring Boot MPC test");
        
        // ÉP SIDEBAR GỌI ĐÚNG FILE HOME.HTML 
        model.addAttribute("contentTemplate", "trasua/pages/home"); // Đường dẫn đến file home.html của bạn
        model.addAttribute("contentFragment", "content");
        
        return "layout";
    }
}