package com.sougata.form_response_service.feignClient;

import com.sougata.form_engine.dto.formResponse.individual.FormResponseIndividualDtoNoPage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient("form-data-service")
public interface FormDataServiceFeignClient {

    @GetMapping(path = "api/v1/forms/{formId}/responses/{formResponseId}")
    FormResponseIndividualDtoNoPage getFormResponse(@PathVariable("formId") UUID formId, @PathVariable("formResponseId") UUID formResponseId);

}
