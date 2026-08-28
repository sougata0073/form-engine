package com.sougata.form_response_service.feignClient;

import com.sougata.form_engine.dto.user.UserSummariesShortDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

@FeignClient("auth-service")
public interface AuthServiceFeignClient {

    @PostMapping(path = "api/v1/users/summaries-short")
    UserSummariesShortDto userSummaries(@RequestBody List<UUID> userIds);

}
