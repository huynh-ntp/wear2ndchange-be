package com.huynhntp.commons.wear2ndchange.service;

import com.huynhntp.commons.wear2ndchange.config.exception.AccessDeniedException;
import com.huynhntp.commons.wear2ndchange.config.exception.BusinessException;
import com.huynhntp.commons.wear2ndchange.enums.*;
import com.huynhntp.commons.wear2ndchange.mapper.ProductMapper;
import com.huynhntp.commons.wear2ndchange.model.dto.ProductDTO;
import com.huynhntp.commons.wear2ndchange.model.dto.ProductForm;
import com.huynhntp.commons.wear2ndchange.model.entity.Account;
import com.huynhntp.commons.wear2ndchange.model.entity.Product;
import com.huynhntp.commons.wear2ndchange.model.entity.ProductImage;
import com.huynhntp.commons.wear2ndchange.repository.AccountRepository;
import com.huynhntp.commons.wear2ndchange.repository.ProductImageRepository;
import com.huynhntp.commons.wear2ndchange.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final AuthService authService;
    private final AccountRepository accountRepository;
    private final ProductImageRepository productImageRepository;
    private final ProductMapper productMapper;

    @Transactional
    public void createProduct(ProductForm productForm, MultipartFile[] images) {
        Account account = accountRepository.findById(authService.getUserId())
                .orElseThrow(() -> new BusinessException("Account not found"));

        Product product = new Product()
                .setName(productForm.getName())
                .setSize(productForm.getSize())
                .setMaterial(productForm.getMaterial())
                .setStatus("ACTIVE")
                .setCreatedDateTime(LocalDateTime.now())
                .setPrice(productForm.getPrice())
                .setPercentage(productForm.getPercentage())
                .setCategory(CategoryEnum.parseStringToEnum(productForm.getCategory()))
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
        product.setName(productForm.getName())
                .setSize(productForm.getSize())
                .setMaterial(productForm.getMaterial())
                .setPrice(productForm.getPrice())
                .setPercentage(productForm.getPercentage())
                .setCategory(CategoryEnum.parseStringToEnum(productForm.getCategory()));

        String uploadDir = "/home/ubuntu/uploads/";
        String domainUrl = "http://45.119.82.37:8080";

        if(newImages!=null && newImages.length>0) {
            List<ProductImage> newImageEntities = saveImages(newImages, product, uploadDir, domainUrl);

            productImageRepository.flush();

            product.getImages().addAll(newImageEntities);
        }

        productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO> getProducts(String name, CategoryEnum category, String status, Pageable pageable) {
        Page<Product> products = productRepository.searchByNameAndStatusAndCategory(name, status, category, pageable);

        List<ProductDTO> productDTOS = new ArrayList<>();
        products.forEach(product -> {
            List<ProductImage> productImages = productImageRepository.findByProductId(product.getId());
            ProductDTO productDTO = new ProductDTO();
            productDTO.setId(product.getId());
            productDTO.setSize(product.getSize());
            productDTO.setMaterial(product.getMaterial());
            productDTO.setName(product.getName());
            productDTO.setCategory(product.getCategory().toString());
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

    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        return productMapper.toDto(product);
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

    @Transactional
    public void changeProductStatus(Long productId, ProductAndOrderStatusEnum newStatus) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException("Product not found"));

        product.setStatus(newStatus.toString());
        productRepository.save(product);
    }
}
