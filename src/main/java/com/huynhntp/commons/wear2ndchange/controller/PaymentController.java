package com.huynhntp.commons.wear2ndchange.controller;

import com.huynhntp.commons.wear2ndchange.model.dto.PaymentLinkResponseDto;
import com.huynhntp.commons.wear2ndchange.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create-payment-link/{orderId}")
    public ResponseEntity<?> createPaymentLink(@PathVariable Long orderId) {
        try {
            PaymentLinkResponseDto paymentData = paymentService.createPaymentLink(orderId);
            return ResponseEntity.ok(paymentData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error creating payment link: " + e.getMessage());
        }
    }

    @PostMapping("/confirm")
    public ResponseEntity<?> confirmPayment(@RequestParam String orderCode) {
        try {
            paymentService.confirmPayment(orderCode);
            return ResponseEntity.ok("Payment confirmed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error confirming payment: " + e.getMessage());
        }
    }
} 