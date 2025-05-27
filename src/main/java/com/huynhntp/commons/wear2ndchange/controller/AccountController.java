package com.huynhntp.commons.wear2ndchange.controller;

import com.huynhntp.commons.wear2ndchange.model.dto.*;
import com.huynhntp.commons.wear2ndchange.service.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/account")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/profile")
    public AccountDTO resetPassword(@RequestParam Long userId) {
        return accountService.viewProfile(userId);
    }
}