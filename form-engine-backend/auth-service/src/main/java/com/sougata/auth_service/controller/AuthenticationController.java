package com.sougata.auth_service.controller;

import com.sougata.auth_service.dto.JwtTokenResponseDto;
import com.sougata.auth_service.dto.LoginRequestDto;
import com.sougata.auth_service.dto.RegisterRequestDto;
import com.sougata.auth_service.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/auth")
@CrossOrigin
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping(path = "register/email-pass")
    public JwtTokenResponseDto registerUser(
            @RequestBody @Validated RegisterRequestDto dto
    ) {
        return authenticationService.registerUser(dto);
    }

    @PostMapping(path = "login/email-pass")
    public JwtTokenResponseDto loginUser(
            @RequestBody @Validated LoginRequestDto dto
    ) {
        return authenticationService.loginUser(dto);
    }

}
