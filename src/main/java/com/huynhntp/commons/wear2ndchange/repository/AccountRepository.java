package com.huynhntp.commons.wear2ndchange.repository;

import com.huynhntp.commons.wear2ndchange.model.entity.Account;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByUsername(String username);

    Optional<Account> findByUsername(String username);

    boolean existsByEmail(String email);

    @Query("SELECT a FROM Account a where a.status = 'ACTIVE' and a.email = :email")
    Optional<Account> findByEmail(@Param("email") String email);
}