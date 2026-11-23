package com.example.demo.Controller;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Controller
public class LayoutController {

    @Autowired private ProductRepository productRepository;
    @Autowired private PromotionRepository promotionRepository;
    @Autowired private CartService cartService;
    @Autowired private CartItemRepository cartItemRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private ReviewRepository reviewRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private OrderRepository orderRepository;
    @Autowired private OrderDetailRepository orderDetailRepository;
    @Autowired private PaymentTransactionRepository paymentTransactionRepository;
    @Autowired private ShippingUnitRepository shippingUnitRepository;
    @Autowired private ToppingRepository toppingRepository;
    @Autowired private KichCoRepository kichCoRepository;
    @Autowired private HinhAnhRepository hinhAnhRepository;
    @Autowired private ChiTietGioHangToppingRepository chiTietGioHangToppingRepository;
    @Autowired private ChiTietDonHangToppingRepository chiTietDonHangToppingRepository;
    @Autowired private PhuongThucThanhToanRepository phuongThucThanhToanRepository;
    
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
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("activeToppings", toppingRepository.findByStatus("Đang bán"));
        model.addAttribute("pageTitle", "Tất Cả Sản Phẩm");
        model.addAttribute("contentTemplate", "trasua/pages/list");
        model.addAttribute("contentFragment", "content");
        return "layout";
    }

    @GetMapping("/category/{alias}")
    public String filterByCategory(@PathVariable("alias") String alias, Model model) {
        String categoryName = switch (alias) {
            case "trasua" -> "Trà sữa";
            case "tra" -> "Trà trái cây";
            case "anvat" -> "Đồ ăn vặt";
            default -> "";
        };
        if (categoryName.isEmpty()) return "redirect:/products";

        model.addAttribute("products", productRepository.findByCategoryObj_Name(categoryName));
        model.addAttribute("activeToppings", toppingRepository.findByStatus("Đang bán"));
        model.addAttribute("pageTitle", "Danh mục: " + categoryName);
        model.addAttribute("contentTemplate", "trasua/pages/list");
        model.addAttribute("contentFragment", "content");
        return "layout";
    }

    @GetMapping("/search")
    public String search(@RequestParam("keyword") String keyword, Model model) {
        model.addAttribute("products", productRepository.findByNameContainingIgnoreCase(keyword));
        model.addAttribute("activeToppings", toppingRepository.findByStatus("Đang bán"));
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
        for (Review r : dbReviews) {
            User u = userRepository.findById(r.getUserId()).orElse(null);
            r.setCustomerName(u != null ? u.getFullName() : "Khách hàng");
        }
        double avgRating = dbReviews.isEmpty() ? 0.0 :
                dbReviews.stream().mapToInt(Review::getRating).average().orElse(0.0);

        model.addAttribute("reviews", dbReviews);
        model.addAttribute("totalReviews", dbReviews.size());
        model.addAttribute("avgRating", String.format("%.1f", avgRating));
        model.addAttribute("pageTitle", product != null ? product.getName() : "Chi tiết");
        model.addAttribute("contentTemplate", "trasua/pages/detail");
        model.addAttribute("contentFragment", "content");
        return "layout";
    }

    @PostMapping("/product/review")
    public String saveReview(@RequestParam("orderDetailId") int orderDetailId,
                             @RequestParam(value = "rating", defaultValue = "0") int rating,
                             @RequestParam(value = "comment", defaultValue = "") String comment,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) return "redirect:/login";

        if (rating < 1 || rating > 5) {
            redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng chọn số sao đánh giá từ 1 đến 5 sao!");
            return "redirect:/my-orders";
        }
        if (comment != null && comment.trim().length() > 500) {
            redirectAttributes.addFlashAttribute("errorMessage", "Nội dung nhận xét không được vượt quá 500 ký tự!");
            return "redirect:/my-orders";
        }

        try {
            OrderDetail detail = orderDetailRepository.findById(orderDetailId).orElse(null);
            if (detail == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy sản phẩm trong đơn hàng!");
                return "redirect:/my-orders";
            }

            Order order = orderRepository.findById(detail.getOrderId()).orElse(null);
            if (order == null || order.getUserId() != loggedInUser.getId()
                    || (!"HOÀN THÀNH".equalsIgnoreCase(order.getStatus()) && !"ĐÃ GIAO".equalsIgnoreCase(order.getStatus()))) {
                redirectAttributes.addFlashAttribute("errorMessage", "Chỉ khách hàng có đơn hàng đã giao thành công mới được đánh giá!");
                return "redirect:/my-orders";
            }

            if (detail.isReviewed()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Bạn đã đánh giá sản phẩm này trong đơn hàng rồi!");
                return "redirect:/my-orders";
            }

            Review newReview = new Review();
            newReview.setUserId(loggedInUser.getId());
            newReview.setProductId(detail.getProductId());
            newReview.setOrderDetailId(detail.getId());
            newReview.setRating(rating);
            newReview.setComment(comment.trim());
            newReview.setCustomerName(loggedInUser.getFullName());
            reviewRepository.save(newReview);

            detail.setReviewed(true);
            orderDetailRepository.save(detail);

            redirectAttributes.addFlashAttribute("successMessage", "Đánh giá sản phẩm thành công");
            return "redirect:/my-orders";

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi hệ thống: Không thể lưu đánh giá lúc này, vui lòng thử lại sau!");
            return "redirect:/my-orders";
        }
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
        if (user == null) return "redirect:/login";

        String referer = request.getHeader("Referer");
        String redirectBack = (referer != null && !referer.isEmpty()) ? referer : "/products";

        Product product = productRepository.findById(id).orElse(null);
        if (product == null) return "redirect:/products";

        if ("NGỪNG BÁN".equalsIgnoreCase(product.getStatus())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Sản phẩm này hiện đang ngừng bán!");
            return "redirect:" + redirectBack;
        }
        if (product.getQuantity() <= 0) {
            redirectAttributes.addFlashAttribute("errorMessage", "Sản phẩm '" + product.getName() + "' hiện đã tạm hết hàng trong kho!");
            return "redirect:" + redirectBack;
        }

        List<CartItem> userCartItems = cartService.getItems(user.getId());
        int currentQtyInCart = userCartItems.stream()
                .filter(item -> item.getProductId() == id)
                .mapToInt(CartItem::getQuantity)
                .sum();

        if (currentQtyInCart + quantity > product.getQuantity()) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Không thể thêm! Kho chỉ còn " + product.getQuantity() + " phần (Trong giỏ hàng bạn đã có sẵn " + currentQtyInCart + ").");
            return "redirect:" + redirectBack;
        }

        boolean isFood = "FOOD".equalsIgnoreCase(options) || "Đồ ăn".equalsIgnoreCase(product.getCategory());
        int sizeId = 1;
        List<Integer> selectedToppingIds = new ArrayList<>();
        String cleanNote = "";

        if (!isFood) {
            if (options != null) {
                if (options.contains("Size L")) {
                    KichCo kcL = kichCoRepository.findByTenSize("L");
                    if (kcL != null) sizeId = kcL.getId();
                } else if (options.contains("Size M")) {
                    KichCo kcM = kichCoRepository.findByTenSize("M");
                    if (kcM != null) sizeId = kcM.getId();
                }

                for (Topping tp : toppingRepository.findAll()) {
                    if (options.contains(tp.getName())) {
                        selectedToppingIds.add(tp.getId());
                    }
                }

                // Lọc riêng phần Đường & Đá để hiển thị gọn gàng trong giỏ hàng
                List<String> noteParts = new ArrayList<>();
                for (String part : options.split(",")) {
                    String pTrim = part.trim();
                    if (pTrim.contains("Đường") || pTrim.contains("đá") || pTrim.contains("Đá")) {
                        noteParts.add(pTrim);
                    }
                }
                cleanNote = String.join(", ", noteParts);
            }
        }

        cartService.addItemWithToppings(user.getId(), product.getId(), sizeId, quantity, cleanNote, selectedToppingIds);
        redirectAttributes.addFlashAttribute("successMessage", "Đã thêm " + quantity + " '" + product.getName() + "' vào giỏ hàng!");
        return "redirect:" + redirectBack;
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
                if (!dir.exists()) dir.mkdirs();

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
        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) return "redirect:/login";

        try {
            model.addAttribute("products", productRepository.findAll());
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
        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) return "redirect:/login";

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
        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) return "redirect:/login";

        Product product = productRepository.findById(id).orElse(null);
        if (product == null) return "redirect:/admin/products";

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
        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) return "redirect:/login";

        String redirectBack = (product.getId() == 0) ? "redirect:/admin/product/add" : "redirect:/admin/product/edit/" + product.getId();

        try {
            if (product.getName() == null || product.getName().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Lỗi dữ liệu: Vui lòng nhập tên sản phẩm!");
                return redirectBack;
            }
            if (product.getPrice() <= 0) {
                redirectAttributes.addFlashAttribute("errorMessage", "Lỗi dữ liệu: Giá sản phẩm phải lớn hơn 0 VNĐ!");
                return redirectBack;
            }
            if (product.getQuantity() < 0) {
                redirectAttributes.addFlashAttribute("errorMessage", "Lỗi dữ liệu: Số lượng tồn kho không được nhỏ hơn 0!");
                return redirectBack;
            }

            product.setName(product.getName().trim());
            if (product.getStatus() == null || product.getStatus().isEmpty()) {
                product.setStatus("ĐANG BÁN");
            }

            Category catObj = categoryRepository.findAll().stream()
                    .filter(c -> c.getName().equalsIgnoreCase(product.getCategory()))
                    .findFirst()
                    .orElse(categoryRepository.findAll().stream().findFirst().orElse(null));
            product.setCategoryObj(catObj);

            boolean isNew = (product.getId() == 0);
            Product savedProduct = productRepository.save(product);

            if (product.getImageUrl() != null && !product.getImageUrl().trim().isEmpty()) {
                List<HinhAnh> oldImgs = hinhAnhRepository.findByProduct_Id(savedProduct.getId());
                if (oldImgs.isEmpty()) {
                    hinhAnhRepository.save(new HinhAnh(product.getImageUrl().trim(), savedProduct));
                } else {
                    oldImgs.get(0).setHinhanhUrl(product.getImageUrl().trim());
                    hinhAnhRepository.save(oldImgs.get(0));
                }
            }

            redirectAttributes.addFlashAttribute("successMessage",
                    (isNew ? "Thêm" : "Cập nhật") + " sản phẩm '" + product.getName() + "' thành công!");

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
        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) return "redirect:/login";

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
        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) return "redirect:/login";

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
        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) return "redirect:/login";

        try {
            if (code == null || code.trim().isEmpty() || title == null || title.trim().isEmpty()
                    || startDateStr == null || startDateStr.isEmpty() || endDateStr == null || endDateStr.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Lỗi dữ liệu: Vui lòng nhập đầy đủ các trường thông tin bắt buộc!");
                return "redirect:/admin/promotions";
            }

            String cleanCode = code.toUpperCase().trim().replaceAll("\\s+", "");
            LocalDate start = LocalDate.parse(startDateStr);
            LocalDate end = LocalDate.parse(endDateStr);

            if (!start.isBefore(end)) {
                redirectAttributes.addFlashAttribute("errorMessage", "Lỗi dữ liệu: Thời gian bắt đầu phải nhỏ hơn thời gian kết thúc!");
                return "redirect:/admin/promotions";
            }
            if (discountPercent < 1 || discountPercent > 100) {
                redirectAttributes.addFlashAttribute("errorMessage", "Lỗi dữ liệu: Mức giảm giá phải từ 1% đến 100%!");
                return "redirect:/admin/promotions";
            }

            Promotion existingPromo = promotionRepository.findByCode(cleanCode);
            if (id == null) {
                if (existingPromo != null) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Từ chối thao tác: Mã khuyến mãi '" + cleanCode + "' đã tồn tại trên hệ thống!");
                    return "redirect:/admin/promotions";
                }
            } else {
                if (existingPromo != null && existingPromo.getId() != id) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Từ chối thao tác: Mã khuyến mãi '" + cleanCode + "' đã thuộc về chương trình khác!");
                    return "redirect:/admin/promotions";
                }
            }

            Promotion promo = (id != null) ? promotionRepository.findById(id).orElse(new Promotion()) : new Promotion();
            promo.setCode(cleanCode);
            promo.setTitle(title.trim());
            promo.setDescription(description != null ? description.trim() : "");
            promo.setDiscountPercent(discountPercent);
            promo.setStartDate(start.atStartOfDay());
            promo.setEndDate(end.atTime(23, 59, 59));
            if (id == null) {
                promo.setImageUrl("noel.jpg");
                promo.setActive(true);
            }

            promotionRepository.save(promo);
            redirectAttributes.addFlashAttribute("successMessage", (id == null ? "Thêm mới" : "Cập nhật") + " chương trình khuyến mại '" + cleanCode + "' thành công!");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi hệ thống hoặc sai định dạng dữ liệu! Không thể lưu thông tin khuyến mại.");
        }

        return "redirect:/admin/promotions";
    }

    @GetMapping("/admin/promotion/toggle/{id}")
    public String togglePromotion(@PathVariable("id") int id, HttpSession session,
                                  RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) return "redirect:/login";

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
        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) return "redirect:/login";

        try {
            Promotion promo = promotionRepository.findById(id).orElse(null);
            if (promo != null) {
                if (!promo.isExpired()) {
                    redirectAttributes.addFlashAttribute("errorMessage",
                            "Từ chối xóa: Khuyến mãi '" + promo.getCode() + "' vẫn còn hạn sử dụng (đến hết ngày " + promo.getEndDate().toLocalDate() + ")!");
                    return "redirect:/admin/promotions";
                }
                promotionRepository.deleteById(id);
                redirectAttributes.addFlashAttribute("successMessage", "Xóa chương trình khuyến mãi đã hết hạn thành công!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi kết nối cơ sở dữ liệu! Không thể thực hiện xóa.");
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
        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) return "redirect:/login";

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
        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) return "redirect:/login";

        Order order = orderRepository.findById(id).orElse(null);
        if (order == null) return "redirect:/admin/orders";

        List<OrderDetail> items = orderDetailRepository.findByOrderId(order.getId());
        for (OrderDetail d : items) {
            Product p = productRepository.findById(d.getProductId()).orElse(null);
            if (p != null) {
                d.setProductName(p.getName());
                d.setImageUrl(p.getImageUrl());
            }
        }

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
        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) return "redirect:/login";

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
        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) return "redirect:/login";

        Order order = orderRepository.findById(id).orElse(null);
        if (order != null && ("CHỜ XÁC NHẬN".equals(order.getStatus()) || "ĐANG CHUẨN BỊ".equals(order.getStatus()))) {
            order.setStatus("ĐÃ HỦY");
            orderRepository.save(order);
        }
        return "redirect:/admin/order/detail/" + id;
    }

    @GetMapping("/admin/revenue")
    public String adminRevenueDashboard(@RequestParam(value = "startDate", required = false) String startDateStr,
                                        @RequestParam(value = "endDate", required = false) String endDateStr,
                                        @RequestParam(value = "quickFilter", required = false, defaultValue = "MONTH") String quickFilter,
                                        Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) return "redirect:/login";

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
        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) return "redirect:/login";

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
        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) return "redirect:/login";

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
        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) return "redirect:/login";

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

