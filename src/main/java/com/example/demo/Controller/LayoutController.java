package com.example.demo.Controller;

import com.example.demo.model.RevenueDTO;
import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;
import com.example.demo.model.CartItem;
import com.example.demo.model.Product;
import com.example.demo.model.Review;
import com.example.demo.model.User; 
import com.example.demo.model.Promotion;
import com.example.demo.model.Order;
import com.example.demo.model.OrderDetail;
import com.example.demo.model.Policy;
import com.example.demo.model.Support;
import com.example.demo.model.Category;
import com.example.demo.repository.CartItemRepository; 
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.PromotionRepository;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.repository.SupportRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.PolicyRepository;
import com.example.demo.repository.OrderDetailRepository;
import com.example.demo.service.CartService; 
import jakarta.servlet.http.HttpSession; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class LayoutController {

    @Autowired
    private ProductRepository productRepository; 
    
    @Autowired
    private PromotionRepository promotionRepository;
    
    @Autowired
    private CartService cartService;

    @Autowired
    private CartItemRepository cartItemRepository; 

    @Autowired
    private com.example.demo.repository.CategoryRepository categoryRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private PolicyRepository policyRepository;

    @Autowired
    private SupportRepository supportRepository;

    // ==========================================
    // CÁC CHỨC NĂNG CHUNG & KHÁCH HÀNG
    // ==========================================

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("products", productRepository.findAll().stream().limit(8).toList());
        model.addAttribute("promotions", promotionRepository.findByIsActiveTrue());
        model.addAttribute("pageTitle", "Trang chủ - Soranchi Tea");
        model.addAttribute("contentTemplate", "trasua/pages/home");
        model.addAttribute("contentFragment", "content");
        return "layout";
    }

    @GetMapping("/products")
    public String products(Model model) {
        List<Product> productList = productRepository.findAll();
        model.addAttribute("products", productList);
        model.addAttribute("pageTitle", "Tất Cả Sản Phẩm");
        model.addAttribute("contentTemplate", "trasua/pages/list");
        model.addAttribute("contentFragment", "content");
        return "layout";
    }

    @GetMapping("/category/{alias}")
    public String filterByCategory(@PathVariable("alias") String alias, Model model) {
        String categoryName = "";
        switch (alias) {
            case "trasua": categoryName = "Trà sữa"; break;
            case "tra": categoryName = "Trà trái cây"; break;
            case "topping": categoryName = "Topping"; break;
            case "anvat": categoryName = "Đồ ăn vặt"; break;
            default: return "redirect:/products";
        }
        List<Product> filteredProducts = productRepository.findByCategory(categoryName);
        model.addAttribute("products", filteredProducts);
        model.addAttribute("pageTitle", "Danh mục: " + categoryName);
        model.addAttribute("contentTemplate", "trasua/pages/list");
        model.addAttribute("contentFragment", "content");
        return "layout";
    }

    @GetMapping("/search")
    public String search(@RequestParam("keyword") String keyword, Model model) {
        List<Product> searchResults = productRepository.findByNameContainingIgnoreCase(keyword);
        model.addAttribute("products", searchResults);
        model.addAttribute("pageTitle", "Kết quả tìm kiếm cho: " + keyword);
        model.addAttribute("contentTemplate", "trasua/pages/list"); 
        model.addAttribute("contentFragment", "content");
        return "layout";
    }

    @GetMapping("/product/{id}")
    public String productDetail(@PathVariable("id") int id, Model model) {
        Product product = productRepository.findById(id).orElse(null);
        model.addAttribute("product", product);
        
        List<Review> dbReviews = reviewRepository.findByProductIdOrderByCreatedAtDesc(id);
        model.addAttribute("reviews", dbReviews);
        model.addAttribute("totalReviews", dbReviews.size());
        
        model.addAttribute("pageTitle", product != null ? product.getName() : "Chi tiết");
        model.addAttribute("contentTemplate", "trasua/pages/detail"); 
        model.addAttribute("contentFragment", "content");
        return "layout";
    }

    @PostMapping("/product/review")
    public String saveReview(@RequestParam("productId") int productId,
                            @RequestParam("rating") int rating,
                            @RequestParam("comment") String comment,
                            HttpSession session, HttpServletRequest request) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }
        
        Review newReview = new Review();
        newReview.setProductId(productId);
        newReview.setRating(rating);
        newReview.setComment(comment);
        newReview.setCustomerName(loggedInUser.getFullName());
        
        reviewRepository.save(newReview);
        
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/product/" + productId);
    }

    @GetMapping("/order")
    public String orderPage(Model model) {
        model.addAttribute("cartItems", cartService.getItems());
        model.addAttribute("totalPrice", cartService.getTotalPrice());
        model.addAttribute("pageTitle", "Giỏ hàng");
        model.addAttribute("contentTemplate", "trasua/pages/order");
        model.addAttribute("contentFragment", "content");
        return "layout";
    }

    @GetMapping("/about")
    public String aboutPage(Model model) {
        model.addAttribute("pageTitle", "Giới thiệu");
        model.addAttribute("contentTemplate", "trasua/pages/about");
        model.addAttribute("contentFragment", "content");
        return "layout";
    }

    @GetMapping("/contact")
    public String contactPage(Model model) {
        model.addAttribute("pageTitle", "Liên hệ");
        model.addAttribute("contentTemplate", "trasua/pages/contact");
        model.addAttribute("contentFragment", "content");
        return "layout";
    }

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam("id") int id, 
                            @RequestParam("quantity") int quantity, 
                            @RequestParam(value = "options", required = false, defaultValue = "") String options, 
                            HttpSession session, 
                            HttpServletRequest request) {
        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/login"; 
        }

        Product product = productRepository.findById(id).orElse(null);
        if (product != null) {
            double finalSinglePrice = product.getPrice();
            if (options != null && !options.isEmpty()) {
                if (options.contains("Trân châu hoàng kim")) { finalSinglePrice += 6000; }
                if (options.contains("Thạch sương sáo")) { finalSinglePrice += 5000; }
                if (options.contains("Kem cheese")) { finalSinglePrice += 10000; }
            }

            String customProductName = product.getName();
            if (options != null && !options.isEmpty()) {
                customProductName += " (" + options + ")";
            }

            cartService.addItem(new CartItem(
                product.getId(), customProductName, product.getImageUrl(), finalSinglePrice, quantity
            ));
        }
        
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }

    // ==========================================
    // CHỨC NĂNG SIDEBAR PHÍA KHÁCH HÀNG
    // ==========================================

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("pageTitle", "Đăng ký tài khoản");
        model.addAttribute("contentTemplate", "trasua/pages/register");
        model.addAttribute("contentFragment", "content");
        return "layout";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam("fullName") String fullName,
                               @RequestParam("username") String username,
                               @RequestParam("password") String password,
                               @RequestParam("avatarFile") org.springframework.web.multipart.MultipartFile file,
                               Model model) {
        try {
            if (userRepository.findByUsername(username) != null) {
                model.addAttribute("error", "Tên đăng nhập đã tồn tại!");
                model.addAttribute("pageTitle", "Đăng ký tài khoản");
                model.addAttribute("contentTemplate", "trasua/pages/register");
                model.addAttribute("contentFragment", "content");
                return "layout";
            }

            User user = new User();
            user.setFullName(fullName);
            user.setUsername(username);
            user.setPassword(password); 
            user.setRole("USER");

            if (file != null && !file.isEmpty()) {
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                String projectPath = System.getProperty("user.dir");
                String uploadDir = projectPath + java.io.File.separator + "src" 
                                            + java.io.File.separator + "main" 
                                                + java.io.File.separator + "resources" 
                                            + java.io.File.separator + "static" 
                                            + java.io.File.separator + "avatar";
                
                java.io.File dir = new java.io.File(uploadDir);
                if (!dir.exists()) { dir.mkdirs(); }
                
                java.nio.file.Path path = java.nio.file.Paths.get(uploadDir + java.io.File.separator + fileName);
                java.nio.file.Files.write(path, file.getBytes());
                
                user.setAvatarUrl(fileName); 
            } else {
                user.setAvatarUrl("default-avatar.png");
            }

            userRepository.save(user); 
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi tải ảnh đại diện từ máy: " + e.getMessage());
            model.addAttribute("pageTitle", "Đăng ký thành viên");
            model.addAttribute("contentTemplate", "trasua/pages/register");
            model.addAttribute("contentFragment", "content");
            return "layout";
        }
    }

    @GetMapping("/promotions")
    public String promotionsPage(Model model) {
        model.addAttribute("promotions", promotionRepository.findByIsActiveTrue());
        model.addAttribute("pageTitle", "Khuyến mại");
        model.addAttribute("contentTemplate", "trasua/pages/promotions");
        model.addAttribute("contentFragment", "content");
        return "layout";
    }

    @GetMapping("/policy")
    public String policyPage(Model model) {
        model.addAttribute("policies", policyRepository.findAll());
        model.addAttribute("pageTitle", "Điều khoản và chính sách");
        model.addAttribute("contentTemplate", "trasua/pages/policy");
        model.addAttribute("contentFragment", "content");
        return "layout";
    }

    @GetMapping("/support")
    public String supportPage(Model model) {
        model.addAttribute("pageTitle", "Hỗ trợ khách hàng");
        model.addAttribute("contentTemplate", "trasua/pages/support");
        model.addAttribute("contentFragment", "content");
        return "layout";
    }

    // ==========================================
    // KHU VỰC QUẢN TRỊ VIÊN (ADMIN CRUDS)
    // ==========================================

    @GetMapping("/admin/products")
    public String adminProductList(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || !"ADMIN".equals(user.getRole())) return "redirect:/login";

        List<Product> productList = productRepository.findAll();
        model.addAttribute("products", productList);
        model.addAttribute("pageTitle", "Hệ thống Quản lý Sản phẩm - Admin");
        model.addAttribute("contentTemplate", "trasua/pages/admin_products"); 
        model.addAttribute("contentFragment", "content");
        return "layout";
    }

    @GetMapping("/admin/product/delete/{id}")
    public String deleteProduct(@PathVariable("id") int id, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || !"ADMIN".equals(user.getRole())) return "redirect:/login";
        productRepository.deleteById(id); 
        return "redirect:/admin/products"; 
    }

    @GetMapping("/admin/product/edit/{id}")
    public String editProductForm(@PathVariable("id") int id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || !"ADMIN".equals(user.getRole())) return "redirect:/login";

        Product product = productRepository.findById(id).orElse(null);
        model.addAttribute("product", product); 
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("pageTitle", "Chỉnh sửa sản phẩm");
        model.addAttribute("contentTemplate", "trasua/pages/edit_product"); 
        model.addAttribute("contentFragment", "content");
        return "layout";
    }

    @GetMapping("/admin/product/add")
    public String showAddForm(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || !"ADMIN".equals(user.getRole())) return "redirect:/login";

        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryRepository.findAll()); 
        model.addAttribute("pageTitle", "Thêm sản phẩm mới");
        model.addAttribute("contentTemplate", "trasua/pages/edit_product");
        model.addAttribute("contentFragment", "content");
        return "layout";
    }

    @PostMapping("/admin/product/save")
    public String saveProduct(@ModelAttribute("product") Product product, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || !"ADMIN".equals(user.getRole())) return "redirect:/login";
        productRepository.save(product); 
        return "redirect:/admin/products"; 
    }

    @GetMapping("/admin/promotions")
    public String adminPromotions(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || !"ADMIN".equals(user.getRole())) return "redirect:/login";

        model.addAttribute("promotions", promotionRepository.findAll());
        model.addAttribute("pageTitle", "Hệ thống Quản lý Khuyến mại");
        model.addAttribute("contentTemplate", "trasua/pages/admin_promotions");
        model.addAttribute("contentFragment", "content");
        return "layout";
    }

    @PostMapping("/admin/promotion/save")
    public String savePromotion(@RequestParam("code") String code,
                                @RequestParam("title") String title,
                                @RequestParam("description") String description,
                                @RequestParam("discountPercent") int discountPercent,
                                @RequestParam("startDate") String startDate,
                                @RequestParam("endDate") String endDate,
                                HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || !"ADMIN".equals(user.getRole())) return "redirect:/login";

        Promotion promo = new Promotion();
        promo.setCode(code.toUpperCase().trim()); 
        promo.setTitle(title);
        promo.setDescription(description);
        promo.setDiscountPercent(discountPercent);
        promo.setImageUrl("noel.jpg"); 
        promo.setStartDate(LocalDate.parse(startDate));
        promo.setEndDate(LocalDate.parse(endDate));
        promo.setActive(true); 

        promotionRepository.save(promo);
        return "redirect:/admin/promotions";
    }

    @GetMapping("/admin/promotion/toggle/{id}")
    public String togglePromotion(@PathVariable("id") int id, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || !"ADMIN".equals(user.getRole())) return "redirect:/login";

        Promotion promo = promotionRepository.findById(id).orElse(null);
        if (promo != null) {
            promo.setActive(!promo.isActive());
            promotionRepository.save(promo);
        }
        return "redirect:/admin/promotions";
    }

    @GetMapping("/admin/promotion/delete/{id}")
    public String deletePromotion(@PathVariable("id") int id, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || !"ADMIN".equals(user.getRole())) return "redirect:/login";

        promotionRepository.deleteById(id);
        return "redirect:/admin/promotions";
    }

    @GetMapping("/admin/orders")
    public String adminOrderDashboard(@RequestParam(value = "status", required = false) String status, 
                                      Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || !"ADMIN".equals(user.getRole())) return "redirect:/login";

        List<Order> allOrders;
        if (status != null && !status.isEmpty() && !"ALL".equalsIgnoreCase(status)) {
            allOrders = orderRepository.findAll().stream()
                .filter(o -> status.toUpperCase().equals(o.getStatus()))
                .sorted((o1, o2) -> Integer.compare(o2.getId(), o1.getId())) 
                .toList();
            model.addAttribute("currentFilter", status.toUpperCase());
        } else {
            allOrders = orderRepository.findAll().stream()
                .sorted((o1, o2) -> Integer.compare(o2.getId(), o1.getId()))
                .toList();
            model.addAttribute("currentFilter", "ALL");
        }

        model.addAttribute("orders", allOrders);
        model.addAttribute("pageTitle", "Hệ thống Quản lý Đơn hàng - Admin");
        model.addAttribute("contentTemplate", "trasua/pages/admin_orders");
        model.addAttribute("contentFragment", "content");
        return "layout";
    }

    @GetMapping("/admin/order/detail/{id}")
    public String adminOrderDetail(@PathVariable("id") int id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || !"ADMIN".equals(user.getRole())) return "redirect:/login";

        Order order = orderRepository.findById(id).orElse(null);
        if (order == null) return "redirect:/admin/orders";

        List<OrderDetail> items = orderDetailRepository.findByOrderId(order.getId());
        
        model.addAttribute("order", order);
        model.addAttribute("orderItems", items);
        model.addAttribute("pageTitle", "Chi tiết đơn hàng #" + order.getId());
        model.addAttribute("contentTemplate", "trasua/pages/admin_order_detail");
        model.addAttribute("contentFragment", "content");
        return "layout";
    }

    @GetMapping("/admin/order/update-status/{id}")
    public String updateOrderStatus(@PathVariable("id") int id, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || !"ADMIN".equals(user.getRole())) return "redirect:/login";

        Order order = orderRepository.findById(id).orElse(null);
        if (order != null) {
            String currentStatus = order.getStatus();
            if ("CHỜ XÁC NHẬN".equals(currentStatus)) {
                order.setStatus("ĐANG CHUẨN BỊ");
            } else if ("ĐANG CHUẨN BỊ".equals(currentStatus)) {
                order.setStatus("ĐANG GIAO HÀNG");
            } else if ("ĐANG GIAO HÀNG".equals(currentStatus)) {
                order.setStatus("HOÀN THÀNH");
            }
            orderRepository.save(order); 
            return "redirect:/admin/order/detail/" + id; 
        }
        return "redirect:/admin/orders";
    }

    @GetMapping("/admin/order/cancel/{id}")
    public String adminCancelOrder(@PathVariable("id") int id, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || !"ADMIN".equals(user.getRole())) return "redirect:/login";

        Order order = orderRepository.findById(id).orElse(null);
        if (order != null && ("CHỜ XÁC NHẬN".equals(order.getStatus()) || "ĐANG CHUẨN BỊ".equals(order.getStatus()))) {
            order.setStatus("ĐÃ HỦY");
            orderRepository.save(order);
        }
        return "redirect:/admin/order/detail/" + id;
    }

    //QUẢN LÝ ĐIỀU KHOẢN & CHÍNH SÁCH (CRUD)
    @GetMapping("/admin/policies")
    public String adminPolicies(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || !"ADMIN".equals(user.getRole())) return "redirect:/login";
        
        model.addAttribute("policies", policyRepository.findAll());
        model.addAttribute("pageTitle", "Quản lý điều khoản chính sách");
        model.addAttribute("contentTemplate", "trasua/pages/admin_policies");
        model.addAttribute("contentFragment", "content");
        return "layout";
    }

    @PostMapping("/admin/policy/save")
    public String savePolicy(@RequestParam(value = "id", required = false) Integer id,
                             @RequestParam("title") String title,
                             @RequestParam("content") String content, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || !"ADMIN".equals(user.getRole())) return "redirect:/login";

        Policy policy = (id != null) ? policyRepository.findById(id).orElse(new Policy()) : new Policy();
        policy.setTitle(title);
        policy.setContent(content);
        policyRepository.save(policy);
        return "redirect:/admin/policies";
    }

    @GetMapping("/admin/policy/delete/{id}")
    public String deletePolicy(@PathVariable("id") int id, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || !"ADMIN".equals(user.getRole())) return "redirect:/login";
        policyRepository.deleteById(id);
        return "redirect:/admin/policies";
    }

    //TRUNG TÂM TIẾP NHẬN HỖ TRỢ & KHIẾU NẠI KHÁCH HÀNG
    @GetMapping("/admin/supports")
    public String adminSupports(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || !"ADMIN".equals(user.getRole())) return "redirect:/login";

        model.addAttribute("complaints", supportRepository.findAll());
        model.addAttribute("pageTitle", "Trung tâm tiếp nhận khiếu nại");
        model.addAttribute("contentTemplate", "trasua/pages/admin_supports");
        model.addAttribute("contentFragment", "content");
        return "layout";
    }

    @GetMapping("/admin/support/resolve/{id}")
    public String resolveSupport(@PathVariable("id") int id, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || !"ADMIN".equals(user.getRole())) return "redirect:/login";

        Support sp = supportRepository.findById(id).orElse(null);
        if (sp != null) {
            sp.setStatus("ĐÃ GIẢI QUYẾT");
            supportRepository.save(sp);
        }
        return "redirect:/admin/supports";
    }
    
    //THỐNG KÊ DOANH THU
    @GetMapping("/admin/revenue")
    public String adminRevenueDashboard(@RequestParam(value = "startDate", required = false) String startDateStr,
                                        @RequestParam(value = "endDate", required = false) String endDateStr,
                                        @RequestParam(value = "quickFilter", required = false, defaultValue = "MONTH") String quickFilter,
                                        Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || !"ADMIN".equals(user.getRole())) return "redirect:/login";

        LocalDate startDate = LocalDate.now().withDayOfMonth(1);
        LocalDate endDate = LocalDate.now();

        if (startDateStr != null && !startDateStr.isEmpty() && endDateStr != null && !endDateStr.isEmpty()) {
            startDate = LocalDate.parse(startDateStr);
            endDate = LocalDate.parse(endDateStr);
            quickFilter = "CUSTOM";
        } else if ("TODAY".equalsIgnoreCase(quickFilter)) {
            startDate = LocalDate.now();
            endDate = LocalDate.now();
        } else if ("YEAR".equalsIgnoreCase(quickFilter)) {
            startDate = LocalDate.now().withDayOfYear(1);
            endDate = LocalDate.now();
        }

        final LocalDate finalStart = startDate;
        final LocalDate finalEnd = endDate;
        
        List<Order> successOrders = orderRepository.findAll().stream()
            .filter(o -> "HOÀN THÀNH".equalsIgnoreCase(o.getStatus()))
            .filter(o -> {
                LocalDate orderDate = o.getCreatedAt().toLocalDate();
                return !orderDate.isBefore(finalStart) && !orderDate.isAfter(finalEnd);
            })
            .toList();

        double totalRevenue = successOrders.stream().mapToDouble(Order::getTotalPrice).sum();
        long totalOrders = successOrders.size();
        
        long totalProductsSold = successOrders.stream()
            .mapToLong(o -> orderDetailRepository.findByOrderId(o.getId()).stream()
                .mapToLong(OrderDetail::getQuantity).sum())
            .sum();

        java.util.Map<String, Double> revenueMap = new java.util.TreeMap<>();
        LocalDate tempDate = startDate;
        while (!tempDate.isAfter(endDate)) {
            revenueMap.put(tempDate.toString(), 0.0);
            tempDate = tempDate.plusDays(1);
        }
        for (Order o : successOrders) {
            String dateKey = o.getCreatedAt().toLocalDate().toString();
            revenueMap.put(dateKey, revenueMap.getOrDefault(dateKey, 0.0) + o.getTotalPrice());
        }

        com.example.demo.model.RevenueDTO dto = new com.example.demo.model.RevenueDTO();
        dto.setTotalRevenue(totalRevenue);
        dto.setTotalOrders(totalOrders);
        dto.setTotalProductsSold(totalProductsSold);
        dto.setChartLabels(revenueMap.keySet().stream().toList());
        dto.setChartData(revenueMap.values().stream().toList());

        model.addAttribute("data", dto);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("quickFilter", quickFilter);
        
        model.addAttribute("pageTitle", "Báo cáo doanh thu | Soranchi Admin");
        model.addAttribute("contentTemplate", "trasua/pages/admin_revenue");
        model.addAttribute("contentFragment", "content");
        return "layout";
    }
    
    //Giao diện xem danh sách toàn bộ danh mục đang có (FULL CRUD TRÊN MODAL POPUP)
    @GetMapping("/admin/categories")
    public String adminCategoryList(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || !"ADMIN".equals(user.getRole())) return "redirect:/login";

        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("pageTitle", "Quản lý danh mục | Soranchi Admin");
        model.addAttribute("contentTemplate", "trasua/pages/admin_categories");
        model.addAttribute("contentFragment", "content");
        return "layout";
    }
    
    //Xử lý lưu dữ liệu danh mục (Dùng chung cho cả THÊM MỚI, CẬP NHẬT SỬA)
    @PostMapping("/admin/category/save")
    public String saveCategory(@RequestParam(value = "id", required = false) Integer id,
                               @RequestParam("name") String name,
                               @RequestParam("alias") String alias, 
                               @RequestParam(value = "status", required = false, defaultValue = "ACTIVE") String status,
                               HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || !"ADMIN".equals(user.getRole())) return "redirect:/login";

        Category cat = (id != null) ? categoryRepository.findById(id).orElse(new Category()) : new Category();
        cat.setName(name);
        cat.setAlias(alias.toLowerCase().trim());
        cat.setStatus(status); 
        categoryRepository.save(cat); 
        
        return "redirect:/admin/categories";
    }
    
    //Xử lý xóa danh mục an toàn có try-catch chống sập dữ liệu
    @GetMapping("/admin/category/delete/{id}")
    public String deleteCategory(@PathVariable("id") int id, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || !"ADMIN".equals(user.getRole())) return "redirect:/login";
        
        try {
            categoryRepository.deleteById(id); 
        } catch (Exception e) {
            return "redirect:/admin/categories?error=foreign_key";
        }
        return "redirect:/admin/categories";
    }
    
    // ==========================================
    // PHÂN HỆ KHÁCH HÀNG
    // ==========================================

    @PostMapping("/checkout/apply-coupon")
    public String applyCouponAction(@RequestParam("couponCode") String couponCode, 
                               HttpSession session, HttpServletRequest request) {
        Promotion promo = promotionRepository.findAll().stream()
            .filter(p -> p.getCode().equalsIgnoreCase(couponCode.trim()) && p.isActive() && !p.isExpired())
            .findFirst().orElse(null);

        if (promo != null) {
            double originalTotal = cartService.getTotalPrice();
            double discountAmount = originalTotal * (promo.getDiscountPercent() / 100.0);
            double finalPrice = originalTotal - discountAmount;

            session.setAttribute("appliedPromoCode", promo.getCode());
            session.setAttribute("discountPercent", promo.getDiscountPercent());
            session.setAttribute("discountAmount", discountAmount);
            session.setAttribute("finalPrice", finalPrice);
            session.removeAttribute("promoError");
        } else {
            session.setAttribute("promoError", "Mã giảm giá không hợp lệ hoặc đã hết hạn dùng!");
            session.removeAttribute("appliedPromoCode");
            session.removeAttribute("discountPercent");
            session.removeAttribute("discountAmount");
            session.removeAttribute("finalPrice");
        }

        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/checkout");
    }

    @PostMapping("/checkout/submit")
    public String processOrderAction(@RequestParam("fullName") String fullName,
                               @RequestParam("phone") String phone,
                               @RequestParam("address") String address,
                               @RequestParam(value = "paymentMethod", required = false, defaultValue = "COD") String paymentMethod, 
                               HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";
        if (cartService.getItems().isEmpty()) return "redirect:/products";

        Order order = new Order();
        order.setUserId(user.getId());
        order.setFullName(fullName);
        order.setPhone(phone);
        order.setAddress(address);
        order.setPaymentMethod(paymentMethod); 
        order.setTotalPrice(session.getAttribute("finalPrice") != null ? (double) session.getAttribute("finalPrice") : cartService.getTotalPrice());
        orderRepository.save(order);

        for (CartItem item : cartService.getItems()) {
            OrderDetail detail = new OrderDetail();
            detail.setOrderId(order.getId());
            detail.setProductId(item.getProductId());
            detail.setProductName(item.getName());
            detail.setImageUrl(item.getImageUrl());
            detail.setPrice(item.getPrice());
            detail.setQuantity(item.getQuantity());
            orderDetailRepository.save(detail);
        }

        cartService.clear(); 
        session.removeAttribute("appliedPromoCode");
        session.removeAttribute("discountAmount");
        session.removeAttribute("finalPrice");

        model.addAttribute("successOrderId", "SRC" + order.getId());
        model.addAttribute("pageTitle", "Đặt hàng thành công");
        model.addAttribute("contentTemplate", "trasua/pages/order_success");
        model.addAttribute("contentFragment", "content");
        return "layout";
    }

    @GetMapping("/my-orders")
    public String myOrdersPage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        List<Order> orders = orderRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
        model.addAttribute("orders", orders);
        model.addAttribute("detailRepo", orderDetailRepository);

        model.addAttribute("pageTitle", "Đơn hàng của tôi");
        model.addAttribute("contentTemplate", "trasua/pages/my_orders");
        model.addAttribute("contentFragment", "content");
        return "layout";
    }

    @GetMapping("/order/cancel/{id}")
    public String cancelOrder(@PathVariable("id") int id, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        Order order = orderRepository.findById(id).orElse(null);
        if (order != null && order.getUserId() == user.getId() && "CHỜ XÁC NHẬN".equals(order.getStatus())) {
            order.setStatus("ĐÃ HỦY");
            orderRepository.save(order);
        }
        return "redirect:/my-orders";
    }

    @GetMapping("/profile")
    public String profilePage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "Thông tin cá nhân");
        model.addAttribute("contentTemplate", "trasua/pages/profile");
        model.addAttribute("contentFragment", "content");
        return "layout";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@RequestParam("fullName") String fullName,
                                @RequestParam("phone") String phone,
                                @RequestParam("address") String address,
                                HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        user.setFullName(fullName);
        user.setPhone(phone);
        user.setAddress(address);
        userRepository.save(user);

        session.setAttribute("loggedInUser", user);
        return "redirect:/profile?success=true";
    }

    @PostMapping("/cart/update")
    public String updateCartQuantity(@RequestParam("id") int id, @RequestParam("action") String action) {
        CartItem item = cartItemRepository.findById(id).orElse(null);
        if (item != null) {
            int newQty = action.equals("plus") ? item.getQuantity() + 1 : item.getQuantity() - 1;
            cartService.updateQuantity(id, newQty);
        }
        return "redirect:/order"; 
    }

    @GetMapping("/cart/remove/{id}")
    public String removeFromCart(@PathVariable("id") int id) {
        cartService.removeItem(id);
        return "redirect:/order"; 
    }
}