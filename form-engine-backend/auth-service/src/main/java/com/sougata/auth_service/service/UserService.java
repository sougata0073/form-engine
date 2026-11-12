package com.sougata.auth_service.service;

import com.sougata.auth_service.dto.UserSummaryDto;
import com.sougata.auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserSummaryDto getUserSummary(UUID userId) {
        return userRepository.findUserSummaryById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    }
}
