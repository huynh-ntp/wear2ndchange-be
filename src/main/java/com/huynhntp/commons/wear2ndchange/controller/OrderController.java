package com.huynhntp.commons.wear2ndchange.controller;


import com.huynhntp.commons.wear2ndchange.model.entity.*;
import com.huynhntp.commons.wear2ndchange.repository.CartRepository;
import com.huynhntp.commons.wear2ndchange.service.*;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/order")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final CartRepository cartRepository;
    private final AuthService authService;

    @PostMapping
    public ResponseEntity<?> placeOrder(@RequestParam List<Long> cartItemIds) {
        List<Cart> selectedItems = cartRepository.findAllById(cartItemIds).stream()
                .filter(cart -> cart.getUserId().equals(authService.getUserId()))
                .toList();

        if (selectedItems.isEmpty()) {
            return ResponseEntity.badRequest().body("Không có sản phẩm hợp lệ để đặt hàng.");
        }
        orderService.createOrderFromCart(selectedItems);

        cartRepository.deleteAll(selectedItems);

        return ResponseEntity.ok("Đặt hàng thành công!");
    }

    @PutMapping
    public ResponseEntity<?> changeStatus(@RequestParam Long id,
                                          @RequestParam String action) {
        orderService.changeOrderStatus(id, action);

        return ResponseEntity.ok(Map.of("message", "Order status updated"));
    }
}
