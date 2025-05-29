package com.huynhntp.commons.wear2ndchange.controller;


import com.huynhntp.commons.wear2ndchange.model.entity.*;
import com.huynhntp.commons.wear2ndchange.repository.CartRepository;
import com.huynhntp.commons.wear2ndchange.service.*;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final CartRepository cartRepository;
    private final AuthService authService;

    @PostMapping
    public ResponseEntity<?> placeOrder() {
        List<Cart> cartItems = cartRepository.findByUserId(authService.getUserId());
        orderService.createOrderFromCart(cartItems);

        cartRepository.deleteAll(cartItems);

        return ResponseEntity.ok("Đặt hàng thành công!");
    }
}
