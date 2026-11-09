package com.sougata.form_data_service.controller;

import com.sougata.form_data_service.service.FormDataService;
import com.sougata.form_data_service.service.FormDataServiceCached;
import com.sougata.form_engine.constant.HttpHeaderConstant;
import com.sougata.form_engine.constant.cache.FormDataCacheNames;
import com.sougata.form_engine.dto.form.FormResponsePutReqDto;
import com.sougata.form_engine.dto.form.FormResponsePutResDto;
import com.sougata.form_engine.dto.formResponse.individual.FormResponseIndividualDtoNoPage;
import com.sougata.form_engine.dto.others.IsResponseSubmittedDto;
import com.sougata.form_engine.dto.others.SuccessMessageDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/form-data")
@RequiredArgsConstructor
public class FormDataController {

    private final FormDataService formDataService;
    private final FormDataServiceCached formDataServiceCached;

    @PostMapping(path = "{formId}/responses")
    public ResponseEntity<FormResponsePutResDto> saveFormResponse(
            @PathVariable("formId") UUID formId,
            @Valid @RequestBody FormResponsePutReqDto dto,
            @RequestHeader(value = HttpHeaderConstant.X_USER_ID, required = false) UUID responderId,
            @RequestHeader(HttpHeaderConstant.X_FORWARDED_FOR) String clientIps
    ) {
        var res = formDataService.saveFormResponse(formId, dto, responderId, clientIps);
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @GetMapping(path = "{formId}/responses/{formResponseId}")
    public FormResponseIndividualDtoNoPage getFormResponse(
            @PathVariable("formId") UUID formId,
            @PathVariable("formResponseId") UUID formResponseId
    ) {
        return formDataServiceCached.getFormResponse(formId, formResponseId);
    }

    @DeleteMapping(path = "{formId}/responses/{formResponseId}")
    @CacheEvict(
            cacheNames = {FormDataCacheNames.FORM_RESPONSE_NO_PAGE},
            key = "'formId=' + :formId + '::formResponseId=' + #formResponseId"
    )
    public SuccessMessageDto deleteFormResponse(
            @PathVariable("formId") UUID formId,
            @PathVariable("formResponseId") UUID formResponseId
    ) {
        return formDataService.deleteFormResponse(formId, formResponseId);
    }

    @GetMapping(path = "{formId}/responders/{responderId}/is-response-submitted")
    public IsResponseSubmittedDto getIsResponseAlreadySubmitted(
            @PathVariable("formId") UUID formId,
            @PathVariable("responderId") String responderId
    ) {
        return formDataServiceCached.getIsResponseAlreadySubmitted(formId, responderId);
    }

}
