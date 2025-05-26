package com.huynhntp.commons.wear2ndchange.repository;

import com.huynhntp.commons.wear2ndchange.model.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
}
