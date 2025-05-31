package com.huynhntp.commons.wear2ndchange.controller;


import com.huynhntp.commons.wear2ndchange.config.exception.BusinessException;
import com.huynhntp.commons.wear2ndchange.model.dto.OrderRequestDto;
import com.huynhntp.commons.wear2ndchange.model.dto.OrderResponseDto;
import com.huynhntp.commons.wear2ndchange.model.entity.Cart;
import com.huynhntp.commons.wear2ndchange.repository.CartRepository;
import com.huynhntp.commons.wear2ndchange.service.AuthService;
import com.huynhntp.commons.wear2ndchange.service.OrderService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/order")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final CartRepository cartRepository;
    private final AuthService authService;

    @PostMapping
    @SneakyThrows
    public ResponseEntity<?> placeOrder(@RequestBody OrderRequestDto orderRequestDto) {
        List<Long> cartItemIds = orderRequestDto.getCartIds();
        List<Cart> selectedItems = cartRepository.findAllById(cartItemIds).stream()
                .filter(cart -> cart.getUserId().equals(authService.getUserId()))
                .toList();

        if (selectedItems.isEmpty()) {
            throw new BusinessException("Không có sản phẩm hợp lệ để đặt hàng.");
        }
        orderService.createOrderFromCart(selectedItems, orderRequestDto);

        cartRepository.deleteAll(selectedItems);

        return ResponseEntity.ok("Đặt hàng thành công!");
    }

    @PutMapping
    public ResponseEntity<?> changeStatus(@RequestParam Long id,
                                          @RequestParam String action) {
        orderService.changeOrderStatus(id, action);

        return ResponseEntity.ok(Map.of("message", "Order status updated"));
    }

    @GetMapping("/owner")
    public ResponseEntity<?> getOrders() {
        List<OrderResponseDto> orders = orderService.getOrderByOwner();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

}
