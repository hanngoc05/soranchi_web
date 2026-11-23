package com.example.demo.Controller;

import com.example.demo.model.CuocHoiThoai;
import com.example.demo.model.Product;
import com.example.demo.model.Promotion;
import com.example.demo.model.TinNhan;
import com.example.demo.model.User;
import com.example.demo.repository.CuocHoiThoaiRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.PromotionRepository;
import com.example.demo.repository.TinNhanRepository;
import com.example.demo.service.AIServiceGateway;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chatbot")
public class ChatbotController {

    @Autowired
    private CuocHoiThoaiRepository cuocHoiThoaiRepository;

    @Autowired
    private TinNhanRepository tinNhanRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private AIServiceGateway aiServiceGateway;

    @GetMapping("/history")
    public Map<String, Object> getChatHistory(HttpSession session) {
        Map<String, Object> res = new HashMap<>();
        CuocHoiThoai sessionChat = getOrCreateCuocHoiThoai(session);
        List<TinNhan> messages = tinNhanRepository.findByMaCuocHoiThoaiOrderByThoiGianGuiAsc(sessionChat.getMaCuocHoiThoai());
        res.put("messages", messages);
        return res;
    }

    @PostMapping("/send")
    public Map<String, Object> guiCauHoi(@RequestParam("question") String question, HttpSession session) {
        Map<String, Object> res = new HashMap<>();

        String validationError = kiemTraCauHoi(question);
        if (validationError != null) {
            res.put("success", false);
            res.put("reply", validationError);
            return res;
        }

        String cleanQuestion = question.trim();
        CuocHoiThoai cuocHoiThoai = getOrCreateCuocHoiThoai(session);

        TinNhan userMsg = new TinNhan("USER", cleanQuestion, cuocHoiThoai.getMaCuocHoiThoai());
        tinNhanRepository.save(userMsg);

        if (!isRelatedToTeaShop(cleanQuestion)) {
            String offTopicReply = "Vui lòng hỏi các câu hỏi liên quan đến sản phẩm và dịch vụ của cửa hàng.";
            tinNhanRepository.save(new TinNhan("BOT", offTopicReply, cuocHoiThoai.getMaCuocHoiThoai()));
            res.put("success", true);
            res.put("reply", offTopicReply);
            return res;
        }

        List<TinNhan> recentHistory = tinNhanRepository.findTop6ByMaCuocHoiThoaiOrderByThoiGianGuiDesc(cuocHoiThoai.getMaCuocHoiThoai());
        Collections.reverse(recentHistory);

        List<Product> activeProducts = productRepository.findAll().stream()
                .filter(p -> !"NGỪNG BÁN".equalsIgnoreCase(p.getStatus()))
                .collect(Collectors.toList());

        List<Promotion> activePromotions = promotionRepository.findByIsActiveTrue().stream()
                .filter(p -> !p.isExpired())
                .collect(Collectors.toList());

        String prompt = taoPrompt(cleanQuestion, recentHistory, activeProducts, activePromotions);

        try {
            String aiReply = aiServiceGateway.guiYeuCauAI(prompt);

            TinNhan botMsg = new TinNhan("BOT", aiReply, cuocHoiThoai.getMaCuocHoiThoai());
            tinNhanRepository.save(botMsg);

            res.put("success", true);
            res.put("reply", aiReply);
            return res;

        } catch (Exception e) {
            e.printStackTrace();
            String busyMessage = "Trợ lý đang bận, vui lòng thử lại sau hoặc liên hệ Hotline";
            res.put("success", false);
            res.put("reply", busyMessage);
            return res;
        }
    }

    private String kiemTraCauHoi(String question) {
        if (question == null || question.trim().isEmpty() || question.trim().length() > 500) {
            return "Vui lòng nhập câu hỏi hợp lệ";
        }
        return null;
    }

