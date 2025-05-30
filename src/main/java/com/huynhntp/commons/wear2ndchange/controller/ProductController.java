package com.huynhntp.commons.wear2ndchange.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.huynhntp.commons.wear2ndchange.enums.CategoryEnum;
import com.huynhntp.commons.wear2ndchange.model.dto.*;
import com.huynhntp.commons.wear2ndchange.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/product")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    @SneakyThrows
    @PostMapping()
    public ResponseEntity<?> createProduct(
            @RequestParam("productForm") String productFormStr,
            @RequestParam("images") MultipartFile[] images) {

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        ProductForm productForm = mapper.readValue(productFormStr, ProductForm.class);
        productService.createProduct(productForm, images);

        return ResponseEntity.ok("Đăng bán thành công.");
    }

    @PutMapping()
    @SneakyThrows
    public ResponseEntity<?> updateProduct(
            @RequestParam("productId") Long productId,
            @RequestParam("productForm") String productFormStr,
            @RequestParam("images") MultipartFile[] images) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        ProductForm productForm = mapper.readValue(productFormStr, ProductForm.class);
        productService.updateProduct(productId, productForm, images);

        return ResponseEntity.ok("saved successfully");
    }

    @GetMapping()
    public Page<ProductDTO> getAllProducts(@RequestParam(required = false) String name,
                                           @RequestParam(required = false) String category,
                                           @RequestParam(required = false) String status,
                                           Pageable pageable) {
        return productService.getProducts(name, category != null ?
                CategoryEnum.parseStringToEnum(category) : null, status, pageable);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> changeStatus(@PathVariable Long id,
                                          @RequestBody ChangeProductStatusForm changeProductStatusForm) {
        productService.changeProductStatus(id, changeProductStatusForm.getStatus());
        return ResponseEntity.ok(Map.of("message", "Product status updated"));
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        ProductDTO product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully.");
    }
}
