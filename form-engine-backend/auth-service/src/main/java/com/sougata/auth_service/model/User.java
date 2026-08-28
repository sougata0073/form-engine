package com.sougata.auth_service.model;

import com.sougata.auth_service.constant.AuthProvider;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;

@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "unique_auth_provider_social_auth_id",
                        columnNames = {"social_auth_id", "auth_provider"}
                )
        }
)
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private String socialAuthId;

    @Column(nullable = false)
    private String username;

    private String email;

    @Column(nullable = false)
    private Boolean isEmailVerified;

    @Column(unique = true)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private AuthProvider authProvider;

    @Column(columnDefinition = "TEXT")
    private String avatarUrl;
}
