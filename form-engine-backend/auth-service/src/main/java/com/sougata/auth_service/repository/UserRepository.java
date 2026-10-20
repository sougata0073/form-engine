package com.sougata.auth_service.repository;

import com.sougata.auth_service.constant.AuthProvider;
import com.sougata.auth_service.dto.UserSummaryShortDto;
import com.sougata.auth_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmailAndAuthProvider(String email, AuthProvider authProvider);

    boolean existsByEmailAndAuthProvider(String email, AuthProvider authProvider);

    Optional<User> findBySocialAuthIdAndAuthProvider(String socialAuthId, AuthProvider authProvider);

    @Query("""
            select
            new com.sougata.auth_service.dto.UserSummaryShortDto(
                u.id,
                u.username
            )
            from User u
            where u.id in :userIds
            """)
    List<UserSummaryShortDto> getUserSummariesShort(List<UUID> userIds);
}
