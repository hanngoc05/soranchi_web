package com.example.demo.Controller;

import com.example.demo.model.Topping;
import com.example.demo.model.User;
import com.example.demo.repository.OrderDetailRepository;
import com.example.demo.repository.ToppingRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/toppings")
public class AdminToppingController {

    @Autowired
    private ToppingRepository toppingRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;
    // Thêm @Autowired ở đầu class AdminToppingController:
    @Autowired
    private com.example.demo.repository.ChiTietDonHangToppingRepository chiTietDonHangToppingRepository;

    private boolean isAdmin(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        return user != null && "ADMIN".equalsIgnoreCase(user.getRole());
    }

    // Bước 1: Hiển thị danh sách topping
    @GetMapping
    public String listToppings(Model model, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        model.addAttribute("toppings", toppingRepository.findAll());
        model.addAttribute("pageTitle", "Quản lý Topping");
        model.addAttribute("contentTemplate", "trasua/pages/admin_toppings");
        model.addAttribute("contentFragment", "content");
        return "layout";
    }

    // Bước 2.2 & 3.2: Thêm mới hoặc Cập nhật topping
    @PostMapping("/save")
    public String saveTopping(@RequestParam(value = "id", required = false) Integer id,
                              @RequestParam(value = "name", defaultValue = "") String name,
                              @RequestParam(value = "price", defaultValue = "0") double price,
                              @RequestParam(value = "status", defaultValue = "Đang bán") String status,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        String cleanName = name.trim();

        // Rẽ nhánh 1: Kiểm tra bỏ trống tên hoặc giá <= 0
        if (cleanName.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Tên topping không được để trống!");
            return "redirect:/admin/toppings";
        }
        if (price <= 0) {
            redirectAttributes.addFlashAttribute("errorMessage", "Giá bán topping phải là số dương lớn hơn 0!");
            return "redirect:/admin/toppings";
        }

        try {
            // Rẽ nhánh 1: Kiểm tra trùng tên topping
            if (id == null || id == 0) {
                if (toppingRepository.existsByNameIgnoreCase(cleanName)) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Tên topping '" + cleanName + "' đã tồn tại!");
                    return "redirect:/admin/toppings";
                }
                Topping newTopping = new Topping();
                newTopping.setName(cleanName);
                newTopping.setPrice(price);
                newTopping.setStatus(status);
                toppingRepository.save(newTopping);
                redirectAttributes.addFlashAttribute("successMessage", "Thêm topping mới thành công!");
            } else {
                if (toppingRepository.existsByNameIgnoreCaseAndIdNot(cleanName, id)) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Tên topping '" + cleanName + "' đã tồn tại!");
                    return "redirect:/admin/toppings";
                }
                Topping existing = toppingRepository.findById(id).orElse(null);
                if (existing != null) {
                    existing.setName(cleanName);
                    existing.setPrice(price);
                    existing.setStatus(status);
                    toppingRepository.save(existing);
                    redirectAttributes.addFlashAttribute("successMessage", "Cập nhật topping thành công!");
                }
            }
        } catch (Exception e) {
            // Rẽ nhánh 3: Lỗi kết nối CSDL
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi hệ thống: Không thể lưu dữ liệu vào cơ sở dữ liệu!");
        }

        return "redirect:/admin/toppings";
    }

    // Bước 4: Xóa topping (Nếu đã xuất hiện trong đơn hàng -> chuyển sang "Ngừng bán")
    @GetMapping("/delete/{id}")
    public String deleteTopping(@PathVariable("id") int id,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        try {
            Topping topping = toppingRepository.findById(id).orElse(null);
            if (topping == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy topping cần xóa!");
                return "redirect:/admin/toppings";
            }

            // Kiểm tra xem topping đã từng xuất hiện trong đơn hàng chưa (tên món có chứa tên topping)


// Thay đoạn kiểm tra trong hàm deleteTopping bằng:
boolean usedInOrders = chiTietDonHangToppingRepository.findAll().stream()
        .anyMatch(ct -> ct.getToppingId() == id);

            if (usedInOrders) {
                topping.setStatus("Ngừng bán");
                toppingRepository.save(topping);
                redirectAttributes.addFlashAttribute("successMessage",
                        "Topping '" + topping.getName() + "' đã từng xuất hiện trong đơn hàng nên hệ thống chuyển trạng thái sang 'Ngừng bán'!");
            } else {
                toppingRepository.delete(topping);
                redirectAttributes.addFlashAttribute("successMessage", "Xóa topping thành công!");
            }
        } catch (Exception e) {
            // Rẽ nhánh 3: Lỗi kết nối CSDL
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi hệ thống: Không thể thực hiện thao tác với cơ sở dữ liệu!");
        }

        return "redirect:/admin/toppings";
    }
}