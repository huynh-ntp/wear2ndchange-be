package com.huynhntp.commons.wear2ndchange.controller;

import com.huynhntp.commons.wear2ndchange.enums.CategoryEnum;
import com.huynhntp.commons.wear2ndchange.model.dto.*;
import com.huynhntp.commons.wear2ndchange.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/product")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createProduct(
            @ModelAttribute("productForm") ProductForm productForm,
            @ModelAttribute("images") MultipartFile[] images) {

        productService.createProduct(productForm, images);

        return ResponseEntity.ok("saved successfully");
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProduct(
            @ModelAttribute("productId") Long productId,
            @ModelAttribute("productForm") ProductForm productForm,
            @ModelAttribute("images") MultipartFile[] images) {
        productService.updateProduct(productId, productForm, images);

        return ResponseEntity.ok("saved successfully");
    }

    @GetMapping
    public Page<ProductDTO> getAllProducts(@RequestParam(required = false) String name,
                                           @RequestParam(required = false) CategoryEnum category,
                                           Pageable pageable) {
        return productService.getProducts(name, category, pageable);
    }

}
