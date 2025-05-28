package com.huynhntp.commons.wear2ndchange.controller;

import com.huynhntp.commons.wear2ndchange.model.dto.*;
import com.huynhntp.commons.wear2ndchange.service.*;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;


@RestController
@RequestMapping("/api/account")
@AllArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/profile")
    public AccountDTO resetPassword(@RequestParam Long userId) {
        return accountService.viewProfile(userId);
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request) {
        accountService.changePassword(request);
        return ResponseEntity.ok("Password changed successfully");
    }

    @PatchMapping
    public ResponseEntity<?> updateAccount(@RequestBody UpdateAccountForm form) {
        accountService.updateAccount(form);
        return ResponseEntity.ok("Account updated successfully");
    }

    @PostMapping(value = "/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadAvatar(@RequestParam("avatar") MultipartFile avatarFile) {
        String avatarUrl = accountService.uploadAvatar(avatarFile);
        return ResponseEntity.ok(Map.of("avatarUrl", avatarUrl));
    }

    @PutMapping("/preference")
    public ResponseEntity<?> updatePreference(@RequestParam Map<String, String> preferenceParams) {
        accountService.updatePreference(preferenceParams);
        return ResponseEntity.ok("Preference updated successfully");
    }

}