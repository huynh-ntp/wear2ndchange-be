package com.huynhntp.commons.wear2ndchange.controller;

import com.huynhntp.commons.wear2ndchange.infra.mail.*;
import com.huynhntp.commons.wear2ndchange.model.dto.*;
import com.huynhntp.commons.wear2ndchange.model.entity.Account;
import com.huynhntp.commons.wear2ndchange.repository.AccountRepository;
import com.huynhntp.commons.wear2ndchange.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        authService.logout(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/register")
    public ResponseEntity<?> signUp(@RequestBody RegisterRequest accountDTO, HttpServletRequest request) {
        return new ResponseEntity<>(authService.registerUser(accountDTO, request), HttpStatus.OK);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        return new ResponseEntity<>(authService.login(loginRequest, request), HttpStatus.OK);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordForm request) {
        authService.forgotPassword(request);

        return ResponseEntity.ok("Reset password link sent to your email.");
    }
}