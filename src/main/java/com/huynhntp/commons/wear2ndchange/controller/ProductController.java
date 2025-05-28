package com.huynhntp.commons.wear2ndchange.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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

    @PostMapping()
    public ResponseEntity<?> createProduct(
            @RequestParam("productForm") String productFormStr,
            @RequestParam("images") MultipartFile[] images) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        ProductForm productForm = mapper.readValue(productFormStr, ProductForm.class);
        productService.createProduct(productForm, images);

        return ResponseEntity.ok("Đăng bán thành công.");
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

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        ProductDTO product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully.");
    }
}
