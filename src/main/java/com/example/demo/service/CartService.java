package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CartService {

    @Autowired private CartItemRepository cartItemRepository;
    @Autowired private GioHangRepository gioHangRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private KichCoRepository kichCoRepository;
    @Autowired private ToppingRepository toppingRepository;
    @Autowired private ChiTietGioHangToppingRepository cartItemToppingRepository;

    private GioHang getOrCreateCart(int userId) {
        GioHang cart = gioHangRepository.findByUserId(userId);
        if (cart == null) {
            cart = new GioHang(userId);
            cart = gioHangRepository.save(cart);
        }
        return cart;
    }

    // Điền thông tin hiển thị (tên món, ảnh, size, topping, giá) cho CartItem từ DB
    private void populateDisplayFields(CartItem item, int userId) {
        item.setUserId(userId);
        Product p = productRepository.findById(item.getProductId()).orElse(null);
        KichCo kc = kichCoRepository.findById(item.getSizeId()).orElse(null);

        String categoryName = (p != null) ? p.getCategory() : "";
        boolean isFood = "Đồ ăn".equalsIgnoreCase(categoryName);

        double basePrice = (p != null) ? p.getPrice() : 0;
        double sizeExtra = (!isFood && kc != null) ? kc.getGiaPhuThu() : 0;
        double toppingTotal = 0;

        List<ChiTietGioHangTopping> itemToppings = cartItemToppingRepository.findByCartItemId(item.getId());
        List<String> toppingNames = new ArrayList<>();
        if (!isFood) {
            for (ChiTietGioHangTopping ct : itemToppings) {
                Topping tp = toppingRepository.findById(ct.getToppingId()).orElse(null);
                if (tp != null) {
                    toppingTotal += tp.getPrice() * ct.getQuantity();
                    toppingNames.add(tp.getName());
                }
            }
        }

        String sizeName = (kc != null) ? kc.getTenSize() : "S";
        String toppingStr = String.join(", ", toppingNames);
        String displayName = (p != null) ? p.getName() : "Sản phẩm";
        if (!isFood) {
            displayName += " (Size " + sizeName + ")";
        }

        item.setName(displayName);
        item.setImageUrl(p != null ? p.getImageUrl() : "DoUong/TraSua/OLongNhaiSua.jpeg");
        item.setSize(isFood ? "" : sizeName);
        item.setToppings(isFood ? "" : toppingStr);
        item.setBasePrice(basePrice);
        item.setPrice(basePrice + sizeExtra + toppingTotal);
    }

    // Thêm vào giỏ kèm danh sách ID topping
    @Transactional
    public void addItemWithToppings(int userId, int productId, int sizeId, int quantity, String note, List<Integer> toppingIds) {
        GioHang cart = getOrCreateCart(userId);
        List<CartItem> existingItems = cartItemRepository.findByCartId(cart.getId());

        List<Integer> sortedNewToppings = (toppingIds != null) ? new ArrayList<>(toppingIds) : new ArrayList<>();
        Collections.sort(sortedNewToppings);
        String safeNewNote = (note != null) ? note.trim() : "";

        // Kiểm tra trùng món + trùng size + trùng ghi chú (đường/đá) + trùng danh sách topping để cộng dồn
        for (CartItem item : existingItems) {
            String safeExistingNote = (item.getNote() != null) ? item.getNote().trim() : "";
            if (item.getProductId() == productId && item.getSizeId() == sizeId && safeExistingNote.equals(safeNewNote)) {
                List<Integer> existingToppingIds = cartItemToppingRepository.findByCartItemId(item.getId())
                        .stream().map(ChiTietGioHangTopping::getToppingId).sorted().toList();
                if (existingToppingIds.equals(sortedNewToppings)) {
                    item.setQuantity(item.getQuantity() + quantity);
                    cartItemRepository.save(item);
                    return;
                }
            }
        }

        CartItem newItem = new CartItem();
        newItem.setCartId(cart.getId());
        newItem.setProductId(productId);
        newItem.setSizeId(sizeId);
        newItem.setQuantity(quantity);
        newItem.setNote(safeNewNote);
        newItem = cartItemRepository.save(newItem);

        for (Integer tpId : sortedNewToppings) {
            cartItemToppingRepository.save(new ChiTietGioHangTopping(newItem.getId(), tpId, 1));
        }
    }

    public List<CartItem> getItems(int userId) {
        GioHang cart = gioHangRepository.findByUserId(userId);
        if (cart == null) return Collections.emptyList();
        List<CartItem> items = cartItemRepository.findByCartId(cart.getId());
        for (CartItem item : items) {
            populateDisplayFields(item, userId);
        }
        return items;
    }

    public CartItem getItemByIdForUser(int cartItemId, int userId) {
        GioHang cart = gioHangRepository.findByUserId(userId);
        if (cart == null) return null;
        CartItem item = cartItemRepository.findById(cartItemId).orElse(null);
        if (item != null && item.getCartId() == cart.getId()) {
            populateDisplayFields(item, userId);
            return item;
        }
        return null;
    }

    public double getTotalPrice(int userId) {
        double total = 0;
        for (CartItem item : getItems(userId)) {
            total += item.getPrice() * item.getQuantity();
        }
        return total;
    }

    public int getCount(int userId) {
        GioHang cart = gioHangRepository.findByUserId(userId);
        if (cart == null) return 0;
        return cartItemRepository.findByCartId(cart.getId()).size();
    }

    @Transactional
    public void removeItem(int id) {
        cartItemToppingRepository.deleteByCartItemId(id);
        cartItemRepository.deleteById(id);
    }

    public void updateQuantity(int id, int qty) {
        CartItem item = cartItemRepository.findById(id).orElse(null);
        if (item != null) {
            if (qty <= 0) {
                removeItem(id);
            } else {
                item.setQuantity(qty);
                cartItemRepository.save(item);
            }
        }
    }

    @Transactional
    public void clear(int userId) {
        GioHang cart = gioHangRepository.findByUserId(userId);
        if (cart != null) {
            List<CartItem> items = cartItemRepository.findByCartId(cart.getId());
            for (CartItem item : items) {
                cartItemToppingRepository.deleteByCartItemId(item.getId());
            }
            cartItemRepository.deleteByCartId(cart.getId());
        }
    }
}