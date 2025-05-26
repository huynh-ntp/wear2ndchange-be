package com.huynhntp.commons.wear2ndchange.service;

import com.huynhntp.commons.wear2ndchange.config.exception.BusinessException;
import com.huynhntp.commons.wear2ndchange.config.jwt.JwtUtils;
import com.huynhntp.commons.wear2ndchange.config.jwt.TokenBlacklist;
import com.huynhntp.commons.wear2ndchange.config.security.UserDetails;
import com.huynhntp.commons.wear2ndchange.infra.mail.*;
import com.huynhntp.commons.wear2ndchange.mapper.AccountMapper;
import com.huynhntp.commons.wear2ndchange.model.dto.*;
import com.huynhntp.commons.wear2ndchange.model.entity.*;
import com.huynhntp.commons.wear2ndchange.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class AuthService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountMapper accountMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final TokenBlacklist tokenBlacklist;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final MsgService msgService;

    public AccountDTO registerUser(RegisterRequest registerRequest, HttpServletRequest request) {
        if (accountRepository.existsByUsername(registerRequest.getUsername())) {
            throw new BusinessException("Tài khoản đã tồn tại");
        }

        if (accountRepository.existsByEmail(registerRequest.getEmail())) {
            throw new BusinessException("Email đã tồn tại");
        }

        Account newAccount = new Account();
        newAccount.setUsername(registerRequest.getUsername());
        newAccount.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        newAccount.setEmail(registerRequest.getEmail());
        newAccount.setPhoneNumber(registerRequest.getPhoneNumber());
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

    @Transactional
    public boolean createPasswordResetToken(Account account, String token) {
        boolean shouldCreateToken = passwordResetTokenRepository.findByAccountId(account.getId())
                .map(existingToken -> {
                    if (existingToken.isExpired()) {
                        passwordResetTokenRepository.deleteByAccount_Id(existingToken.getAccount().getId());
                        passwordResetTokenRepository.flush();
                        return true;
                    }
                    return false;
                })
                .orElse(true);

        if (shouldCreateToken) {
            PasswordResetToken prt = new PasswordResetToken();
            prt.setToken(token);
            prt.setAccount(account);
            prt.setExpiryDate(LocalDateTime.now().plusMinutes(30));
            passwordResetTokenRepository.save(prt);
            return true;
        }

        return false;
    }

    @Transactional
    public void forgotPassword(ForgotPasswordForm forgotPasswordForm) {
        Optional<Account> userOpt = accountRepository.findByEmail(forgotPasswordForm.getEmail());
        if (userOpt.isEmpty()) {
            throw new BusinessException("email not found");
        }

        Account user = userOpt.get();
        String token = UUID.randomUUID().toString();

        boolean passwordResetToken = createPasswordResetToken(user, token);

        if (passwordResetToken) {
            String resetLink = "http://45.119.82.37:8080/api/auth/reset-password?token=" + token;
            Msg msg = new Msg().setMsgUser(
                            new Account().setEmail(user.getEmail()))
                    .setParams(Map.of(
                            "link", resetLink,
                            "emailTo", user.getEmail())
                    );
            msgService.send(msg);
        } else {
            throw new BusinessException("Check your email to get password");
        }
    }

    @Transactional
    public void resetPassword(UUID token) {

    }
}