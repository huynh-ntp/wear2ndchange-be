package com.huynhntp.commons.wear2ndchange.service;

import com.huynhntp.commons.wear2ndchange.mapper.*;
import com.huynhntp.commons.wear2ndchange.model.dto.*;
import com.huynhntp.commons.wear2ndchange.model.entity.*;
import com.huynhntp.commons.wear2ndchange.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderHistoryService {

    private final AuthService authService;
    private final OrderRepository orderRepository;
    private final ProductMapper productMapper;
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public List<OrderDTO> getUserOrderHistory() {
        Long userId = authService.getUserId();
        List<Order> orders = orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
        Account user = accountRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Account not found for ID: " + userId));


        return orders.stream().map(order -> {
            OrderDTO dto = new OrderDTO();
            dto.setId(order.getId());
            dto.setCreatedAt(order.getCreatedAt());

            List<OrderItemDTO> itemDTOs = order.getItems().stream().map(item -> {

                OrderItemDTO itemDTO = new OrderItemDTO();
                ProductDTO productDTO = productMapper.toDto(item.getProduct());
                itemDTO.setProduct(productDTO);
                return itemDTO;
            }).collect(Collectors.toList());
            dto.setStatus(order.getStatus());
            dto.setItems(itemDTOs);
            dto.setUser(accountMapper.toDto(user));
            return dto;
        }).collect(Collectors.toList());
    }

}
