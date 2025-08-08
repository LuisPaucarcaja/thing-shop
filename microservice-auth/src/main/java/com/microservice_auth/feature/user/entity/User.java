package com.microservice_auth.feature.user.entity;

import com.microservice_auth.common.audit.Auditable;
import com.microservice_auth.feature.user.enums.AccountStatus;
import com.microservice_auth.feature.user.enums.AuthProvider;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true , nullable = false)
    private String email;

    private String password;

    @Column(length = 150)
    private String fullName;

    @Column(length = 20)
    private String phoneNumber;

    private String avatarUrl;

    @Enumerated(EnumType.STRING)
    @Column(length = 40, nullable = false)
    private AccountStatus accountStatus ;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private AuthProvider provider;
}
