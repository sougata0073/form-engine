package com.sougata.form_data_service.feignClient;

import com.sougata.form_data_service.dto.form.FormDetailsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient("form-service")
public interface FormServiceFeignClient {

    @GetMapping(path = "api/v1/forms/{formId}/details")
    FormDetailsDto getFormDetails(@PathVariable("formId") UUID formId);
}
