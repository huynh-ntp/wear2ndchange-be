package com.huynhntp.commons.wear2ndchange.service;

import com.huynhntp.commons.wear2ndchange.config.exception.BusinessException;
import com.huynhntp.commons.wear2ndchange.enums.ProductAndOrderStatusEnum;
import com.huynhntp.commons.wear2ndchange.model.dto.OrderRequestDto;
import com.huynhntp.commons.wear2ndchange.model.dto.OrderResponseDto;
import com.huynhntp.commons.wear2ndchange.model.entity.Cart;
import com.huynhntp.commons.wear2ndchange.model.entity.Order;
import com.huynhntp.commons.wear2ndchange.model.entity.OrderItem;
import com.huynhntp.commons.wear2ndchange.model.entity.Product;
import com.huynhntp.commons.wear2ndchange.repository.OrderRepository;
import com.huynhntp.commons.wear2ndchange.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@AllArgsConstructor
public class OrderService {

    private final ProductRepository productRepository;
    private final AuthService authService;
    private final OrderRepository orderRepository;

    @Transactional
    public void createOrderFromCart(List<Cart> cartItems, OrderRequestDto orderRequestDto) {
        Long userId = authService.getUserId();

        Order order = new Order();
        order.setUserId(userId)
                .setAddress(orderRequestDto.getAddress())
                .setReceiver(orderRequestDto.getReceiver())
                .setPhoneNumber(orderRequestDto.getPhoneNumber())
                .setEmail(orderRequestDto.getEmail())
                .setNote(orderRequestDto.getNote());

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
            product.setStatus(ProductAndOrderStatusEnum.SOLD_OUT.toString());
            productRepository.save(product);
        }

        order.setItems(items);
        order.setTotalAmount(totalAmount);
        order.setStatus(ProductAndOrderStatusEnum.INIT.toString());

        orderRepository.save(order);
    }

    @Transactional
    public void changeOrderStatus(Long orderId, String action) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException("Order not found"));

        for (OrderItem item : order.getItems()) {
            Long productId = item.getProduct().getId();

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new BusinessException("Product not found"));

            switch (action) {
                case "CANCEL":
                    product.setStatus(ProductAndOrderStatusEnum.ACTIVE.name());
                    break;
                case "DELIVERING", "DELIVERED", "RECEIVED", "INIT":
                    product.setStatus(ProductAndOrderStatusEnum.SOLD_OUT.name());
                    break;
                default:
                    throw new BusinessException("Action support: CANCEL or DELIVERING or DELIVERED or RECEIVED");
            }

            productRepository.save(product);
        }

        order.setStatus(action);
        orderRepository.save(order);
    }

    public Page<Order> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public Page<Order> getOrdersByUserId(Long userId, Pageable pageable) {
        return orderRepository.findByUserId(userId, pageable);
    }
}
