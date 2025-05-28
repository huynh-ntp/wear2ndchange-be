package com.huynhntp.commons.wear2ndchange.service;

import com.huynhntp.commons.wear2ndchange.config.exception.BusinessException;
import com.huynhntp.commons.wear2ndchange.mapper.ProductMapper;
import com.huynhntp.commons.wear2ndchange.model.dto.*;
import com.huynhntp.commons.wear2ndchange.model.entity.*;
import com.huynhntp.commons.wear2ndchange.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final AuthService authService;
    private final ProductMapper productMapper;

    public void addToCart(CartForm form) {
        Long userId = authService.getUserId();

        Product product = productRepository.findById(form.getProductId())
                .orElseThrow(() -> new BusinessException("Product not found"));

        Cart cartItem = cartRepository.findByUserIdAndProduct_Id(userId, form.getProductId())
                .orElse(new Cart());

        cartItem.setUserId(userId);
        cartItem.setProduct(product);

        cartRepository.save(cartItem);
    }


    public void deleteCartItem(Long cartItemId) {
        cartRepository.deleteById(cartItemId);
    }

    public List<CartDTO> viewCart() {
        Long userId = authService.getUserId();
        List<Cart> cartItems = cartRepository.findByUserId(userId);

        return cartItems.stream()
                .map(cart -> {
                    Product product = cart.getProduct();
                    CartDTO cartDTO = new CartDTO();
                    cartDTO.setId(cart.getId());
                    cartDTO.setProduct(productMapper.toDto(product));

                    return cartDTO;
                })
                .collect(Collectors.toList());
    }
}
