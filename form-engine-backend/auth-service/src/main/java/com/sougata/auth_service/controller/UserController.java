package com.sougata.auth_service.controller;

import com.sougata.auth_service.dto.UserSummariesShortDto;
import com.sougata.auth_service.dto.UserSummaryDto;
import com.sougata.auth_service.service.UserService;
import com.sougata.form_engine.constant.cache.AuthCacheNames;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping(path = "summaries-short")
    public UserSummariesShortDto userSummaries(
            @RequestBody List<UUID> userIds
    ) {
        return userService.getUserSummariesShort(userIds);
    }

    @GetMapping(path = "{userId}/summary")
    @Cacheable(cacheNames = AuthCacheNames.USER_SUMMARY, key = "#userId")
    public UserSummaryDto getUserSummary(
            @PathVariable("userId") UUID userId
    ) {
        return userService.getUserSummary(userId);
    }
}
