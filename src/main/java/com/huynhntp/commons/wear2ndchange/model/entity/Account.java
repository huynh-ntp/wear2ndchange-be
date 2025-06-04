package com.huynhntp.commons.wear2ndchange.model.entity;

import com.huynhntp.commons.wear2ndchange.infra.mail.MsgUser;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.*;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Entity
@Table(name = "accounts")
@Accessors(chain = true)
public class Account  implements MsgUser {
    @Transient
    private final String ACTIVE = "ACTIVE";
    @Transient
    private final String INACTIVE = "INACTIVE";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String phoneNumber;

    private String fullName;

    private String avatarUrl;

    private String address;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private String status = ACTIVE;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> preference;
}