    private boolean isRelatedToTeaShop(String q) {
        String lower = q.toLowerCase();
        String[] keywords = {
            "trà", "sữa", "uống", "món", "menu", "thực đơn", "topping", "trân châu", "kem", "thạch",
            "giá", "tiền", "bao nhiêu", "rẻ", "ngon", "bán chạy", "best", "ngọt", "đá", "đường",
            "khuyến mãi", "khuyến mại", "giảm giá", "voucher", "mã", "ship", "giao hàng", "đặt hàng",
            "thanh toán", "quán", "cửa hàng", "soranchi", "tư vấn", "gợi ý", "xin chào", "chào", "hi", "hello", "cảm ơn", "hết hàng", "còn"
        };
        for (String kw : keywords) {
            if (lower.contains(kw)) return true;
        }
        return false;
    }

private CuocHoiThoai getOrCreateCuocHoiThoai(HttpSession session) {
    User user = (User) session.getAttribute("loggedInUser");
    int targetUserId = (user != null) ? user.getId() : 1; // Nếu chưa đăng nhập thì gán tạm tài khoản mặc định id=1

    CuocHoiThoai chat = cuocHoiThoaiRepository
            .findFirstByMaNguoiDungAndTrangThaiOrderByThoiGianTaoDesc(targetUserId, "ACTIVE");
    if (chat == null) {
        chat = new CuocHoiThoai();
        chat.setMaNguoiDung(targetUserId);
        chat.setTieuDe("Tư vấn trà sữa Soranchi");
        chat = cuocHoiThoaiRepository.save(chat);
    }
    return chat;
}

    private String taoPrompt(String question, List<TinNhan> history, List<Product> products, List<Promotion> promotions) {
        StringBuilder sb = new StringBuilder();
        sb.append("Bạn là trợ lý tư vấn bán hàng thân thiện, tự nhiên của cửa hàng trà sữa Soranchi.\n");
        sb.append("QUY TẮC BẮT BUỘC:\n");
        sb.append("1. CHỈ tư vấn các sản phẩm có trong danh sách ĐANG KINH DOANH dưới đây. Tuyệt đối không bịa món hoặc giá ngoài danh sách.\n");
        sb.append("2. Trả lời ngắn gọn, lịch sự, tự nhiên bằng tiếng Việt.\n");
        sb.append("3. Nếu khách hỏi ngoài lề không liên quan đến cửa hàng trà sữa, hãy trả lời đúng câu: 'Vui lòng hỏi các câu hỏi liên quan đến sản phẩm và dịch vụ của cửa hàng'.\n");
        sb.append("4. Trình bày bằng văn bản thuần túy, xuống dòng rõ ràng, KHÔNG sử dụng ký tự Markdown như dấu sao (**) hay dấu thăng (#).\n\n");

        sb.append("=== DANH SÁCH SẢN PHẨM ĐANG KINH DOANH ===\n");
        for (Product p : products) {
            sb.append(String.format("- %s (Danh mục: %s) | Giá: %,.0f VNĐ | Tồn kho: %d ly | Mô tả: %s\n",
                    p.getName(), p.getCategory(), p.getPrice(), p.getQuantity(),
                    p.getDescription() != null ? p.getDescription() : "Thơm ngon chuẩn vị"));
        }

        sb.append("\n=== CHƯƠNG TRÌNH KHUYẾN MÃI ĐANG ÁP DỤNG ===\n");
        if (promotions.isEmpty()) {
            sb.append("- Hiện chưa có mã khuyến mãi đặc biệt.\n");
        } else {
            for (Promotion pr : promotions) {
                sb.append(String.format("- Mã %s: %s (Giảm %d%% - %s)\n",
                        pr.getCode(), pr.getTitle(), pr.getDiscountPercent(), pr.getDescription()));
            }
        }

        sb.append("\n=== LỊCH SỬ HỘI THOẠI GẦN NHẤT ===\n");
        for (TinNhan msg : history) {
            sb.append(msg.getVaiTro()).append(": ").append(msg.getNoiDung()).append("\n");
        }

        sb.append("\nKHÁCH HÀNG HỎI: ").append(question).append("\nTRỢ LÝ TRẢ LỜI:");
        return sb.toString();
    }
}