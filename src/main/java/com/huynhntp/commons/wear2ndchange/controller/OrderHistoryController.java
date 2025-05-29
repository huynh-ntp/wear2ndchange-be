package com.huynhntp.commons.wear2ndchange.controller;


import com.huynhntp.commons.wear2ndchange.model.dto.OrderDTO;
import com.huynhntp.commons.wear2ndchange.model.entity.*;
import com.huynhntp.commons.wear2ndchange.service.*;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order-history")
@AllArgsConstructor
public class OrderHistoryController {

    private final OrderHistoryService orderHistoryService;

    @GetMapping
    public ResponseEntity<List<OrderDTO>> viewOrderHistory() {
        List<OrderDTO> history = orderHistoryService.getUserOrderHistory();
        return ResponseEntity.ok(history);
    }
}
