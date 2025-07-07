package com.huynhntp.commons.wear2ndchange.controller;

import com.huynhntp.commons.wear2ndchange.repository.ProductImageRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/productImage")
@AllArgsConstructor
public class ProductImageController {

    private final ProductImageRepository productImageRepository;

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteImage(@PathVariable  Long id){
        productImageRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
