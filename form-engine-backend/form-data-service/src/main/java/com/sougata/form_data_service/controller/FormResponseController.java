package com.sougata.form_data_service.controller;

import com.sougata.form_data_service.dto.common.SuccessMessageDto;
import com.sougata.form_data_service.dto.form.FormResponsePutReqDto;
import com.sougata.form_data_service.dto.form.FormResponsePutResDto;
import com.sougata.form_data_service.service.FormResponseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "api/v1/forms")
@CrossOrigin
@RequiredArgsConstructor
public class FormResponseController {

    private final FormResponseService formResponseService;

    @PostMapping(path = "{formId}/response")
    public ResponseEntity<FormResponsePutResDto> addFormResponse(
            @PathVariable("formId") UUID formId,
            @Valid @RequestBody FormResponsePutReqDto dto,
            @RequestHeader("auth-jwt") UUID authJwt
    ) {
        var res = formResponseService.saveResponse(formId, dto, authJwt);
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @DeleteMapping(path = "{formId}/users/{userId}/responses/{formResponseId}")
    public SuccessMessageDto deleteFormResponse(
            @PathVariable("formId") UUID formId,
            @PathVariable("userId") UUID userId,
            @PathVariable("formResponseId") Long formResponseId
    ) {
        return formResponseService.deleteFormResponse(formId, userId, formResponseId);
    }

}
