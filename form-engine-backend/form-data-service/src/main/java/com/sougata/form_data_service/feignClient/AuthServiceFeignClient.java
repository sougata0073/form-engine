package com.sougata.form_data_service.feignClient;

import com.sougata.form_engine.dto.user.UserSummaryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(value = "auth-service")
public interface AuthServiceFeignClient {

    @GetMapping(path = "api/v1/users/{userId}/summary")
    UserSummaryDto getUserSummary(@PathVariable("userId") UUID userId);

}
