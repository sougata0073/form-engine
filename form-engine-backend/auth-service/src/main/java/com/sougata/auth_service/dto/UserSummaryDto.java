package com.sougata.auth_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSummaryDto {
    private UUID userId;
    private String userName;
    private String email;
    private String avatarUrl;
}
