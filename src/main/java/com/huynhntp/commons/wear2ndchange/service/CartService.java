package com.huynhntp.commons.wear2ndchange.service;

import com.huynhntp.commons.wear2ndchange.config.exception.BusinessException;
import com.huynhntp.commons.wear2ndchange.mapper.ProductMapper;
import com.huynhntp.commons.wear2ndchange.model.dto.*;
import com.huynhntp.commons.wear2ndchange.model.entity.*;
import com.huynhntp.commons.wear2ndchange.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final AuthService authService;
    private final ProductMapper productMapper;
    private final AccountRepository accountRepository;

    public void addToCart(CartForm form) {
        Long userId = authService.getUserId();
        Account account = accountRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("Account not found"));
        Product product = productRepository.findById(form.getProductId())
                .orElseThrow(() -> new BusinessException("Product not found"));

        Cart cartItem = cartRepository.findByUserIdAndProduct_Id(userId, form.getProductId())
                .orElse(new Cart());

        cartItem.setUserId(userId);
        cartItem.setProduct(product);
        cartRepository.save(cartItem);

        int cartCount = cartRepository.countByUserId(userId);

        Map<String, Object> preference = account.getPreference();
        if (preference == null) {
            preference = new HashMap<>();
        }
        preference.put("cart_number", cartCount);
        account.setPreference(preference);
        accountRepository.save(account);
    }


    public void deleteCartItem(Long cartItemId) {
        cartRepository.deleteById(cartItemId);

        Long userId = authService.getUserId();

        int cartCount = cartRepository.countByUserId(userId);

        Account account = accountRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("Account not found"));

        Map<String, Object> preference = account.getPreference();
        if (preference == null) {
            preference = new HashMap<>();
        }
        preference.put("cart_number", cartCount);
        account.setPreference(preference);

        accountRepository.save(account);
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
