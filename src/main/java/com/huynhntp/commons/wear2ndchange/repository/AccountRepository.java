package com.huynhntp.commons.wear2ndchange.repository;

import com.huynhntp.commons.wear2ndchange.model.entity.Account;
import org.springframework.data.jpa.repository.*;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByUsername(String username);

    Optional<Account> findByUsername(String username);

    boolean existsByEmail(String email);

    @Query("SELECT a FROM Account a where a.status = 'ACTIVE'")
    Optional<Account> findByEmail(String email);
}