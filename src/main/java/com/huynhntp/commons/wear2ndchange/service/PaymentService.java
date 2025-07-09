package com.huynhntp.commons.wear2ndchange.service;

import com.huynhntp.commons.wear2ndchange.config.exception.BusinessException;
import com.huynhntp.commons.wear2ndchange.config.payos.PayOSConfig;
import com.huynhntp.commons.wear2ndchange.enums.ProductAndOrderStatusEnum;
import com.huynhntp.commons.wear2ndchange.model.dto.PaymentLinkResponseDto;
import com.huynhntp.commons.wear2ndchange.model.entity.Order;
import com.huynhntp.commons.wear2ndchange.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.ItemData;
import vn.payos.type.PaymentData;
import vn.payos.type.PaymentLinkData;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PayOSConfig payOSConfig;
    private final OrderRepository orderRepository;
    private final PayOS payOS;

    @Transactional
    public PaymentLinkResponseDto createPaymentLink(Long orderId) {
        try {
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new BusinessException("Order not found"));

            // Generate unique order code
            String orderCode = String.valueOf(new Date().getTime());
            order.setOrderCode(orderCode);
            order.setStatus(ProductAndOrderStatusEnum.WAIT_FOR_PAYMENT.name());
            orderRepository.save(order);

            ItemData item = ItemData.builder()
                    .name("Đơn hàng #" + order.getId())
                    .price(order.getTotalAmount().intValue())
                    .quantity(1)
                    .build();

            PaymentData paymentData = PaymentData.builder()
                    .orderCode(Long.parseLong(orderCode))
                    .amount(order.getTotalAmount().intValue())
                    .description("#" + order.getId())
                    .items(List.of(item))
                    .cancelUrl(payOSConfig.getCancelUrl())
                    .returnUrl(payOSConfig.getReturnUrl())
                    .build();

            CheckoutResponseData result = payOS.createPaymentLink(paymentData);
            log.info("Payment link created successfully for order: {}", orderId);
            
            return PaymentLinkResponseDto.builder()
                    .checkoutUrl(result.getCheckoutUrl())
                    .orderCode(orderCode)
                    .orderId(orderId)
                    .amount(order.getTotalAmount())
                    .status(order.getStatus())
                    .build();
        } catch (Exception e) {
            log.error("Error creating payment link: ", e);
            throw new BusinessException("Failed to create payment link: " + e.getMessage());
        }
    }

    @Transactional
    public void confirmPayment(String orderCode) {
        try {
            // Get payment info from PayOS
            PaymentLinkData paymentInfo = payOS.getPaymentLinkInformation(Long.parseLong(orderCode));
            
            if (paymentInfo == null) {
                throw new BusinessException("Payment information not found");
            }

            // Check payment status
            if ("PAID".equals(paymentInfo.getStatus())) {
                // Update order status
                Order order = orderRepository.findByOrderCode(orderCode)
                        .orElseThrow(() -> new BusinessException("Order not found with orderCode: " + orderCode));
                
                order.setStatus(ProductAndOrderStatusEnum.PAID.name());
                orderRepository.save(order);
                
                log.info("Payment confirmed for order code: {}", orderCode);
            } else {
                throw new BusinessException("Payment is not completed. Status: " + paymentInfo.getStatus());
            }
        } catch (Exception e) {
            log.error("Error confirming payment: ", e);
            throw new BusinessException("Failed to confirm payment: " + e.getMessage());
        }
    }
} 