// ==========================================
    // PHÂN HỆ KHÁCH HÀNG: CHECKOUT & THANH TOÁN
    // ==========================================

    @GetMapping("/checkout")
    public String showCheckoutPage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";
        if (cartService.getItems(user.getId()).isEmpty()) return "redirect:/order";

        double originalTotal = cartService.getTotalPrice(user.getId());
        double discountAmount = session.getAttribute("discountAmount") != null ? (double) session.getAttribute("discountAmount") : 0.0;
        double finalPrice = Math.max(0, originalTotal - discountAmount);

        List<ShippingUnit> shippingUnits = shippingUnitRepository.findAll().stream()
                .filter(u -> "HOẠT ĐỘNG".equalsIgnoreCase(u.getStatus()))
                .toList();
        List<PhuongThucThanhToan> paymentMethods = phuongThucThanhToanRepository.findAll();


        model.addAttribute("cartItems", cartService.getItems(user.getId()));
        model.addAttribute("shippingUnits", shippingUnits);
        model.addAttribute("paymentMethods", paymentMethods);
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
            double finalPrice = Math.max(0, originalTotal - discountAmount);

            session.setAttribute("appliedPromoCode", promo.getCode());
            session.setAttribute("discountPercent", promo.getDiscountPercent());
            session.setAttribute("discountAmount", discountAmount);
            session.setAttribute("finalPrice", finalPrice);
            session.removeAttribute("promoError");
        } else {
            session.setAttribute("promoError", "Mã giảm giá không hợp lệ, đã hết hạn hoặc chưa đủ điều kiện áp dụng!");
            session.removeAttribute("appliedPromoCode");
            session.removeAttribute("discountPercent");
            session.removeAttribute("discountAmount");
            session.removeAttribute("finalPrice");
        }

        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/checkout");
    }

    @PostMapping("/api/checkout/create-order")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> createOrderAjax(@RequestParam("fullName") String fullName,
                                               @RequestParam("phone") String phone,
                                               @RequestParam("address") String address,
                                               @RequestParam(value = "shippingUnitId", required = false) Integer shippingUnitId,
                                               @RequestParam(value = "paymentMethod", defaultValue = "COD") String paymentMethod,
                                               HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            response.put("success", false);
            response.put("message", "Vui lòng đăng nhập để tiến hành thanh toán!");
            return response;
        }

        List<CartItem> cartItems = cartService.getItems(user.getId());
        if (cartItems.isEmpty()) {
            response.put("success", false);
            response.put("message", "Đơn hàng phải có ít nhất 1 sản phẩm!");
            return response;
        }

        if (fullName == null || fullName.trim().isEmpty() || address == null || address.trim().isEmpty()) {
            response.put("success", false);
            response.put("message", "Vui lòng nhập đầy đủ Họ tên và Địa chỉ nhận hàng!");
            return response;
        }

        // Rẽ nhánh 1 & Yêu cầu đặc biệt: Số điện thoại phải đúng 10 chữ số
        String cleanPhone = phone != null ? phone.trim().replaceAll("[^0-9]", "") : "";
        if (!cleanPhone.matches("^0[0-9]{9}$")) {
            response.put("success", false);
            response.put("message", "Số điện thoại không hợp lệ! Vui lòng nhập đúng định dạng 10 chữ số (bắt đầu bằng số 0).");
            return response;
        }

        try {
            double originalTotal = cartService.getTotalPrice(user.getId());
            double discountAmount = session.getAttribute("discountAmount") != null ? (double) session.getAttribute("discountAmount") : 0.0;

            ShippingUnit selectedShipping = (shippingUnitId != null)
                    ? shippingUnitRepository.findById(shippingUnitId).orElse(null)
                    : shippingUnitRepository.findAll().stream().findFirst().orElse(null);

            int finalShippingId = (selectedShipping != null) ? selectedShipping.getId() : 1;
            double shippingFee = (selectedShipping != null) ? selectedShipping.getShippingFee() : 0.0;
            double finalPrice = Math.max(0, originalTotal - discountAmount) + shippingFee;

            Order order = new Order();
            order.setUserId(user.getId());
            order.setFullName(fullName.trim());
            order.setPhone(cleanPhone);
            order.setAddress(address.trim());
            order.setPaymentMethod(paymentMethod);
            order.setTotalPrice(finalPrice);
            order.setShippingUnitId(finalShippingId);
            order.setStatus("BANK".equalsIgnoreCase(paymentMethod) ? "CHỜ THANH TOÁN" : "CHỜ XÁC NHẬN");

            String appliedCode = (String) session.getAttribute("appliedPromoCode");
            if (appliedCode != null) {
                Promotion appliedPromo = promotionRepository.findByCode(appliedCode);
                if (appliedPromo != null) order.setPromotionId(appliedPromo.getId());
            }
            orderRepository.save(order);

            for (CartItem item : cartItems) {
                OrderDetail detail = new OrderDetail();
                detail.setOrderId(order.getId());
                detail.setProductId(item.getProductId());
                detail.setSizeId(item.getSizeId());
                detail.setPrice(item.getPrice());
                detail.setQuantity(item.getQuantity());
                detail.setNote(item.getNote());
                detail = orderDetailRepository.save(detail);

                List<ChiTietGioHangTopping> cartToppings = chiTietGioHangToppingRepository.findByCartItemId(item.getId());
                for (ChiTietGioHangTopping ct : cartToppings) {
                    Topping tp = toppingRepository.findById(ct.getToppingId()).orElse(null);
                    double tpPrice = (tp != null) ? tp.getPrice() : 0;
                    chiTietDonHangToppingRepository.save(
                            new ChiTietDonHangTopping(detail.getId(), ct.getToppingId(), ct.getQuantity(), tpPrice)
                    );
                }
            }

            String transCode = "TRANS_" + System.currentTimeMillis();
            PaymentTransaction transaction = new PaymentTransaction();
            transaction.setOrderId(order.getId());
            transaction.setAmount(finalPrice);
            transaction.setPaymentMethodId("BANK".equalsIgnoreCase(paymentMethod) ? 2 : 1);
            transaction.setStatus("BANK".equalsIgnoreCase(paymentMethod) ? "PENDING" : "SUCCESS");
            transaction.setResponseCode(transCode);
            paymentTransactionRepository.save(transaction);

            // Rẽ nhánh 3: Nếu COD -> Trừ kho, xóa giỏ hàng ngay
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
            response.put("transCode", transCode);
            response.put("finalPrice", finalPrice);
            response.put("paymentMethod", paymentMethod);
            return response;

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Sự cố kết nối cơ sở dữ liệu! Không thể lưu thay đổi.");
            return response;
        }
    }

    // Rẽ nhánh 4: Ghi nhận thất bại vào GiaoDichThanhToan khi khách hủy / lỗi thanh toán QR & giữ nguyên giỏ hàng
    @PostMapping("/api/checkout/fail/{orderId}")
    @ResponseBody
    @Transactional
    public String markPaymentFailed(@PathVariable("orderId") int orderId) {
        try {
            PaymentTransaction transaction = paymentTransactionRepository.findByOrderId(orderId);
            if (transaction != null && "PENDING".equalsIgnoreCase(transaction.getStatus())) {
                transaction.setStatus("FAILED");
                transaction.setTransactionTime(LocalDateTime.now());
                paymentTransactionRepository.save(transaction);
            }
            Order order = orderRepository.findById(orderId).orElse(null);
            if (order != null && "CHỜ THANH TOÁN".equalsIgnoreCase(order.getStatus())) {
                order.setStatus("THANH TOÁN THẤT BẠI");
                orderRepository.save(order);
            }
            return "FAILED_RECORDED";
        } catch (Exception e) {
            return "ERROR";
        }
    }

    @GetMapping("/api/checkout/check-status/{orderId}")
    @ResponseBody
    public Map<String, Object> checkOrderStatus(@PathVariable("orderId") int orderId) {
        Map<String, Object> response = new HashMap<>();
        Order order = orderRepository.findById(orderId).orElse(null);
        response.put("paid", order != null && "ĐÃ THANH TOÁN".equalsIgnoreCase(order.getStatus()));
        return response;
    }

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

                List<OrderDetail> details = orderDetailRepository.findByOrderId(orderId);
                for (OrderDetail detail : details) {
                    Product product = productRepository.findById(detail.getProductId()).orElse(null);
                    if (product != null) {
                        product.setQuantity(Math.max(0, product.getQuantity() - detail.getQuantity()));
                        productRepository.save(product);
                    }
                }

                cartService.clear(order.getUserId());
                return "SUCCESS";
            }
        } catch (Exception e) {
            return "ERROR";
        }
        return "ALREADY_PAID";
    }

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
        if (user == null) return "redirect:/login";

        try {
            model.addAttribute("cartItems", cartService.getItems(user.getId()));
            model.addAttribute("totalPrice", cartService.getTotalPrice(user.getId()));
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Không thể kết nối cơ sở dữ liệu để tải giỏ hàng!");
            model.addAttribute("cartItems", Collections.emptyList());
            model.addAttribute("totalPrice", 0.0);
        }

        model.addAttribute("pageTitle", "Giỏ hàng của bạn");
        model.addAttribute("contentTemplate", "trasua/pages/order");
        model.addAttribute("contentFragment", "content");
        return "layout";
    }

    @PostMapping("/api/cart/update-qty")
    @ResponseBody
    public Map<String, Object> updateCartQtyAjax(@RequestParam("id") int id,
                                                 @RequestParam("quantity") String quantityStr,
                                                 HttpSession session) {
        Map<String, Object> res = new HashMap<>();
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            res.put("success", false);
            res.put("message", "Phiên đăng nhập hết hạn!");
            return res;
        }

        try {
            CartItem item = cartService.getItemByIdForUser(id, user.getId());
            if (item == null) {
                res.put("success", false);
                res.put("message", "Không tìm thấy sản phẩm trong giỏ hàng!");
                return res;
            }

            int newQty;
            try {
                newQty = Integer.parseInt(quantityStr.trim());
                if (newQty < 1) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                res.put("success", false);
                res.put("errorType", "INVALID_FORMAT");
                res.put("oldQty", item.getQuantity());
                res.put("message", "Số lượng không hợp lệ! Vui lòng nhập số nguyên lớn hơn hoặc bằng 1.");
                return res;
            }

            Product product = productRepository.findById(item.getProductId()).orElse(null);
            int stock = (product != null) ? product.getQuantity() : 0;

            int otherLinesQty = cartService.getItems(user.getId()).stream()
                    .filter(i -> i.getProductId() == item.getProductId() && i.getId() != id)
                    .mapToInt(CartItem::getQuantity)
                    .sum();

            int maxAllowedForThisLine = Math.max(0, stock - otherLinesQty);

            if (newQty > maxAllowedForThisLine) {
                item.setQuantity(maxAllowedForThisLine > 0 ? maxAllowedForThisLine : 1);
                cartItemRepository.save(item);

                res.put("success", false);
                res.put("errorType", "EXCEED_STOCK");
                res.put("maxQty", item.getQuantity());
                res.put("itemTotal", item.getPrice() * item.getQuantity());
                res.put("cartTotal", cartService.getTotalPrice(user.getId()));
                res.put("message", "Số lượng sản phẩm vượt quá số lượng còn lại trong kho (Tối đa: " + maxAllowedForThisLine + ")!");
                return res;
            }

            item.setQuantity(newQty);
            cartItemRepository.save(item);

            res.put("success", true);
            res.put("newQty", item.getQuantity());
            res.put("itemTotal", item.getPrice() * item.getQuantity());
            res.put("cartTotal", cartService.getTotalPrice(user.getId()));
            return res;

        } catch (Exception e) {
            res.put("success", false);
            res.put("message", "Lỗi kết nối cơ sở dữ liệu! Hệ thống giữ nguyên trạng thái cũ.");
            return res;
        }
    }

    @PostMapping("/cart/update")
    public String updateCartQuantity(@RequestParam("id") int id,
                                     @RequestParam("action") String action,
                                     HttpSession session,
                                     RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        try {
            CartItem item = cartService.getItemByIdForUser(id, user.getId());
            if (item != null) {
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
        if (user == null) return "redirect:/login";

        try {
            CartItem item = cartService.getItemByIdForUser(id, user.getId());
            if (item != null) {
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
        if (user == null) return "redirect:/login";

        try {
            cartService.clear(user.getId());
            redirectAttributes.addFlashAttribute("successMessage", "Đã xóa toàn bộ sản phẩm trong giỏ hàng thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi cơ sở dữ liệu! Không thể làm rỗng giỏ hàng.");
        }

        return "redirect:/order";
    }

    // ==========================================
    // KHU VỰC QUẢN TRỊ VIÊN: QUẢN LÝ ĐƠN VỊ VẬN CHUYỂN
    // ==========================================

    @GetMapping("/admin/shipping-units")
    public String adminShippingUnits(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) return "redirect:/login";

        try {
            model.addAttribute("shippingUnits", shippingUnitRepository.findAll());
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Lỗi kết nối cơ sở dữ liệu! Không thể tải danh sách đơn vị vận chuyển.");
            model.addAttribute("shippingUnits", Collections.emptyList());
        }

        model.addAttribute("pageTitle", "Quản lý đơn vị vận chuyển - Admin");
        model.addAttribute("contentTemplate", "trasua/pages/admin_shipping_units");
        model.addAttribute("contentFragment", "content");
        return "layout";
    }

    @PostMapping("/admin/shipping-unit/save")
    public String saveShippingUnit(@RequestParam(value = "id", required = false) Integer id,
                                   @RequestParam("name") String name,
                                   @RequestParam("shippingFee") double shippingFee,
                                   @RequestParam("estimatedTime") String estimatedTime,
                                   @RequestParam(value = "description", defaultValue = "") String description,
                                   @RequestParam("status") String status,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) return "redirect:/login";

        try {
            if (name == null || name.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Lỗi dữ liệu: Tên đơn vị vận chuyển không được để trống!");
                return "redirect:/admin/shipping-units";
            }
            if (shippingFee < 0) {
                redirectAttributes.addFlashAttribute("errorMessage", "Lỗi dữ liệu: Phí vận chuyển không được nhỏ hơn 0!");
                return "redirect:/admin/shipping-units";
            }
            if (estimatedTime == null || estimatedTime.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Lỗi dữ liệu: Vui lòng nhập thời gian giao dự kiến!");
                return "redirect:/admin/shipping-units";
            }

            String cleanName = name.trim();
            ShippingUnit existing = shippingUnitRepository.findByNameIgnoreCase(cleanName);
            if (id == null) {
                if (existing != null) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Từ chối lưu: Tên đơn vị vận chuyển '" + cleanName + "' đã tồn tại trong hệ thống!");
                    return "redirect:/admin/shipping-units";
                }
            } else {
                if (existing != null && existing.getId() != id) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Từ chối lưu: Tên đơn vị vận chuyển '" + cleanName + "' đã trùng với đơn vị khác!");
                    return "redirect:/admin/shipping-units";
                }
            }

            ShippingUnit unit = (id != null)
                    ? shippingUnitRepository.findById(id).orElse(new ShippingUnit())
                    : new ShippingUnit();

            if (id == null) {
                unit.setUnitCode("DVVC" + (System.currentTimeMillis() % 10000));
            }
            unit.setName(cleanName);
            unit.setShippingFee(shippingFee);
            unit.setEstimatedTime(estimatedTime.trim());
            unit.setDescription(description.trim());
            unit.setStatus(status);

            shippingUnitRepository.save(unit);
            redirectAttributes.addFlashAttribute("successMessage",
                    (id == null ? "Thêm mới" : "Cập nhật") + " đơn vị vận chuyển '" + cleanName + "' thành công!");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi kết nối cơ sở dữ liệu! Không thể lưu đơn vị vận chuyển.");
        }

        return "redirect:/admin/shipping-units";
    }

    @GetMapping("/admin/shipping-unit/delete/{id}")
    public String deleteShippingUnit(@PathVariable("id") int id,
                                     HttpSession session,
                                     RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) return "redirect:/login";

        try {
            ShippingUnit unit = shippingUnitRepository.findById(id).orElse(null);
            if (unit != null) {
                boolean hasOrders = orderRepository.findAll().stream()
                        .anyMatch(o -> o.getShippingUnitId() != null && o.getShippingUnitId() == id);

                if (hasOrders) {
                    unit.setStatus("NGỪNG HOẠT ĐỘNG");
                    shippingUnitRepository.save(unit);
                    redirectAttributes.addFlashAttribute("errorMessage",
                            "Đơn vị vận chuyển '" + unit.getName() + "' đã từng phát sinh đơn hàng nên hệ thống tự động chuyển sang trạng thái 'NGỪNG HOẠT ĐỘNG'.");
                } else {
                    shippingUnitRepository.deleteById(id);
                    redirectAttributes.addFlashAttribute("successMessage",
                            "Đã xóa đơn vị vận chuyển '" + unit.getName() + "' thành công!");
                }
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi kết nối cơ sở dữ liệu! Không thể thực hiện xóa.");
        }

        return "redirect:/admin/shipping-units";
    }
}