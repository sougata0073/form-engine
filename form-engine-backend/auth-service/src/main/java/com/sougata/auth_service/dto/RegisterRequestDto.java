package com.sougata.auth_service.dto;

import com.sougata.auth_service.constant.ValidationMessages;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDto {
    private String username;

    @NotBlank(message = ValidationMessages.EMPTY_EMAIL)
    @Email(message = ValidationMessages.WRONG_EMAIL_FORMAT)
    private String email;

    @NotBlank(message = ValidationMessages.EMPTY_PASSWORD)
    private String password;

    private String avatarUrl;
}
