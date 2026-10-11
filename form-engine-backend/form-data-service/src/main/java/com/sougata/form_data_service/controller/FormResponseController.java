package com.sougata.form_data_service.controller;

import com.sougata.form_data_service.service.FormResponseService;
import com.sougata.form_engine.constant.cache.FormDataCacheNames;
import com.sougata.form_engine.dto.form.FormResponsePutReqDto;
import com.sougata.form_engine.dto.form.FormResponsePutResDto;
import com.sougata.form_engine.dto.formResponse.individual.FormResponseIndividualDtoNoPage;
import com.sougata.form_engine.dto.others.SuccessMessageDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/forms")
@CrossOrigin
@RequiredArgsConstructor
public class FormResponseController {

    private final FormResponseService formResponseService;

    @PostMapping(path = "{formId}/responses")
    public ResponseEntity<FormResponsePutResDto> addFormResponse(
            @PathVariable("formId") UUID formId,
            @Valid @RequestBody FormResponsePutReqDto dto,
            @RequestHeader("auth-jwt") UUID responderId
    ) {
        var res = formResponseService.saveResponse(formId, dto, responderId);
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @GetMapping(path = "{formId}/responses/{formResponseId}")
    @Cacheable(cacheNames = {FormDataCacheNames.FORM_RESPONSE_NO_PAGE}, key = "#formResponseId")
    public FormResponseIndividualDtoNoPage getFormResponse(
            @PathVariable("formId") UUID formId,
            @PathVariable("formResponseId") UUID formResponseId
    ) {
        return formResponseService.getFormResponse(formId, formResponseId);
    }

    @DeleteMapping(path = "{formId}/responses/{formResponseId}")
    @CacheEvict(cacheNames = {FormDataCacheNames.FORM_RESPONSE_NO_PAGE}, key = "#formResponseId")
    public SuccessMessageDto deleteFormResponse(
            @PathVariable("formId") UUID formId,
            @PathVariable("formResponseId") UUID formResponseId
    ) {
        return formResponseService.deleteFormResponse(formId, formResponseId);
    }

}
