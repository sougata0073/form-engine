package com.sougata.auth_service.dto;

import com.sougata.auth_service.constant.ValidationMessages;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDto {
    private @NotBlank String email;

    private @NotBlank(message = ValidationMessages.EMPTY_PASSWORD) String password;
}
