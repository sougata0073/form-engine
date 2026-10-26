package com.sougata.form_service.service.formSchema.impl;

import com.sougata.form_engine.constant.ViewFormErrorReason;
import com.sougata.form_engine.constant.cache.CommonCacheNames;
import com.sougata.form_engine.constant.cache.FormCacheNames;
import com.sougata.form_engine.constant.cache.QuestionCacheNames;
import com.sougata.form_engine.constant.messaging.MessagingChannelNames;
import com.sougata.form_engine.dto.form.*;
import com.sougata.form_engine.dto.messaging.FormDeletedMessage;
import com.sougata.form_engine.dto.others.SuccessMessageDto;
import com.sougata.form_engine.dto.question.details.QuestionDetailsDto;
import com.sougata.form_engine.dto.template.TemplateDetails;
import com.sougata.form_service.configuration.AppConfiguration;
import com.sougata.form_service.exception.FormNotAcceptingResponseException;
import com.sougata.form_service.exception.FormNotFoundException;
import com.sougata.form_service.exception.FormResponseAlreadySubmittedException;
import com.sougata.form_service.feignClient.FormDataServiceFeignClient;
import com.sougata.form_service.feignClient.FormResponseServiceFeignClient;
import com.sougata.form_service.model.formSchema.*;
import com.sougata.form_service.repository.formSchema.FormRepository;
import com.sougata.form_service.service.formSchema.FormService;
import com.sougata.form_service.service.formSchema.FormServiceCached;
import com.sougata.form_service.service.formSchema.questionManager.QuestionManagerFactory;
import com.sougata.form_service.util.CacheUtil;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FormServiceImpl implements FormService {

    private final FormRepository formRepo;
    private final FormResponseServiceFeignClient formResponseServiceFeignClient;
    private final FormDataServiceFeignClient formDataServiceFeignClient;
    private final FormServiceCached formServiceCached;
    private final QuestionManagerFactory questionManagerFactory;
    private final RedisTemplate<String, Object> redisTemplate;
    private final StringRedisTemplate stringRedisTemplate;
    private final AppConfiguration appConfiguration;
    private final RedissonClient redissonClient;

    @Override
    public FormInfoDto createForm(UUID userId) {
        var newForm = new Form();

        var newFormDefaultSettings = new FormDefaultSettings();
        var newFormGeneralSettings = new FormGeneralSettings();
        var newFormPresentationSettings = new FormPresentationSettings();
        var newFormQuizSettings = new FormQuizSettings();
        var newFormResponseSettings = new FormResponseSettings();

        newFormDefaultSettings.setForm(newForm);
        newFormGeneralSettings.setForm(newForm);
        newFormPresentationSettings.setForm(newForm);
        newFormQuizSettings.setForm(newForm);
        newFormResponseSettings.setForm(newForm);

        newForm.setUserId(userId);
        newForm.setName("Untitled form");
        newForm.setLastOpenedOn(Instant.now());

        newForm.setFormDefaultSettings(newFormDefaultSettings);
        newForm.setFormGeneralSettings(newFormGeneralSettings);
        newForm.setFormPresentationSettings(newFormPresentationSettings);
        newForm.setFormQuizSettings(newFormQuizSettings);
        newForm.setFormResponseSettings(newFormResponseSettings);

        var savedForm = formRepo.save(newForm);

        var formInfo = toFormInfo(savedForm);

        addFirstInRecentForms(userId, formInfo);
        putFormInfo(formInfo);

        return formInfo;
    }

    @Override
    @Transactional
    public FormInfoDto copyForm(UUID formId, CopyFormReqDto req, UUID userId) {
        var referenceFormDetails = formServiceCached.getFormDetails(formId);

        var referenceFormDefaultSettings = referenceFormDetails.getFormInfo().getDefaultSettings();
        var referenceFormGeneralSettings = referenceFormDetails.getFormInfo().getGeneralSettings();
        var referenceFormPresentationSettings = referenceFormDetails.getFormInfo().getPresentationSettings();
        var referenceFormQuizSettings = referenceFormDetails.getFormInfo().getQuizSettings();
        var referenceFormResponseSettings = referenceFormDetails.getFormInfo().getResponseSettings();

        var newForm = new Form();

        var newFormDefaultSettings = new FormDefaultSettings();
        var newFormGeneralSettings = new FormGeneralSettings();
        var newFormPresentationSettings = new FormPresentationSettings();
        var newFormQuizSettings = new FormQuizSettings();
        var newFormResponseSettings = new FormResponseSettings();

        newFormDefaultSettings.setForm(newForm);
        newFormDefaultSettings.setDefaultEmailCollectionOption(referenceFormDefaultSettings.getDefaultEmailCollectionOption());
        newFormDefaultSettings.setMakeQuestionsRequiredByDefault(referenceFormDefaultSettings.getMakeQuestionsRequiredByDefault());

        newFormGeneralSettings.setForm(newForm);
        newFormGeneralSettings.setPublished(referenceFormGeneralSettings.getPublished());
        newFormGeneralSettings.setAcceptingResponse(referenceFormGeneralSettings.getAcceptingResponse());
        newFormGeneralSettings.setNotAcceptingResponseMessage(referenceFormGeneralSettings.getNotAcceptingResponseMessage());
        newFormGeneralSettings.setStopAcceptingResponseOn(referenceFormGeneralSettings.getStopAcceptingResponseOn());
        newFormGeneralSettings.setResponseLimit(referenceFormGeneralSettings.getResponseLimit());

        newFormPresentationSettings.setForm(newForm);
        newFormPresentationSettings.setShowProgressBar(referenceFormPresentationSettings.getShowProgressBar());
        newFormPresentationSettings.setShuffleQuestionOrder(referenceFormPresentationSettings.getShuffleQuestionOrder());
        newFormPresentationSettings.setResponseSavedConfirmationMessage(referenceFormPresentationSettings.getResponseSavedConfirmationMessage());
        newFormPresentationSettings.setShowLinkToSubmitAnotherResponse(referenceFormPresentationSettings.getShowLinkToSubmitAnotherResponse());
        newFormPresentationSettings.setShareResponseSummaryWithResponders(referenceFormPresentationSettings.getShareResponseSummaryWithResponders());
        newFormPresentationSettings.setDisableAutoSaveForAllRespondents(referenceFormPresentationSettings.getDisableAutoSaveForAllRespondents());

        newFormQuizSettings.setForm(newForm);
        newFormQuizSettings.setIsQuiz(referenceFormQuizSettings.getIsQuiz());
        newFormQuizSettings.setReleaseMarksOption(referenceFormQuizSettings.getReleaseMarksOption());
        newFormQuizSettings.setRespondersCanSeeIncorrectAnswers(referenceFormQuizSettings.getRespondersCanSeeIncorrectAnswers());
        newFormQuizSettings.setRespondersCanSeeCorrectAnswers(referenceFormQuizSettings.getRespondersCanSeeCorrectAnswers());
        newFormQuizSettings.setRespondersCanSeePointsReceived(referenceFormQuizSettings.getRespondersCanSeePointsReceived());
        newFormQuizSettings.setQuestionPointValue(referenceFormQuizSettings.getQuestionPointValue());

        newFormResponseSettings.setForm(newForm);
        newFormResponseSettings.setEmailCollectionOption(referenceFormResponseSettings.getEmailCollectionOption());
        newFormResponseSettings.setResponseCopySendOption(referenceFormResponseSettings.getResponseCopySendOption());
        newFormResponseSettings.setIsResponseEditable(referenceFormResponseSettings.getIsResponseEditable());
        newFormResponseSettings.setSingleResponseOnly(referenceFormResponseSettings.getSingleResponseOnly());

        newForm.setUserId(userId);
        newForm.setName(req.getFormName());
        newForm.setTitle(referenceFormDetails.getFormInfo().getTitle());
        newForm.setDescription(referenceFormDetails.getFormInfo().getDescription());
        newForm.setLastOpenedOn(Instant.now());

        newForm.setFormDefaultSettings(newFormDefaultSettings);
        newForm.setFormGeneralSettings(newFormGeneralSettings);
        newForm.setFormPresentationSettings(newFormPresentationSettings);
        newForm.setFormQuizSettings(newFormQuizSettings);
        newForm.setFormResponseSettings(newFormResponseSettings);

        var savedForm = formRepo.save(newForm);

        referenceFormDetails.getQuestions().forEach(q -> {
            var manager = questionManagerFactory.get(q.getQuestionType());
            var addUpdateReq = manager.toQuestionAddUpdateReq(q);
            manager.create(savedForm.getId(), addUpdateReq);
        });

        var formInfo = toFormInfo(savedForm);

        addFirstInRecentForms(userId, formInfo);
        putFormInfo(formInfo);

        return formInfo;
    }

    @Override
    public FormInfoDto updateForm(UUID formId, FormUpdateReqDto dto, UUID userId) {
        var formToUpdate = getFormById(formId);

        var formToUpdateDefaultSettings = formToUpdate.getFormDefaultSettings();
        var formToUpdateGeneralSettings = formToUpdate.getFormGeneralSettings();
        var formToUpdatePresentationSettings = formToUpdate.getFormPresentationSettings();
        var formToUpdateQuizSettings = formToUpdate.getFormQuizSettings();
        var formToUpdateResponseSettings = formToUpdate.getFormResponseSettings();

        var dtoDefaultSettings = dto.getDefaultSettingsUpdateReq();
        var dtoGeneralSettings = dto.getGeneralSettingsUpdateReq();
        var dtoPresentationSettings = dto.getPresentationSettingsUpdateReq();
        var dtoQuizSettings = dto.getQuizSettingsUpdateReq();
        var dtoResponseSettings = dto.getResponseSettingsUpdateReq();

        dto.getUpdateFields().forEach(field -> {
            switch (field) {
                case FormUpdateReqDto.Fields.name -> formToUpdate.setName(dto.getName());
                case FormUpdateReqDto.Fields.title -> formToUpdate.setTitle(dto.getTitle());
                case FormUpdateReqDto.Fields.description -> formToUpdate.setDescription(dto.getDescription());

                case FormUpdateReqDto.Fields.defaultSettingsUpdateReq ->
                        dto.getDefaultSettingsUpdateReq().getUpdateFields().forEach(settingsField -> {
                            switch (settingsField) {
                                case FormDefaultSettingsUpdateReqDto.Fields.defaultEmailCollectionOption ->
                                        formToUpdateDefaultSettings.setDefaultEmailCollectionOption(dtoDefaultSettings.getDefaultEmailCollectionOption());
                                case FormDefaultSettingsUpdateReqDto.Fields.makeQuestionsRequiredByDefault ->
                                        formToUpdateDefaultSettings.setMakeQuestionsRequiredByDefault(dtoDefaultSettings.getMakeQuestionsRequiredByDefault());
                            }
                        });

                case FormUpdateReqDto.Fields.generalSettingsUpdateReq ->
                        dtoGeneralSettings.getUpdateFields().forEach(settingsField -> {
                            switch (settingsField) {
                                case FormGeneralSettingsUpdateReqDto.Fields.published ->
                                        formToUpdateGeneralSettings.setPublished(dtoGeneralSettings.getPublished());
                                case FormGeneralSettingsUpdateReqDto.Fields.acceptingResponse ->
                                        formToUpdateGeneralSettings.setAcceptingResponse(dtoGeneralSettings.getAcceptingResponse());
                                case FormGeneralSettingsUpdateReqDto.Fields.notAcceptingResponseMessage ->
                                        formToUpdateGeneralSettings.setNotAcceptingResponseMessage(dtoGeneralSettings.getNotAcceptingResponseMessage());
                                case FormGeneralSettingsUpdateReqDto.Fields.stopAcceptingResponseOn ->
                                        formToUpdateGeneralSettings.setStopAcceptingResponseOn(dtoGeneralSettings.getStopAcceptingResponseOn());
                                case FormGeneralSettingsUpdateReqDto.Fields.responseLimit ->
                                        formToUpdateGeneralSettings.setResponseLimit(dtoGeneralSettings.getResponseLimit());
                            }
                        });

                case FormUpdateReqDto.Fields.presentationSettingsUpdateReq ->
                        dtoPresentationSettings.getUpdateFields().forEach(settingsField -> {
                            switch (settingsField) {
                                case FormPresentationSettingsUpdateReqDto.Fields.showProgressBar ->
                                        formToUpdatePresentationSettings.setShowProgressBar(dtoPresentationSettings.getShowProgressBar());
                                case FormPresentationSettingsUpdateReqDto.Fields.shuffleQuestionOrder ->
                                        formToUpdatePresentationSettings.setShuffleQuestionOrder(dtoPresentationSettings.getShuffleQuestionOrder());
                                case FormPresentationSettingsUpdateReqDto.Fields.responseSavedConfirmationMessage ->
                                        formToUpdatePresentationSettings.setResponseSavedConfirmationMessage(dtoPresentationSettings.getResponseSavedConfirmationMessage());
                                case FormPresentationSettingsUpdateReqDto.Fields.showLinkToSubmitAnotherResponse ->
                                        formToUpdatePresentationSettings.setShowLinkToSubmitAnotherResponse(dtoPresentationSettings.getShowLinkToSubmitAnotherResponse());
                                case FormPresentationSettingsUpdateReqDto.Fields.shareResponseSummaryWithResponders ->
                                        formToUpdatePresentationSettings.setShareResponseSummaryWithResponders(dtoPresentationSettings.getShareResponseSummaryWithResponders());
                                case FormPresentationSettingsUpdateReqDto.Fields.disableAutoSaveForAllRespondents ->
                                        formToUpdatePresentationSettings.setDisableAutoSaveForAllRespondents(dtoPresentationSettings.getDisableAutoSaveForAllRespondents());
                            }
                        });

                case FormUpdateReqDto.Fields.quizSettingsUpdateReq ->
                        dtoQuizSettings.getUpdateFields().forEach(settingsField -> {
                            switch (settingsField) {
                                case FormQuizSettingsUpdateReqDto.Fields.isQuiz ->
                                        formToUpdateQuizSettings.setIsQuiz(dtoQuizSettings.getIsQuiz());
                                case FormQuizSettingsUpdateReqDto.Fields.releaseMarksOption ->
                                        formToUpdateQuizSettings.setReleaseMarksOption(dtoQuizSettings.getReleaseMarksOption());
                                case FormQuizSettingsUpdateReqDto.Fields.respondersCanSeeIncorrectAnswers ->
                                        formToUpdateQuizSettings.setRespondersCanSeeIncorrectAnswers(dtoQuizSettings.getRespondersCanSeeIncorrectAnswers());
                                case FormQuizSettingsUpdateReqDto.Fields.respondersCanSeeCorrectAnswers ->
                                        formToUpdateQuizSettings.setRespondersCanSeeCorrectAnswers(dtoQuizSettings.getRespondersCanSeeCorrectAnswers());
                                case FormQuizSettingsUpdateReqDto.Fields.respondersCanSeePointsReceived ->
                                        formToUpdateQuizSettings.setRespondersCanSeePointsReceived(dtoQuizSettings.getRespondersCanSeePointsReceived());
                                case FormQuizSettingsUpdateReqDto.Fields.questionPointValue ->
                                        formToUpdateQuizSettings.setQuestionPointValue(dtoQuizSettings.getQuestionPointValue());
                            }
                        });

                case FormUpdateReqDto.Fields.responseSettingsUpdateReq ->
                        dtoResponseSettings.getUpdateFields().forEach(settingsField -> {
                            switch (settingsField) {
                                case FormResponseSettingsUpdateReqDto.Fields.emailCollectionOption ->
                                        formToUpdateResponseSettings.setEmailCollectionOption(dtoResponseSettings.getEmailCollectionOption());
                                case FormResponseSettingsUpdateReqDto.Fields.responseCopySendOption ->
                                        formToUpdateResponseSettings.setResponseCopySendOption(dtoResponseSettings.getResponseCopySendOption());
                                case FormResponseSettingsUpdateReqDto.Fields.isResponseEditable ->
                                        formToUpdateResponseSettings.setIsResponseEditable(dtoResponseSettings.getIsResponseEditable());
                                case FormResponseSettingsUpdateReqDto.Fields.singleResponseOnly ->
                                        formToUpdateResponseSettings.setSingleResponseOnly(dtoResponseSettings.getSingleResponseOnly());
                            }
                        });
            }
        });

        var savedForm = formRepo.save(formToUpdate);

        var formInfo = toFormInfo(savedForm);

        updateRecentForms(userId, formInfo);
        updateFormDetails(formInfo);
        putFormInfo(formInfo);

        return formInfo;
    }

    @Override
    @Transactional
    public FormDetailsDto getForm(UUID formId, UUID userId) {

        var currTime = Instant.now();

        formRepo.updateLastOpenedOn(formId, currTime);

        var recentFormsCacheKey = CacheUtil.buildKey(FormCacheNames.RECENT_FORMS, userId);
        var formSummariesCached = (FormSummariesDto) redisTemplate.opsForValue().get(recentFormsCacheKey);

        if (formSummariesCached != null) {

            formSummariesCached.getForms().forEach(f -> {
                if (f.getId().equals(formId)) {
                    f.setLastOpenedOn(currTime);
                }
            });
            formSummariesCached.getForms().sort(Comparator.comparing(FormSummaryDto::getLastOpenedOn, Comparator.reverseOrder()));

            redisTemplate.opsForValue().set(recentFormsCacheKey, formSummariesCached, Duration.ofMinutes(appConfiguration.getCacheDefaultTtlMinutes()));
        }

        var formDetailsCacheKey = CacheUtil.buildKey(FormCacheNames.FORM_DETAILS, formId);
        var formDetailsCached = (FormDetailsDto) redisTemplate.opsForValue().get(formDetailsCacheKey);

        if (formDetailsCached != null) {

            formDetailsCached.getFormInfo().setLastOpenedOn(currTime);

            redisTemplate.opsForValue().set(formDetailsCacheKey, formDetailsCached, Duration.ofMinutes(appConfiguration.getCacheDefaultTtlMinutes()));
        }

        var formInfoCacheKey = CacheUtil.buildKey(FormCacheNames.FORM_INFO, formId);
        var prevFormInfoCached = (FormInfoDto) redisTemplate.opsForValue().get(formInfoCacheKey);

        if (prevFormInfoCached != null) {

            prevFormInfoCached.setLastOpenedOn(currTime);

            redisTemplate.opsForValue().set(formInfoCacheKey, prevFormInfoCached, Duration.ofMinutes(appConfiguration.getCacheDefaultTtlMinutes()));
        }

        return formServiceCached.getFormDetails(formId);
    }

    @Override
    public FormDetailsDto viewForm(UUID formId, UUID userId) {
        var formDetails = formServiceCached.getFormDetails(formId);

        if (!formDetails.getFormInfo().getGeneralSettings().getPublished()) {
            throw new FormNotAcceptingResponseException(
                    new ViewFormErrorResDto(
                            null,
                            ViewFormErrorReason.NOT_PUBLISHED,
                            "We're sorry. This document is not published."
                    )
            );
        }

        boolean isAcceptingDateExceeded =
                formDetails.getFormInfo().getGeneralSettings().getStopAcceptingResponseOn() != null &&
                        Instant.now().isAfter(formDetails.getFormInfo().getGeneralSettings().getStopAcceptingResponseOn());

        var formResponseCountRedisKey = CommonCacheNames.FORM_RESPONSE_COUNT + "::" + formDetails.getFormInfo().getId();

        String formResponseCountString = stringRedisTemplate.opsForValue().get(formResponseCountRedisKey);
        Long formResponseCount;

        if (formResponseCountString == null) {
            formResponseCount = formResponseServiceFeignClient.getFormResponseCount(formDetails.getFormInfo().getId()).getCount();

            stringRedisTemplate.opsForValue().setIfAbsent(
                    formResponseCountRedisKey,
                    formResponseCount.toString(),
                    Duration.ofMinutes(appConfiguration.getCacheDefaultTtlMinutes())
            );
        } else {
            formResponseCount = Long.valueOf(formResponseCountString);
        }

        boolean isNumberOfResponseExceeded = formDetails.getFormInfo().getGeneralSettings().getResponseLimit() != null &&
                formResponseCount >= formDetails.getFormInfo().getGeneralSettings().getResponseLimit();

        if (!formDetails.getFormInfo().getGeneralSettings().getAcceptingResponse() || isAcceptingDateExceeded || isNumberOfResponseExceeded) {
            throw new FormNotAcceptingResponseException(
                    new ViewFormErrorResDto(
                            formDetails.getFormInfo().getTitle(),
                            ViewFormErrorReason.NOT_ACCEPTING_RESPONSE,
                            formDetails.getFormInfo().getGeneralSettings().getNotAcceptingResponseMessage()
                    )
            );
        }

        if (formDataServiceFeignClient.getIsResponseAlreadySubmitted(formId, userId)) {
            throw new FormResponseAlreadySubmittedException(
                    new ViewFormErrorResDto(
                            formDetails.getFormInfo().getTitle(),
                            ViewFormErrorReason.RESPONSE_ALREADY_SUBMITTED,
                            "You have already submitted a response for this form."
                    )
            );
        }

        return formDetails;
    }

    @Override
    public FormSummariesDto getFormsSummaries(UUID userId) {
        var forms = formRepo.findByUserIdOrderByLastOpenedOnDesc(userId)
                .stream()
                .map(f ->
                        new FormSummaryDto(f.getId(), f.getName(), f.getLastOpenedOn())
                ).toList();

        return new FormSummariesDto(forms);
    }

    @Override
    public Form getFormById(UUID id) {
        return formRepo.findById(id).orElseThrow(() -> new FormNotFoundException(id));
    }

    @Override
    public FormInfoDto getFormInfo(UUID formId) {
        Form f = getFormById(formId);

        return toFormInfo(f);
    }

    @Override
    @Transactional
    public SuccessMessageDto deleteForm(UUID formId, UUID userId) {
        formRepo.deleteById(formId);

        deleteFormFromRecentForms(userId, formId);
        deleteFormDetailsCache(formId);
        deleteFormInfoCache(formId);
        deleteFormQuestionCache(formId);

        redisTemplate.convertAndSend(
                MessagingChannelNames.FORM_DELETED,
                new FormDeletedMessage(formId)
        );

        return new SuccessMessageDto("Form deleted successfully with ID: " + formId);
    }

    @Override
    @Transactional
    public Form createFromTemplate(TemplateDetails template, UUID userId) {
        var newForm = new Form();

        var newFormDefaultSettings = new FormDefaultSettings();
        var newFormGeneralSettings = new FormGeneralSettings();
        var newFormPresentationSettings = new FormPresentationSettings();
        var newFormQuizSettings = new FormQuizSettings();
        var newFormResponseSettings = new FormResponseSettings();

        newFormDefaultSettings.setForm(newForm);
        newFormGeneralSettings.setForm(newForm);
        newFormPresentationSettings.setForm(newForm);
        newFormQuizSettings.setForm(newForm);
        newFormResponseSettings.setForm(newForm);

        newForm.setUserId(userId);
        newForm.setName(template.getName());
        newForm.setTitle(template.getTitle());
        newForm.setDescription(template.getDescription());
        newForm.setLastOpenedOn(Instant.now());

        newForm.setFormDefaultSettings(newFormDefaultSettings);
        newForm.setFormGeneralSettings(newFormGeneralSettings);
        newForm.setFormPresentationSettings(newFormPresentationSettings);
        newForm.setFormQuizSettings(newFormQuizSettings);
        newForm.setFormResponseSettings(newFormResponseSettings);

        var savedForm = formRepo.save(newForm);

        var questions = new ArrayList<QuestionDetailsDto>();

        template.getQuestionTemplates().forEach(qt -> {
            var questionManager = questionManagerFactory.get(qt.getQuestionType());
            var questionDetails = questionManager.createFromTemplate(qt, savedForm);

            questions.add(questionDetails);
        });

        var formDetails = new FormDetailsDto(toFormInfo(savedForm), questions);

        addFirstInRecentForms(userId, toFormInfo(savedForm));
        putFormDetails(formDetails);

        return savedForm;
    }

    private FormInfoDto toFormInfo(Form form) {
        var defaultSettings = form.getFormDefaultSettings();
        var generalSettings = form.getFormGeneralSettings();
        var presentationSettings = form.getFormPresentationSettings();
        var quizSettings = form.getFormQuizSettings();
        var responseSettings = form.getFormResponseSettings();

        return new FormInfoDto(
                form.getId(),
                form.getName(),
                form.getTitle(),
                form.getDescription(),
                form.getLastOpenedOn(),
                new FormDefaultSettingsDto(
                        defaultSettings.getDefaultEmailCollectionOption(),
                        defaultSettings.getMakeQuestionsRequiredByDefault()
                ),
                new FormGeneralSettingsDto(
                        generalSettings.getPublished(),
                        generalSettings.getAcceptingResponse(),
                        generalSettings.getNotAcceptingResponseMessage(),
                        generalSettings.getStopAcceptingResponseOn(),
                        generalSettings.getResponseLimit()
                ),
                new FormPresentationSettingsDto(
                        presentationSettings.getShowProgressBar(),
                        presentationSettings.getShuffleQuestionOrder(),
                        presentationSettings.getResponseSavedConfirmationMessage(),
                        presentationSettings.getShowLinkToSubmitAnotherResponse(),
                        presentationSettings.getShareResponseSummaryWithResponders(),
                        presentationSettings.getDisableAutoSaveForAllRespondents()
                ),
                new FormQuizSettingsDto(
                        quizSettings.getIsQuiz(),
                        quizSettings.getReleaseMarksOption(),
                        quizSettings.getRespondersCanSeeIncorrectAnswers(),
                        quizSettings.getRespondersCanSeeCorrectAnswers(),
                        quizSettings.getRespondersCanSeePointsReceived(),
                        quizSettings.getQuestionPointValue()
                ),
                new FormResponseSettingsDto(
                        responseSettings.getEmailCollectionOption(),
                        responseSettings.getResponseCopySendOption(),
                        responseSettings.getIsResponseEditable(),
                        responseSettings.getSingleResponseOnly()
                )
        );
    }

    private void addFirstInRecentForms(UUID userId, FormInfoDto formInfo) {
        var cacheKey = CacheUtil.buildKey(FormCacheNames.RECENT_FORMS, userId);
        var formSummaries = (FormSummariesDto) redisTemplate.opsForValue().get(cacheKey);

        if (formSummaries != null) {

            formSummaries.getForms().addFirst(new FormSummaryDto(formInfo.getId(), formInfo.getName(), formInfo.getLastOpenedOn()));

            redisTemplate.opsForValue().set(cacheKey, formSummaries, Duration.ofMinutes(appConfiguration.getCacheDefaultTtlMinutes()));
        }
    }

    private void deleteFormFromRecentForms(UUID userId, UUID formId) {
        var cacheKey = CacheUtil.buildKey(FormCacheNames.RECENT_FORMS, userId);
        var formSummaries = (FormSummariesDto) redisTemplate.opsForValue().get(cacheKey);

        if (formSummaries != null) {

            formSummaries.getForms().removeIf(f -> f.getId().equals(formId));

            redisTemplate.opsForValue().set(cacheKey, formSummaries, Duration.ofMinutes(appConfiguration.getCacheDefaultTtlMinutes()));
        }
    }

    private void deleteFormDetailsCache(UUID formId) {
        var cacheKey = CacheUtil.buildKey(FormCacheNames.FORM_DETAILS, formId);

        redisTemplate.delete(cacheKey);
    }

    private void deleteFormInfoCache(UUID formId) {
        var cacheKey = CacheUtil.buildKey(FormCacheNames.FORM_INFO, formId);

        redisTemplate.delete(cacheKey);
    }

    private void deleteFormQuestionCache(UUID formId) {
        var questionDetailsKeyPattern = CacheUtil.buildKey(QuestionCacheNames.QUESTION_DETAILS, "formId=" + formId, "*");
        var questionSummaryKeyPattern = CacheUtil.buildKey(QuestionCacheNames.QUESTION_SUMMARY, "formId=" + formId, "*");
        var questionSummariesKey = CacheUtil.buildKey(QuestionCacheNames.QUESTION_SUMMARIES, formId);

        var rKeys = redissonClient.getKeys();

        rKeys.delete(questionSummariesKey);
        rKeys.deleteByPattern(questionDetailsKeyPattern);
        rKeys.deleteByPattern(questionSummaryKeyPattern);
    }

    private void updateRecentForms(UUID userId, FormInfoDto formInfo) {
        var cacheKey = CacheUtil.buildKey(FormCacheNames.RECENT_FORMS, userId);
        var formSummaries = (FormSummariesDto) redisTemplate.opsForValue().get(cacheKey);

        if (formSummaries != null) {

            formSummaries.getForms().forEach(f -> {
                if (f.getId().equals(formInfo.getId())) {
                    f.setName(formInfo.getName());
                }
            });

            redisTemplate.opsForValue().set(cacheKey, formSummaries, Duration.ofMinutes(appConfiguration.getCacheDefaultTtlMinutes()));
        }
    }

    private void updateFormDetails(FormInfoDto formInfo) {
        var cacheKey = CacheUtil.buildKey(FormCacheNames.FORM_DETAILS, formInfo.getId());
        var formDetails = (FormDetailsDto) redisTemplate.opsForValue().get(cacheKey);

        if (formDetails != null) {
            formDetails.setFormInfo(formInfo);

            redisTemplate.opsForValue().set(cacheKey, formDetails, Duration.ofMinutes(appConfiguration.getCacheDefaultTtlMinutes()));
        }
    }

    private void putFormInfo(FormInfoDto formInfo) {
        var cacheKey = CacheUtil.buildKey(FormCacheNames.FORM_INFO, formInfo.getId());

        redisTemplate.opsForValue().set(cacheKey, formInfo, Duration.ofMinutes(appConfiguration.getCacheDefaultTtlMinutes()));
    }

    private void putFormDetails(FormDetailsDto formDetails) {
        var cacheKey = CacheUtil.buildKey(FormCacheNames.FORM_DETAILS, formDetails.getFormInfo().getId());

        redisTemplate.opsForValue().set(cacheKey, formDetails, Duration.ofMinutes(appConfiguration.getCacheDefaultTtlMinutes()));
    }

}
