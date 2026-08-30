package com.sougata.form_data_service.controller;

import com.sougata.form_engine.dto.others.SuccessMessageDto;
import com.sougata.form_engine.dto.form.FormResponsePutResDto;
import com.sougata.form_data_service.service.FormResponseService;
import com.sougata.form_engine.dto.form.FormResponsePutReqDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    @PostMapping(path = "{formId}/responses")
    public ResponseEntity<FormResponsePutResDto> addFormResponse(
            @PathVariable("formId") UUID formId,
            @Valid @RequestBody FormResponsePutReqDto dto,
            @RequestHeader("auth-jwt") UUID responderId
    ) {
        var res = formResponseService.saveResponse(formId, dto, responderId);
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @DeleteMapping(path = "{formId}/responders/{responderId}/responses/{formResponseId}")
    public SuccessMessageDto deleteFormResponse(
            @PathVariable("formId") UUID formId,
            @PathVariable("responderId") UUID responderId,
            @PathVariable("formResponseId") Long formResponseId
    ) {
        return formResponseService.deleteFormResponse(formId, responderId, formResponseId);
    }

}
