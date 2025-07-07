package com.huynhntp.commons.wear2ndchange.service;

import com.huynhntp.commons.wear2ndchange.mapper.*;
import com.huynhntp.commons.wear2ndchange.model.dto.*;
import com.huynhntp.commons.wear2ndchange.model.entity.*;
import com.huynhntp.commons.wear2ndchange.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderHistoryService {

    private final AuthService authService;
    private final OrderRepository orderRepository;
    private final ProductMapper productMapper;
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public Page<OrderDTO> getUserOrderHistory(Pageable pageable) {
        Long userId = authService.getUserId();

        Page<Order> orders = orderRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);

        Account user = accountRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Account not found for ID: " + userId));

        List<OrderDTO> dtoList = orders.getContent().stream().map(order -> {
            OrderDTO dto = new OrderDTO();
            dto.setId(order.getId());
            dto.setCreatedAt(order.getCreatedAt());
            dto.setStatus(order.getStatus());

            List<OrderItemDTO> itemDTOs = order.getItems().stream().map(item -> {
                OrderItemDTO itemDTO = new OrderItemDTO();
                ProductDTO productDTO = productMapper.toDto(item.getProduct());
                itemDTO.setProduct(productDTO);
                return itemDTO;
            }).collect(Collectors.toList());

            dto.setItems(itemDTOs);
            dto.setUser(accountMapper.toDto(user));
            return dto;
        }).collect(Collectors.toList());

        return new PageImpl<>(dtoList, pageable, orders.getTotalElements());
    }


    public Page<OrderDTO> getAllUserOrderHistory(Pageable pageable) {
        Page<Order> orders = orderRepository.findAllByOrderByCreatedAtDesc(pageable);

        List<Long> userIds = orders.getContent().stream()
                .map(Order::getUserId)
                .distinct()
                .collect(Collectors.toList());

        Map<Long, Account> userMap = accountRepository.findAllById(userIds).stream()
                .collect(Collectors.toMap(Account::getId, Function.identity()));

        List<OrderDTO> dtoList = orders.getContent().stream().map(order -> {
            OrderDTO dto = new OrderDTO();
            dto.setId(order.getId());
            dto.setCreatedAt(order.getCreatedAt());
            dto.setStatus(order.getStatus());

            List<OrderItemDTO> itemDTOs = order.getItems().stream().map(item -> {
                OrderItemDTO itemDTO = new OrderItemDTO();
                itemDTO.setProduct(productMapper.toDto(item.getProduct()));
                return itemDTO;
            }).collect(Collectors.toList());

            dto.setItems(itemDTOs);

            Account user = userMap.get(order.getUserId());
            dto.setUser(accountMapper.toDto(user));

            return dto;
        }).collect(Collectors.toList());

        return new PageImpl<>(dtoList, pageable, orders.getTotalElements());
    }



}
