package com.sougata.form_service.controller;

import com.sougata.form_engine.constant.cache.FormCacheNames;
import com.sougata.form_engine.constant.cache.QuestionCacheNames;
import com.sougata.form_engine.dto.form.*;
import com.sougata.form_engine.dto.others.SuccessMessageDto;
import com.sougata.form_engine.dto.question.details.QuestionDetailsDto;
import com.sougata.form_engine.dto.question.schemaRequest.QuestionOrderUpdateReqDto;
import com.sougata.form_engine.dto.question.schemaRequest.QuestionPutReqDto;
import com.sougata.form_engine.dto.question.summary.QuestionSummariesDto;
import com.sougata.form_engine.dto.question.summary.QuestionSummaryDto;
import com.sougata.form_service.service.formSchema.FormService;
import com.sougata.form_service.service.formSchema.FormServiceCached;
import com.sougata.form_service.service.formSchema.QuestionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/forms")
@CrossOrigin
public class FormController {

    private final FormService formService;
    private final FormServiceCached formServiceCached;
    private final QuestionService questionService;

    @Autowired
    public FormController(FormService formService, FormServiceCached formServiceCached, QuestionService questionService) {
        this.formService = formService;
        this.formServiceCached = formServiceCached;
        this.questionService = questionService;
    }

    @PostMapping
    public ResponseEntity<FormInfoDto> addForm(
            @Valid @RequestBody FormPutReqDto req,
            @RequestHeader("auth-jwt") UUID userId
    ) {
        var res = formService.createForm(req, userId);
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PostMapping(path = "{formId}/copy")
    public ResponseEntity<FormInfoDto> copyForm(
            @Valid @RequestBody CopyFormReqDto req,
            @PathVariable("formId") UUID formId,
            @RequestHeader("auth-jwt") UUID userId
    ) {
        var res = formService.copyForm(formId, req, userId);
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @DeleteMapping(path = "{formId}")
    @Caching(evict = {
            @CacheEvict(cacheNames = {FormCacheNames.FORM_DETAILS, FormCacheNames.FORM_INFO}, key = "#formId"),
    })
    public SuccessMessageDto deleteForm(
            @PathVariable("formId") UUID formId,
            @RequestHeader("auth-jwt") UUID userId
    ) {
        return formService.deleteForm(formId, userId);
    }

    @PutMapping(path = "{formId}")
    public FormInfoDto updateForm(
            @PathVariable("formId") UUID formId,
            @Valid @RequestBody FormPutReqDto dto,
            @RequestHeader("auth-jwt") UUID userId
    ) {
        return formService.updateForm(formId, dto, userId);
    }

    @PatchMapping(path = "{formId}/rename")
    public SuccessMessageDto renameForm(
            @PathVariable("formId") UUID formId,
            @RequestBody FormRenameReqDto body,
            @RequestHeader("auth-jwt") UUID userId
    ) {
        return formService.renameForm(formId, body, userId);
    }

    @GetMapping(path = "{formId}")
    public FormDetailsDto getForm(
            @PathVariable("formId") UUID formId,
            @RequestHeader("auth-jwt") UUID userId
    ) {
        return formService.getForm(formId, userId);
    }

    @GetMapping(path = "{formId}/details")
    public FormDetailsDto getFormDetails(@PathVariable("formId") UUID formId) {
        return formServiceCached.getFormDetails(formId);
    }

    @GetMapping(path = "{formId}/view")
    public FormDetailsDto viewForm(
            @PathVariable("formId") UUID formId,
            @RequestHeader("auth-jwt") UUID userId
    ) {
        return formService.viewForm(formId, userId);
    }

    @GetMapping(path = "{formId}/info")
    @Cacheable(cacheNames = {FormCacheNames.FORM_INFO}, key = "#formId")
    public FormInfoDto getFormInfo(@PathVariable("formId") UUID formId) {
        return formService.getFormInfo(formId);
    }

    @GetMapping(path = "{formId}/question-summaries")
    @Cacheable(cacheNames = {QuestionCacheNames.QUESTION_SUMMARIES}, key = "#formId")
    public QuestionSummariesDto getQuestionSummaries(@PathVariable("formId") UUID formId) {
        return questionService.getQuestionSummaries(formId);
    }

    @GetMapping(path = "{formId}/questions/{questionId}/summary")
    @Cacheable(cacheNames = {QuestionCacheNames.QUESTION_SUMMARY}, key = "#questionId")
    public QuestionSummaryDto getQuestionSummary(
            @PathVariable("formId") UUID formId,
            @PathVariable("questionId") Long questionId
    ) {
        return questionService.getQuestionSummary(formId, questionId);
    }

    @GetMapping(path = "{formId}/questions/{questionId}")
    @Cacheable(cacheNames = {QuestionCacheNames.QUESTION_DETAILS}, key = "#questionId")
    public QuestionDetailsDto getQuestion(
            @PathVariable("formId") UUID formId,
            @PathVariable("questionId") Long questionId
    ) {
        return questionService.getQuestion(formId, questionId);
    }

    @PostMapping(path = "{formId}/questions")
    public ResponseEntity<QuestionDetailsDto> addQuestion(
            @PathVariable("formId") UUID formId,
            @Valid @RequestBody QuestionPutReqDto body
    ) {
        var res = questionService.createQuestion(formId, body);
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PutMapping(path = "{formId}/questions/{questionId}")
    public QuestionDetailsDto updateQuestion(
            @PathVariable("formId") UUID formId,
            @PathVariable("questionId") Long questionId,
            @Valid @RequestBody QuestionPutReqDto body
    ) {
        return questionService.updateQuestion(formId, questionId, body);
    }

    @DeleteMapping(path = "{formId}/questions/{questionId}")
    @Caching(evict = {
            @CacheEvict(cacheNames = {QuestionCacheNames.QUESTION_DETAILS, QuestionCacheNames.QUESTION_SUMMARY}, key = "#questionId")
    })
    public SuccessMessageDto deleteQuestion(
            @PathVariable("formId") UUID formId,
            @PathVariable("questionId") Long questionId
    ) {
        return questionService.deleteQuestion(formId, questionId);
    }

    @GetMapping(path = "recent")
    @Cacheable(cacheNames = {FormCacheNames.RECENT_FORMS}, key = "#userId")
    public FormSummariesDto getRecentForms(
            @RequestHeader("auth-jwt") UUID userId
    ) {
        return formService.getFormsSummaries(userId);
    }

    @PatchMapping(path = "{formId}/questions/{questionId}/order")
    public SuccessMessageDto updateQuestionIndex(
            @PathVariable("formId") UUID formId,
            @PathVariable("questionId") Long questionId,
            @RequestBody @Valid QuestionOrderUpdateReqDto req
    ) {
        return questionService.updateOrderIndex(formId, questionId, req);
    }
}
