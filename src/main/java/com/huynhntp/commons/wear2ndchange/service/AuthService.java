package com.huynhntp.commons.wear2ndchange.service;

import com.huynhntp.commons.wear2ndchange.config.exception.BusinessException;
import com.huynhntp.commons.wear2ndchange.config.jwt.JwtUtils;
import com.huynhntp.commons.wear2ndchange.config.jwt.TokenBlacklist;
import com.huynhntp.commons.wear2ndchange.config.security.UserDetails;
import com.huynhntp.commons.wear2ndchange.mapper.AccountMapper;
import com.huynhntp.commons.wear2ndchange.model.dto.AccountDTO;
import com.huynhntp.commons.wear2ndchange.model.dto.LoginRequest;
import com.huynhntp.commons.wear2ndchange.model.dto.LoginResponse;
import com.huynhntp.commons.wear2ndchange.model.dto.RegisterRequest;
import com.huynhntp.commons.wear2ndchange.model.entity.Account;
import com.huynhntp.commons.wear2ndchange.repository.AccountRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountMapper accountMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final TokenBlacklist tokenBlacklist;

    public AccountDTO registerUser(RegisterRequest registerRequest, HttpServletRequest request) {
        if (accountRepository.existsByUsername(registerRequest.getUsername())) {
            throw new BusinessException("Tài khoản đã tồn tại");
        }

        Account newAccount = new Account();
        newAccount.setUsername(registerRequest.getUsername());
        newAccount.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        newAccount.setRole(registerRequest.getRole() != null ? registerRequest.getRole() : "USER");

        accountRepository.save(newAccount);
        return accountMapper.toDto(newAccount);
    }

    public LoginResponse login(LoginRequest loginRequest, HttpServletRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String jwt = jwtUtils.generateToken(userDetails);

        return new LoginResponse(
                jwt,
                userDetails.getUsername(),
                userDetails.getAuthorities().stream().findFirst().orElseThrow(() -> new BusinessException("User has no assigned role")).getAuthority());
    }

    public void logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            tokenBlacklist.addToBlacklist(jwt);
        }
    }
}