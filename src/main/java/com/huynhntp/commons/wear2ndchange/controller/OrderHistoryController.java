package com.huynhntp.commons.wear2ndchange.controller;


import com.huynhntp.commons.wear2ndchange.model.dto.OrderDTO;
import com.huynhntp.commons.wear2ndchange.service.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order-history")
@AllArgsConstructor
public class OrderHistoryController {

    private final OrderHistoryService orderHistoryService;

    @GetMapping
    public ResponseEntity<Page<OrderDTO>> viewOrderHistory(Pageable pageable) {
        Page<OrderDTO> history = orderHistoryService.getUserOrderHistory(pageable);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/all")
    public ResponseEntity<Page<OrderDTO>> viewAllOrderHistory(Pageable pageable) {
        Page<OrderDTO> history = orderHistoryService.getAllUserOrderHistory(pageable);
        return ResponseEntity.ok(history);
    }
}
