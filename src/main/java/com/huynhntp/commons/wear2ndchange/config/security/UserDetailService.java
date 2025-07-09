package com.huynhntp.commons.wear2ndchange.config.security;

import com.huynhntp.commons.wear2ndchange.model.entity.Account;
import com.huynhntp.commons.wear2ndchange.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class UserDetailService implements UserDetailsService {

    private AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> account = accountRepository.findByUsername(username);
        return account.map(com.huynhntp.commons.wear2ndchange.config.security.UserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

}
