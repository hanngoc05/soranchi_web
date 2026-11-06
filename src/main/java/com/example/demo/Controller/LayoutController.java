package com.example.demo.Controller;

import com.example.demo.model.RevenueDTO;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.TreeMap;
import com.example.demo.model.CartItem;
import com.example.demo.model.Product;
import com.example.demo.model.Review;
import com.example.demo.model.User; 
import com.example.demo.model.Promotion;
import com.example.demo.model.Order;
import com.example.demo.model.OrderDetail;
import com.example.demo.model.PaymentTransaction;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @Autowired
    private com.example.demo.repository.PaymentTransactionRepository paymentTransactionRepository;

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
                            HttpServletRequest request,
                            RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login"; 
        }

        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return "redirect:/products";
        }

        // 1. Kiểm tra nếu sản phẩm đã ngừng kinh doanh
        if ("NGỪNG BÁN".equalsIgnoreCase(product.getStatus())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Sản phẩm này hiện đang ngừng bán!");
            return "redirect:/product/" + id;
        }

        // 2. Kiểm tra nếu kho đã hết hàng hoàn toàn
        if (product.getQuantity() <= 0) {
            redirectAttributes.addFlashAttribute("errorMessage", "Sản phẩm '" + product.getName() + "' hiện đã tạm hết hàng trong kho!");
            return "redirect:/product/" + id;
        }

        // 3. Tính tổng số lượng sản phẩm này đã có trong giỏ của user (bao gồm cả các loại topping khác nhau)
        List<CartItem> userCartItems = cartItemRepository.findByUserId(user.getId());
        int currentQtyInCart = userCartItems.stream()
                .filter(item -> item.getProductId() == id)
                .mapToInt(CartItem::getQuantity)
                .sum();

        int totalRequested = currentQtyInCart + quantity;

        // 4. Chặn nếu vượt quá số lượng tồn kho và bắn Flash message hiển thị lên màn hình
        if (totalRequested > product.getQuantity()) {
            if (currentQtyInCart > 0) {
                redirectAttributes.addFlashAttribute("errorMessage", 
                    "Không thể thêm! Kho chỉ còn " + product.getQuantity() + " ly (Trong giỏ hàng bạn đã có sẵn " + currentQtyInCart + " ly).");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", 
                    "Không thể thêm! Số lượng yêu cầu (" + quantity + " ly) vượt quá số lượng tồn kho hiện có (" + product.getQuantity() + " ly).");
            }
            return "redirect:/product/" + id;
        }

        // 5. Tính giá theo Topping
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

        // 6. Thêm vào giỏ hàng
        cartService.addItem(new CartItem(
            user.getId(), product.getId(), customProductName, product.getImageUrl(), finalSinglePrice, quantity
        ));
        
        // 7. Thông báo thành công và giữ người dùng ở lại trang chi tiết món
        redirectAttributes.addFlashAttribute("successMessage", "Đã thêm " + quantity + " ly '" + product.getName() + "' vào giỏ hàng!");
        return "redirect:/product/" + id;
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
    // KHU VỰC QUẢN TRỊ VIÊN: QUẢN LÝ SẢN PHẨM
    // ==========================================

    @GetMapping("/admin/products")
    public String adminProductList(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

        try {
            List<Product> productList = productRepository.findAll();
            model.addAttribute("products", productList);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Không thể kết nối cơ sở dữ liệu để tải danh sách sản phẩm!");
        }

        model.addAttribute("pageTitle", "Hệ thống Quản lý Sản phẩm - Admin");
        model.addAttribute("contentTemplate", "trasua/pages/admin_products"); 
        model.addAttribute("contentFragment", "content");
        return "layout";
    }

    @GetMapping("/admin/product/add")
    public String showAddForm(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

        Product product = new Product();
        product.setStatus("ĐANG BÁN");
        product.setQuantity(50);
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryRepository.findAll()); 
        model.addAttribute("pageTitle", "Thêm sản phẩm mới");
        model.addAttribute("contentTemplate", "trasua/pages/edit_product");
        model.addAttribute("contentFragment", "content");
        return "layout";
    }

    @GetMapping("/admin/product/edit/{id}")
    public String editProductForm(@PathVariable("id") int id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return "redirect:/admin/products";
        }

        model.addAttribute("product", product); 
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("pageTitle", "Chỉnh sửa sản phẩm");
        model.addAttribute("contentTemplate", "trasua/pages/edit_product"); 
        model.addAttribute("contentFragment", "content");
        return "layout";
    }

    @PostMapping("/admin/product/save")
    public String saveProduct(@ModelAttribute("product") Product product,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

        try {
            if (product.getProductCode() == null || product.getProductCode().trim().isEmpty()) {
                product.setProductCode("SP" + System.currentTimeMillis() % 100000);
            } else {
                product.setProductCode(product.getProductCode().trim().toUpperCase());
            }

            Product existingProduct = productRepository.findByProductCode(product.getProductCode());
            if (product.getId() == 0) { 
                if (existingProduct != null) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Từ chối tạo mới: Mã sản phẩm '" + product.getProductCode() + "' đã tồn tại!");
                    return "redirect:/admin/product/add";
                }
                if (product.getStatus() == null || product.getStatus().isEmpty()) {
                    product.setStatus("ĐANG BÁN");
                }
                productRepository.save(product);
                redirectAttributes.addFlashAttribute("successMessage", "Thêm sản phẩm '" + product.getName() + "' thành công!");
            } else { 
                if (existingProduct != null && existingProduct.getId() != product.getId()) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Mã sản phẩm '" + product.getProductCode() + "' đã tồn tại ở sản phẩm khác!");
                    return "redirect:/admin/product/edit/" + product.getId();
                }
                productRepository.save(product);
                redirectAttributes.addFlashAttribute("successMessage", "Cập nhật sản phẩm '" + product.getName() + "' thành công!");
            }

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi kết nối cơ sở dữ liệu! Không thể lưu thông tin sản phẩm.");
        }

        return "redirect:/admin/products"; 
    }

    @GetMapping("/admin/product/delete/{id}")
    public String deleteProduct(@PathVariable("id") int id, 
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

        try {
            Product product = productRepository.findById(id).orElse(null);
            if (product != null) {
                boolean hasOrder = orderDetailRepository.findAll().stream().anyMatch(d -> d.getProductId() == id);
                boolean hasCart = cartItemRepository.findAll().stream().anyMatch(c -> c.getProductId() == id);

                if (hasOrder || hasCart) {
                    product.setStatus("NGỪNG BÁN");
                    productRepository.save(product);
                    redirectAttributes.addFlashAttribute("errorMessage", "Sản phẩm '" + product.getName() + "' đã phát sinh đơn hàng hoặc trong giỏ hàng nên hệ thống đã tự động chuyển sang trạng thái 'NGỪNG BÁN'.");
                } else {
                    productRepository.deleteById(id);
                    redirectAttributes.addFlashAttribute("successMessage", "Xóa sản phẩm thành công!");
                }
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi kết nối cơ sở dữ liệu! Không thể thực hiện xóa.");
        }

        return "redirect:/admin/products"; 
    }

    // ==========================================
    // KHU VỰC QUẢN TRỊ VIÊN: QUẢN LÝ KHUYẾN MẠI
    // ==========================================

    @GetMapping("/admin/promotions")
    public String adminPromotions(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

        try {
            model.addAttribute("promotions", promotionRepository.findAll());
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Không thể kết nối với cơ sở dữ liệu để lấy danh sách khuyến mãi!");
        }

        model.addAttribute("pageTitle", "Hệ thống Quản lý Khuyến mại");
        model.addAttribute("contentTemplate", "trasua/pages/admin_promotions");
        model.addAttribute("contentFragment", "content");
        return "layout";
    }

    @PostMapping("/admin/promotion/save")
    public String savePromotion(@RequestParam(value = "id", required = false) Integer id,
                                @RequestParam("code") String code,
                                @RequestParam("title") String title,
                                @RequestParam("description") String description,
                                @RequestParam("discountPercent") int discountPercent,
                                @RequestParam("startDate") String startDateStr,
                                @RequestParam("endDate") String endDateStr,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

        try {
            String cleanCode = code.toUpperCase().trim().replaceAll("\\s+", "");
            LocalDate start = LocalDate.parse(startDateStr);
            LocalDate end = LocalDate.parse(endDateStr);

            if (start.isAfter(end)) {
                redirectAttributes.addFlashAttribute("errorMessage", "Lỗi dữ liệu: Thời gian bắt đầu phải nhỏ hơn hoặc bằng thời gian kết thúc!");
                return "redirect:/admin/promotions";
            }

            if (discountPercent < 1 || discountPercent > 100) {
                redirectAttributes.addFlashAttribute("errorMessage", "Mức giảm giá phải nằm trong khoảng từ 1% đến 100%!");
                return "redirect:/admin/promotions";
            }

            if (id == null) {
                Promotion existingPromo = promotionRepository.findByCode(cleanCode);
                if (existingPromo != null) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Thao tác bị từ chối: Mã khuyến mãi '" + cleanCode + "' đã tồn tại trên hệ thống!");
                    return "redirect:/admin/promotions";
                }
            } else {
                Promotion existingPromo = promotionRepository.findByCode(cleanCode);
                if (existingPromo != null && existingPromo.getId() != id) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Mã khuyến mãi '" + cleanCode + "' đã thuộc về một chương trình khác!");
                    return "redirect:/admin/promotions";
                }
            }

            Promotion promo = (id != null) ? promotionRepository.findById(id).orElse(new Promotion()) : new Promotion();
            promo.setCode(cleanCode);
            promo.setTitle(title.trim());
            promo.setDescription(description.trim());
            promo.setDiscountPercent(discountPercent);
            promo.setStartDate(start);
            promo.setEndDate(end);
            if (id == null) {
                promo.setImageUrl("noel.jpg");
                promo.setActive(true);
            }

            promotionRepository.save(promo);
            redirectAttributes.addFlashAttribute("successMessage", (id == null ? "Thêm mới" : "Cập nhật") + " chương trình khuyến mại '" + cleanCode + "' thành công!");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi kết nối cơ sở dữ liệu! Không thể lưu thông tin khuyến mại.");
        }

        return "redirect:/admin/promotions";
    }

    @GetMapping("/admin/promotion/toggle/{id}")
    public String togglePromotion(@PathVariable("id") int id, HttpSession session,
                                  RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

        try {
            Promotion promo = promotionRepository.findById(id).orElse(null);
            if (promo != null) {
                promo.setActive(!promo.isActive());
                promotionRepository.save(promo);
                redirectAttributes.addFlashAttribute("successMessage", "Cập nhật trạng thái khuyến mại thành công!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi cơ sở dữ liệu! Không thể cập nhật trạng thái.");
        }

        return "redirect:/admin/promotions";
    }

    @GetMapping("/admin/promotion/delete/{id}")
    public String deletePromotion(@PathVariable("id") int id, HttpSession session,
                                  RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

        try {
            Promotion promo = promotionRepository.findById(id).orElse(null);
            if (promo != null) {
                if (promo.isActive() && !promo.isExpired()) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Từ chối thao tác: Chương trình khuyến mại '" + promo.getCode() + "' đang được kích hoạt sử dụng! Vui lòng tạm dừng (Pause) trước khi xóa.");
                    return "redirect:/admin/promotions";
                }
                promotionRepository.deleteById(id);
                redirectAttributes.addFlashAttribute("successMessage", "Xóa chương trình khuyến mại thành công!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi kết nối cơ sở dữ liệu hoặc chương trình đang có dữ liệu liên kết! Không thể xóa.");
        }

        return "redirect:/admin/promotions";
    }

    // ==========================================
    // KHU VỰC QUẢN TRỊ VIÊN: QUẢN LÝ ĐƠN HÀNG & BÁO CÁO
    // ==========================================

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

        Map<String, Double> revenueMap = new TreeMap<>();
        LocalDate tempDate = startDate;
        while (!tempDate.isAfter(endDate)) {
            revenueMap.put(tempDate.toString(), 0.0);
            tempDate = tempDate.plusDays(1);
        }
        for (Order o : successOrders) {
            String dateKey = o.getCreatedAt().toLocalDate().toString();
            revenueMap.put(dateKey, revenueMap.getOrDefault(dateKey, 0.0) + o.getTotalPrice());
        }

        RevenueDTO dto = new RevenueDTO();
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
    // PHÂN HỆ KHÁCH HÀNG: CHECKOUT & THANH TOÁN
    // ==========================================

    @GetMapping("/checkout")
    public String showCheckoutPage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login"; 
        if (cartService.getItems(user.getId()).isEmpty()) return "redirect:/order"; 

        double originalTotal = cartService.getTotalPrice(user.getId());
        double finalPrice = session.getAttribute("finalPrice") != null ? (double) session.getAttribute("finalPrice") : originalTotal;
        double discountAmount = session.getAttribute("discountAmount") != null ? (double) session.getAttribute("discountAmount") : 0.0;

        model.addAttribute("cartItems", cartService.getItems(user.getId()));
        model.addAttribute("totalPrice", originalTotal);
        model.addAttribute("discountAmount", discountAmount);
        model.addAttribute("finalPrice", finalPrice);
        model.addAttribute("user", user);

        model.addAttribute("pageTitle", "Thanh toán đơn hàng - Soranchi");
        model.addAttribute("contentTemplate", "trasua/pages/checkout"); 
        model.addAttribute("contentFragment", "content");
        return "layout";
    }

    @PostMapping("/checkout/apply-coupon")
    public String applyCouponAction(@RequestParam("couponCode") String couponCode, 
                                    HttpSession session, HttpServletRequest request) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        Promotion promo = promotionRepository.findAll().stream()
            .filter(p -> p.getCode().equalsIgnoreCase(couponCode.trim()) && p.isActive() && !p.isExpired())
            .findFirst().orElse(null);

        if (promo != null) {
            double originalTotal = cartService.getTotalPrice(user.getId());
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

// 1. Khởi tạo đơn hàng & tạo bản ghi giao dịch
    @PostMapping("/api/checkout/create-order")
    @ResponseBody
    public Map<String, Object> createOrderAjax(@RequestParam("fullName") String fullName,
                                              @RequestParam("phone") String phone,
                                              @RequestParam("address") String address,
                                              @RequestParam(value = "paymentMethod", defaultValue = "COD") String paymentMethod,
                                              HttpSession session) {
        Map<String, Object> response = new java.util.HashMap<>();
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            response.put("success", false);
            response.put("message", "Vui lòng đăng nhập để tiến hành thanh toán!");
            return response;
        }

        List<CartItem> cartItems = cartService.getItems(user.getId());
        if (cartItems.isEmpty()) {
            response.put("success", false);
            response.put("message", "Giỏ hàng của bạn đang trống!");
            return response;
        }

        String cleanPhone = phone != null ? phone.trim().replaceAll("[^0-9]", "") : "";
        if (cleanPhone.length() < 9 || cleanPhone.length() > 11) {
            response.put("success", false);
            response.put("message", "Số điện thoại không hợp lệ! Vui lòng nhập từ 9 đến 11 chữ số.");
            return response;
        }

        try {
            double finalPrice = session.getAttribute("finalPrice") != null ? 
                                (double) session.getAttribute("finalPrice") : cartService.getTotalPrice(user.getId());

            Order order = new Order();
            order.setUserId(user.getId());
            order.setFullName(fullName.trim());
            order.setPhone(cleanPhone);
            order.setAddress(address.trim());
            order.setPaymentMethod(paymentMethod);
            order.setTotalPrice(finalPrice);
            order.setStatus("BANK".equalsIgnoreCase(paymentMethod) ? "CHỜ THANH TOÁN" : "CHỜ XÁC NHẬN");
            orderRepository.save(order);

            for (CartItem item : cartItems) {
                OrderDetail detail = new OrderDetail();
                detail.setOrderId(order.getId());
                detail.setProductId(item.getProductId());
                detail.setProductName(item.getName());
                detail.setImageUrl(item.getImageUrl());
                detail.setPrice(item.getPrice());
                detail.setQuantity(item.getQuantity());
                orderDetailRepository.save(detail);
            }

            PaymentTransaction transaction = new PaymentTransaction();
            transaction.setOrderId(order.getId());
            transaction.setAmount(finalPrice);
            transaction.setPaymentMethodId("BANK".equalsIgnoreCase(paymentMethod) ? 2 : 1);
            transaction.setStatus("BANK".equalsIgnoreCase(paymentMethod) ? "PENDING" : "SUCCESS");
            transaction.setResponseCode("TRANS_" + System.currentTimeMillis());
            paymentTransactionRepository.save(transaction);

            // Nếu COD: Trừ tồn kho và xóa giỏ luôn
            if ("COD".equalsIgnoreCase(paymentMethod)) {
                for (CartItem item : cartItems) {
                    Product product = productRepository.findById(item.getProductId()).orElse(null);
                    if (product != null) {
                        product.setQuantity(Math.max(0, product.getQuantity() - item.getQuantity()));
                        productRepository.save(product);
                    }
                }
                cartService.clear(user.getId());
                session.removeAttribute("appliedPromoCode");
                session.removeAttribute("discountPercent");
                session.removeAttribute("discountAmount");
                session.removeAttribute("finalPrice");
            }

            response.put("success", true);
            response.put("orderId", order.getId());
            response.put("finalPrice", finalPrice);
            response.put("paymentMethod", paymentMethod);
            return response;

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi cơ sở dữ liệu! Không thể tạo đơn hàng.");
            return response;
        }
    }

    // 2. Polling kiểm tra trạng thái đơn hàng
    @GetMapping("/api/checkout/check-status/{orderId}")
    @ResponseBody
    public Map<String, Object> checkOrderStatus(@PathVariable("orderId") int orderId) {
        Map<String, Object> response = new java.util.HashMap<>();
        Order order = orderRepository.findById(orderId).orElse(null);
        response.put("paid", order != null && "ĐÃ THANH TOÁN".equalsIgnoreCase(order.getStatus()));
        return response;
    }

    // 3. Webhook / Xác nhận thanh toán trực tuyến thành công
    @PostMapping("/api/checkout/simulate-ipn/{orderId}")
    @ResponseBody
    public String simulatePaymentSuccess(@PathVariable("orderId") int orderId) {
        try {
            Order order = orderRepository.findById(orderId).orElse(null);
            if (order != null && !"ĐÃ THANH TOÁN".equalsIgnoreCase(order.getStatus())) {
                order.setStatus("ĐÃ THANH TOÁN");
                orderRepository.save(order);

                PaymentTransaction transaction = paymentTransactionRepository.findByOrderId(orderId);
                if (transaction != null) {
                    transaction.setStatus("SUCCESS");
                    transaction.setTransactionTime(LocalDateTime.now());
                    paymentTransactionRepository.save(transaction);
                }

                // Trừ tồn kho
                List<OrderDetail> details = orderDetailRepository.findByOrderId(orderId);
                for (OrderDetail detail : details) {
                    Product product = productRepository.findById(detail.getProductId()).orElse(null);
                    if (product != null) {
                        product.setQuantity(Math.max(0, product.getQuantity() - detail.getQuantity()));
                        productRepository.save(product);
                    }
                }

                // Làm rỗng giỏ hàng
                cartService.clear(order.getUserId());
                return "SUCCESS";
            }
        } catch (Exception e) {
            return "ERROR";
        }
        return "ALREADY_PAID";
    }

    // 4. Trang thông báo đặt hàng thành công
    @GetMapping("/order-success/{orderId}")
    public String orderSuccessPage(@PathVariable("orderId") int orderId, Model model, HttpSession session) {
        session.removeAttribute("appliedPromoCode");
        session.removeAttribute("discountPercent");
        session.removeAttribute("discountAmount");
        session.removeAttribute("finalPrice");

        model.addAttribute("successOrderId", "SRC" + orderId);
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

    // ==========================================
    // QUẢN LÝ GIỎ HÀNG
    // ==========================================

    @GetMapping("/order")
    public String orderPage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }

        try {
            model.addAttribute("cartItems", cartService.getItems(user.getId()));
            model.addAttribute("totalPrice", cartService.getTotalPrice(user.getId()));
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Không thể kết nối cơ sở dữ liệu để tải giỏ hàng!");
            model.addAttribute("cartItems", java.util.Collections.emptyList());
            model.addAttribute("totalPrice", 0.0);
        }

        model.addAttribute("pageTitle", "Giỏ hàng của bạn");
        model.addAttribute("contentTemplate", "trasua/pages/order");
        model.addAttribute("contentFragment", "content");
        return "layout";
    }

    @PostMapping("/cart/update")
    public String updateCartQuantity(@RequestParam("id") int id, 
                                     @RequestParam("action") String action,
                                     HttpSession session,
                                     RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }

        try {
            CartItem item = cartItemRepository.findById(id).orElse(null);
            if (item != null && item.getUserId() == user.getId()) {
                Product product = productRepository.findById(item.getProductId()).orElse(null);
                int stock = (product != null) ? product.getQuantity() : 999;

                if ("plus".equalsIgnoreCase(action)) {
                    int newQty = item.getQuantity() + 1;
                    if (newQty > stock) {
                        item.setQuantity(stock);
                        cartItemRepository.save(item);
                        redirectAttributes.addFlashAttribute("errorMessage", 
                            "Số lượng sản phẩm vượt quá số lượng còn lại trong kho (Tối đa: " + stock + ")!");
                    } else {
                        item.setQuantity(newQty);
                        cartItemRepository.save(item);
                    }
                } else if ("minus".equalsIgnoreCase(action)) {
                    int newQty = item.getQuantity() - 1;
                    if (newQty <= 0) {
                        cartService.removeItem(id);
                        redirectAttributes.addFlashAttribute("successMessage", "Đã xóa sản phẩm khỏi giỏ hàng.");
                    } else {
                        item.setQuantity(newQty);
                        cartItemRepository.save(item);
                    }
                }
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi kết nối cơ sở dữ liệu! Không thể cập nhật giỏ hàng.");
        }

        return "redirect:/order"; 
    }

    @GetMapping("/cart/remove/{id}")
    public String removeFromCart(@PathVariable("id") int id, 
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }

        try {
            CartItem item = cartItemRepository.findById(id).orElse(null);
            if (item != null && item.getUserId() == user.getId()) {
                cartService.removeItem(id);
                redirectAttributes.addFlashAttribute("successMessage", "Đã xóa sản phẩm khỏi giỏ hàng thành công!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi kết nối cơ sở dữ liệu! Không thể xóa sản phẩm.");
        }

        return "redirect:/order"; 
    }

    @GetMapping("/cart/clear")
    public String clearAllCart(HttpSession session,
                               RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }

        try {
            cartService.clear(user.getId());
            redirectAttributes.addFlashAttribute("successMessage", "Đã xóa toàn bộ sản phẩm trong giỏ hàng thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi cơ sở dữ liệu! Không thể làm rỗng giỏ hàng.");
        }

        return "redirect:/order";
    }
}