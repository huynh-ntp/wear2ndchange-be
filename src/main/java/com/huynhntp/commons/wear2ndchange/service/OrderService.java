package com.huynhntp.commons.wear2ndchange.service;

import com.huynhntp.commons.wear2ndchange.config.exception.BusinessException;
import com.huynhntp.commons.wear2ndchange.model.entity.*;
import com.huynhntp.commons.wear2ndchange.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class OrderService {

    private final ProductRepository productRepository;
    private final AuthService authService;
    private final OrderRepository orderRepository;
    private final OrderHistoryRepository orderHistoryRepository;

    @Transactional
    public void createOrderFromCart(List<Cart> cartItems) {
        Long userId = authService.getUserId();
        if (cartItems.isEmpty()) {
            throw new BusinessException("No cart to order");
        }
        Order order = new Order();
        order.setUserId(userId);

        List<OrderItem> items = new ArrayList<>();
        double totalAmount = 0;

        for (Cart cart : cartItems) {
            Product product = productRepository.findById(cart.getProduct().getId())
                    .orElseThrow(() -> new BusinessException("Product not found"));

            OrderItem item = new OrderItem();
            item.setProduct(product);
            item.setPrice(product.getPrice());
            item.setTotal(product.getPrice());
            item.setOrder(order);

            items.add(item);
            totalAmount += product.getPrice();
            product.setStatus("SOLD_OUT");
            productRepository.save(product);
        }

        order.setItems(items);
        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);
        orderHistoryRepository.save(new OrderHistory(savedOrder.getId(),"CREATED", LocalDateTime.now(), authService.getUserId()));
    }
}
