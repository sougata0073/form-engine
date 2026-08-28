package com.sougata.auth_service.controller;

import com.sougata.auth_service.dto.UserSummariesShortDto;
import com.sougata.auth_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/v1/users")
@CrossOrigin
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "summaries-short")
    public UserSummariesShortDto userSummaries(
            @RequestBody List<UUID> userIds
    ) {
        return userService.getUserSummariesShort(userIds);
    }
}
