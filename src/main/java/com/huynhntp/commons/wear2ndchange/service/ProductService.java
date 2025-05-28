package com.huynhntp.commons.wear2ndchange.service;

import com.huynhntp.commons.wear2ndchange.config.exception.AccessDeniedException;
import com.huynhntp.commons.wear2ndchange.config.exception.BusinessException;
import com.huynhntp.commons.wear2ndchange.enums.CategoryEnum;
import com.huynhntp.commons.wear2ndchange.mapper.ProductMapper;
import com.huynhntp.commons.wear2ndchange.model.dto.*;
import com.huynhntp.commons.wear2ndchange.model.entity.*;
import com.huynhntp.commons.wear2ndchange.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.*;
import java.util.*;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final AuthService authService;
    private final AccountRepository accountRepository;
    private final ProductImageRepository productImageRepository;

    @Transactional
    public void createProduct(ProductForm productForm, MultipartFile[] images) {
        Account account = accountRepository.findById(authService.getUserId())
                .orElseThrow(() -> new BusinessException("Account not found"));

        Product product = new Product()
                .setName(productForm.getName())
                .setSize(productForm.getSize())
                .setMaterial(productForm.getMaterial())
                .setPrice(productForm.getPrice())
                .setPercentage(productForm.getPercentage())
                .setCategory(productForm.getCategory().toString())
                .setCreateBy(account);

        String uploadDir = "/home/ubuntu/uploads/";
        String domainUrl = "http://45.119.82.37:8080";
        List<ProductImage> imageEntities = saveImages(images, product, uploadDir, domainUrl);
        product.setImages(imageEntities);

        productRepository.save(product);
    }

    @Transactional
    public void updateProduct(Long productId, ProductForm productForm, MultipartFile[] newImages) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException("Product not found"));
        product.setName(productForm.getName());
        product.setSize(productForm.getSize());
        product.setMaterial(productForm.getMaterial());
        product.setPrice(productForm.getPrice());
        product.setPercentage(productForm.getPercentage());
        product.setCategory(productForm.getCategory().toString());

        String uploadDir = "/home/ubuntu/uploads/";
        String domainUrl = "http://45.119.82.37:8080";

        List<ProductImage> newImageEntities = saveImages(newImages, product, uploadDir, domainUrl);

        productImageRepository.deleteByProductId(productId);
        productImageRepository.flush();

        product.getImages().addAll(newImageEntities);

        productRepository.save(product);
    }


    @Transactional(readOnly = true)
    public Page<ProductDTO> getProducts(String name, CategoryEnum category, Pageable pageable) {
        String categoryStr = category != null ? category.name() : null;
        Page<Product> products = productRepository.searchByNameAndCategory(name, categoryStr, pageable);

        List<ProductDTO> productDTOS = new ArrayList<>();
        products.forEach(product -> {
            List<ProductImage> productImages = productImageRepository.findByProductId(product.getId());
            ProductDTO productDTO = new ProductDTO();
            productDTO.setId(product.getId());
            productDTO.setSize(product.getSize());
            productDTO.setMaterial(product.getMaterial());
            productDTO.setName(product.getName());
            productDTO.setCategory(product.getCategory());
            productDTO.setPrice(product.getPrice());
            productDTO.setStatus(product.getStatus());
            productDTO.setPercentage(product.getPercentage());
            productDTO.setImages(productImages);
            productDTOS.add(productDTO);
        });

        return new PageImpl<>(productDTOS, pageable, products.getTotalElements());
    }

    @Transactional
    public void deleteProduct(Long productId) {
        Long userId = authService.getUserId();
        Account currentUser = accountRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("Account not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException("Product not found"));

        boolean isAdmin = currentUser.getRole().equalsIgnoreCase("ADMIN");

        boolean isOwner = product.getCreateBy().getId().equals(currentUser.getId());

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("You are not allowed to delete this product.");
        }

        productRepository.delete(product);
    }



    private List<ProductImage> saveImages(MultipartFile[] images, Product product, String uploadDir, String domainUrl) {
        List<ProductImage> imageEntities = new ArrayList<>();

        File dir = new File(uploadDir);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new RuntimeException("Could not create upload directory");
        }

        for (MultipartFile image : images) {
            if (!image.isEmpty()) {
                try {
                    String originalFilename = Paths.get(Objects.requireNonNull(image.getOriginalFilename()))
                            .getFileName().toString();
                    String extension = "";

                    int dotIndex = originalFilename.lastIndexOf('.');
                    if (dotIndex >= 0) {
                        extension = originalFilename.substring(dotIndex);
                    }

                    String safeFilename = UUID.randomUUID() + extension;
                    Path filePath = Paths.get(uploadDir, safeFilename);
                    Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                    ProductImage imageEntity = new ProductImage();
                    imageEntity.setUrl(domainUrl + "/uploads/" + safeFilename);
                    imageEntity.setProduct(product);
                    imageEntities.add(imageEntity);

                } catch (IOException e) {
                    throw new RuntimeException("Failed to save image", e);
                }
            }
        }

        return imageEntities;
    }

}
