package com.huynhntp.commons.wear2ndchange.controller;

import com.huynhntp.commons.wear2ndchange.model.dto.ProductForm;
import com.huynhntp.commons.wear2ndchange.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/product")
@AllArgsConstructor
//@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductService productService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createProduct(
            @ModelAttribute("productForm") ProductForm productForm,
            @ModelAttribute("images") MultipartFile[] images) {

        productService.createProduct(productForm, images);
        return ResponseEntity.ok("saved successfully");
    }

}
