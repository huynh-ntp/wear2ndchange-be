package com.huynhntp.commons.wear2ndchange.service;

import com.huynhntp.commons.wear2ndchange.config.exception.*;
import com.huynhntp.commons.wear2ndchange.config.exception.AccessDeniedException;
import com.huynhntp.commons.wear2ndchange.mapper.AccountMapper;
import com.huynhntp.commons.wear2ndchange.model.dto.*;
import com.huynhntp.commons.wear2ndchange.model.entity.Account;
import com.huynhntp.commons.wear2ndchange.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.*;
import java.util.*;


@Service
@AllArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;

    public AccountDTO viewProfile(Long userId) {
        Account account = accountRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("User not found"));

        return accountMapper.toDto(account);
    }

    @Transactional
    public void changePassword(ChangePasswordRequest request) {
        Long userId = authService.getUserId();
        Account account = accountRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("User not found"));

        if (!passwordEncoder.matches(request.getCurrentPassword(), account.getPassword())) {
            throw new BusinessException("Current password is incorrect");
        }

        account.setPassword(passwordEncoder.encode(request.getNewPassword()));
        accountRepository.save(account);
    }

    @Transactional
    public void updateAccount(UpdateAccountForm form) {
        Long currentUserId = authService.getUserId();

        Account account = accountRepository.findById(currentUserId)
                .orElseThrow(() -> new BusinessException("Account not found"));

        accountMapper.updateAccountFromForm(form, account);
        accountRepository.save(account);
    }

    @Transactional
    public String uploadAvatar(MultipartFile avatarFile) {
        if (avatarFile.isEmpty()) {
            throw new BusinessException("Avatar file is empty");
        }

        Long userId = authService.getUserId();
        Account account = accountRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("Account not found"));

        try {
            String uploadDir = "/home/ubuntu/uploads/";
            File dir = new File(uploadDir);
            if (!dir.exists() && !dir.mkdirs()) {
                throw new RuntimeException("Could not create upload directory");
            }

            String originalFilename = Paths.get(Objects.requireNonNull(avatarFile.getOriginalFilename())).getFileName().toString();
            String extension = "";
            int dotIndex = originalFilename.lastIndexOf('.');
            if (dotIndex >= 0) {
                extension = originalFilename.substring(dotIndex);
            }

            String safeFilename = UUID.randomUUID() + extension;
            Path filePath = Paths.get(uploadDir, safeFilename);
            Files.copy(avatarFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            String avatarUrl = "http://45.119.82.37:8080/uploads/" + safeFilename;
            account.setAvatarUrl(avatarUrl);
            accountRepository.save(account);

            return avatarUrl;

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload avatar", e);
        }
    }

    @Transactional
    public void updatePreference(Map<String, String> preferenceParams) {
        Long userId = authService.getUserId();
        Account account = accountRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("Account not found"));

        Map<String, Object> preference = new HashMap<>(preferenceParams);
        account.setPreference(preference);

        accountRepository.save(account);
    }

    public Page<Account> getAllUsers(Pageable pageable) {
        return accountRepository.findByRole("USER", pageable);
    }

    public Optional<Account> getUserById(Long id) {
        return accountRepository.findById(id);
    }

    @Transactional
    public void deactivateUser(Long id) {
        Optional<Account> accountOpt = accountRepository.findById(id);
        if (accountOpt.isPresent()) {
            Account account = accountOpt.get();
            account.setStatus("INACTIVE");
            accountRepository.save(account);
        }
    }
}

