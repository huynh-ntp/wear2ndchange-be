package com.huynhntp.commons.wear2ndchange.service;

import com.huynhntp.commons.wear2ndchange.config.exception.BusinessException;
import com.huynhntp.commons.wear2ndchange.mapper.AccountMapper;
import com.huynhntp.commons.wear2ndchange.model.dto.AccountDTO;
import com.huynhntp.commons.wear2ndchange.model.entity.Account;
import com.huynhntp.commons.wear2ndchange.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public AccountDTO viewProfile(Long userId) {
        Account account = accountRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("User not found"));

        return accountMapper.toDto(account);
    }
}
