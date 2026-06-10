package com.example.demo.Controller;

import com.example.demo.model.Category;
import com.example.demo.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import java.util.List;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private CategoryRepository categoryRepository;

    // Phép thuật ở đây: Hàm này sẽ tự động chạy và gửi danh sách "globalCategories" 
    // đến TẤT CẢ các file HTML của bạn mà không cần gọi.
    @ModelAttribute("globalCategories")
    public List<Category> getGlobalCategories() {
        return categoryRepository.findAll();
    }
}