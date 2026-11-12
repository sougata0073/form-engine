package com.sougata.form_service.service.formSchema.impl;

import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.constant.cache.CommonCacheNames;
import com.sougata.form_engine.constant.cache.FormCacheNames;
import com.sougata.form_engine.constant.cache.QuestionCacheNames;
import com.sougata.form_engine.constant.messaging.MessagingChannelNames;
import com.sougata.form_engine.dto.form.FormDetailsDto;
import com.sougata.form_engine.dto.messaging.QuestionDeleteMessage;
import com.sougata.form_engine.dto.others.SuccessMessageDto;
import com.sougata.form_engine.dto.question.details.MultipleQuestionDetailsDto;
import com.sougata.form_engine.dto.question.details.QuestionDetailsDto;
import com.sougata.form_engine.dto.question.schemaaddrequest.QuestionAddReqDto;
import com.sougata.form_engine.dto.question.schemaupdatereq.MultipleQuestionUpdateReqDto;
import com.sougata.form_engine.dto.question.schemaupdatereq.QuestionOrderUpdateReqDto;
import com.sougata.form_engine.dto.question.schemaupdatereq.QuestionUpdateReqDto;
import com.sougata.form_engine.dto.question.summary.QuestionSummariesDto;
import com.sougata.form_engine.dto.question.summary.QuestionSummaryDto;
import com.sougata.form_service.configuration.AppConfiguration;
import com.sougata.form_service.exception.QuestionNotFoundException;
import com.sougata.form_service.model.formSchema.Question;
import com.sougata.form_service.projection.QuestionIdAndOrderIndexProjection;
import com.sougata.form_service.repository.formSchema.AnyTypeQuestionRepositoryFactory;
import com.sougata.form_service.repository.formSchema.FormGeneralSettingsRepository;
import com.sougata.form_service.repository.formSchema.QuestionRepository;
import com.sougata.form_service.service.formSchema.QuestionService;
import com.sougata.form_service.service.formSchema.QuestionServiceCached;
import com.sougata.form_service.service.formSchema.questionManager.QuestionManagerFactory;
import com.sougata.form_service.util.CacheUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionManagerFactory questionManagerFactory;
    private final QuestionRepository questionRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final AppConfiguration appConfiguration;
    private final FormGeneralSettingsRepository formGeneralSettingsRepository;
    private final QuestionServiceCached questionServiceCached;
    private final AnyTypeQuestionRepositoryFactory anyTypeQuestionRepositoryFactory;

    @Override
    @Transactional
    public QuestionDetailsDto createQuestion(UUID formId, UUID userId, QuestionAddReqDto dto) {
        var questionManager = questionManagerFactory.get(dto.getQuestionType());
        var questionDetails = questionManager.create(formId, userId, dto);

        if (questionDetails.getQuestionType() == QuestionType.FILE_UPLOAD) {
            formGeneralSettingsRepository.updateResponderNeedsToBeAuthenticated(formId, true);
        }

        var questionSummary = new QuestionSummaryDto(
                questionDetails.getId(),
                questionDetails.getQuestion(),
                questionDetails.getQuestionType(),
                questionDetails.getOrderIndex()
        );

        addQuestionInQuestionSummaries(formId, questionSummary);
        addQuestionInFormDetails(formId, questionDetails);
        putQuestionDetails(formId, questionDetails);
        putQuestionSummary(formId, questionSummary);

        return questionDetails;
    }

    @Override
    @Transactional
    public MultipleQuestionDetailsDto updateQuestions(UUID formId, MultipleQuestionUpdateReqDto dto) {

        var questionDetailsList = new ArrayList<QuestionDetailsDto>();

        dto.getQuestions().forEach(question -> {

            var questionId = Long.valueOf(question.getQuestionId());

            var prevQType = questionRepository.findQuestionTypeById(questionId)
                    .orElseThrow(() -> new QuestionNotFoundException(questionId))
                    .getQuestionType();

            QuestionDetailsDto updatedQuestionDetails;

            if (prevQType == question.getQuestionType()) {
                var manager = questionManagerFactory.get(prevQType);

                updatedQuestionDetails = manager.update(formId, questionId, question);

            } else {
                var prevManager = questionManagerFactory.get(prevQType);
                var newManager = questionManagerFactory.get(question.getQuestionType());

                prevManager.delete(questionId);

                updatedQuestionDetails = newManager.create(formId, questionId, question.getAddReqForQuestionTypeUpdate());

                redisTemplate.convertAndSend(
                        MessagingChannelNames.QUESTION_DELETED,
                        new QuestionDeleteMessage(formId, questionId, prevQType)
                );
            }

            updateQuestionInFormDetails(formId, updatedQuestionDetails);
            updateQuestionInQuestionSummaries(formId, updatedQuestionDetails);
            putQuestionDetails(formId, updatedQuestionDetails);
            putQuestionSummary(formId, new QuestionSummaryDto(
                    updatedQuestionDetails.getId(),
                    updatedQuestionDetails.getQuestion(),
                    updatedQuestionDetails.getQuestionType(),
                    updatedQuestionDetails.getOrderIndex())
            );

            questionDetailsList.add(updatedQuestionDetails);
        });

        if (dto.getQuestions()
                .stream()
                .anyMatch(q -> q.getUpdateFields().contains(QuestionUpdateReqDto.Fields.questionType))
        ) {
            formGeneralSettingsRepository.updateResponderNeedsToBeAuthenticated(
                    formId,
                    questionRepository.existsByForm_IdAndQuestionType(formId, QuestionType.FILE_UPLOAD)
            );
        }

        return new MultipleQuestionDetailsDto(questionDetailsList);
    }

    @Override
    @Transactional
    public QuestionDetailsDto copyQuestion(UUID formId, Long questionId) {
        var parentQuestion = questionRepository.findById(questionId)
                .orElseThrow(() -> new QuestionNotFoundException(questionId));

        var parentQuestionCopy = parentQuestion.copy();

        var repo = anyTypeQuestionRepositoryFactory.get(parentQuestion.getQuestionType());
        var manager = questionManagerFactory.get(parentQuestion.getQuestionType());

        var childQuestion = repo.findById(questionId)
                .orElseThrow(() -> new QuestionNotFoundException(parentQuestion.getQuestionType(), questionId));

        var childQuestionCopy = childQuestion.copy();

        questionRepository.setQuestionOrderDuringCopyQuestion(formId, parentQuestion.getOrderIndex());

        parentQuestionCopy.setOrderIndex(parentQuestion.getOrderIndex() + 1);

        var savedParentQuestionCopy = questionRepository.save(parentQuestionCopy);

        childQuestionCopy.setQuestion(savedParentQuestionCopy);

        var savedChildQuestionCopy = repo.save(childQuestionCopy);

        var questionDetails = manager.toQuestionDetailsDto(savedChildQuestionCopy, savedParentQuestionCopy);

        var questionSummary = new QuestionSummaryDto(
                questionDetails.getId(),
                questionDetails.getQuestion(),
                questionDetails.getQuestionType(),
                questionDetails.getOrderIndex()
        );

        addQuestionInQuestionSummaries(formId, questionSummary);
        addQuestionInFormDetails(formId, questionDetails);
        putQuestionDetails(formId, questionDetails);
        putQuestionSummary(formId, questionSummary);

        adjustQuestionOrderIndexInCache(formId, null);

        return questionDetails;
    }

    @Override
    @Transactional
    public SuccessMessageDto deleteQuestion(UUID formId, Long questionId) {

        var question = questionServiceCached.getQuestionSummary(formId, questionId);

        questionRepository.deleteQuestion(questionId);

        questionRepository.setQuestionOrderAfterDeleteQuestion(formId, question.getOrderIndex());

        if (question.getQuestionType() == QuestionType.FILE_UPLOAD) {
            formGeneralSettingsRepository.updateResponderNeedsToBeAuthenticated(
                    formId,
                    questionRepository.existsByForm_IdAndQuestionType(formId, QuestionType.FILE_UPLOAD)
            );
        }

        deleteQuestionInFormDetails(formId, questionId);
        deleteQuestionInQuestionSummaries(formId, questionId);

        redisTemplate.convertAndSend(
                MessagingChannelNames.QUESTION_DELETED,
                new QuestionDeleteMessage(formId, questionId, question.getQuestionType())
        );

        return SuccessMessageDto.create("Question deleted successfully with question ID: " + questionId);
    }

    @Override
    @Transactional
    public SuccessMessageDto updateOrderIndex(UUID formId, Long questionId, QuestionOrderUpdateReqDto req) {

        var question = questionRepository.findById(questionId)
                .orElseThrow(() -> new QuestionNotFoundException(questionId));

        var prevIndex = question.getOrderIndex();

        if (!Objects.equals(req.getCurrentIndex(), prevIndex)) {
            question.setOrderIndex(req.getCurrentIndex());

            var updatedQuestion = questionRepository.save(question);
            questionRepository.updateNextQuestionOrderIndexes(formId, questionId, prevIndex, req.getCurrentIndex());

            adjustQuestionOrderIndexInCache(formId, updatedQuestion);
        }

        return SuccessMessageDto.create(
                "Question order updated successfully previous order: " + prevIndex + ". current index : " + req.getCurrentIndex()
        );
    }

    private void addQuestionInQuestionSummaries(UUID formId, QuestionSummaryDto questionSummary) {

        var cacheKey = CommonCacheNames.FORM_SERVICE_PREFIX + CommonCacheNames.SEPARATOR + QuestionCacheNames.QUESTION_SUMMARIES + CommonCacheNames.SEPARATOR + formId;

        var prevSummaries = (QuestionSummariesDto) redisTemplate.opsForValue().get(cacheKey);

        if (prevSummaries != null) {
            prevSummaries.getQuestions().add(questionSummary);
            prevSummaries.getQuestions().sort(Comparator.comparingInt(QuestionSummaryDto::getOrderIndex));

            redisTemplate.opsForValue().set(cacheKey, prevSummaries, Duration.ofMinutes(appConfiguration.getCacheDefaultTtlMinutes()));
        }
    }

    private void deleteQuestionInQuestionSummaries(UUID formId, Long questionId) {

        var cacheKey = CommonCacheNames.FORM_SERVICE_PREFIX + CommonCacheNames.SEPARATOR + QuestionCacheNames.QUESTION_SUMMARIES + CommonCacheNames.SEPARATOR + formId;

        var prevSummaries = (QuestionSummariesDto) redisTemplate.opsForValue().get(cacheKey);

        if (prevSummaries != null) {

            prevSummaries.getQuestions().removeIf(q -> q.getId().equals(questionId));
            prevSummaries.getQuestions().sort(Comparator.comparingInt(QuestionSummaryDto::getOrderIndex));

            redisTemplate.opsForValue().set(cacheKey, prevSummaries, Duration.ofMinutes(appConfiguration.getCacheDefaultTtlMinutes()));
        }
    }

    private void updateQuestionInQuestionSummaries(UUID formId, QuestionDetailsDto question) {
        var cacheKey = CommonCacheNames.FORM_SERVICE_PREFIX + CommonCacheNames.SEPARATOR + QuestionCacheNames.QUESTION_SUMMARIES + CommonCacheNames.SEPARATOR + formId;

        var prevSummaries = (QuestionSummariesDto) redisTemplate.opsForValue().get(cacheKey);

        if (prevSummaries != null) {

            prevSummaries.getQuestions().forEach(q -> {
                if (q.getId().equals(question.getId())) {
                    q.setQuestion(question.getQuestion());
                    q.setQuestionType(question.getQuestionType());
                    q.setOrderIndex(question.getOrderIndex());
                }
            });

            redisTemplate.opsForValue().set(cacheKey, prevSummaries, Duration.ofMinutes(appConfiguration.getCacheDefaultTtlMinutes()));
        }
    }

    private void addQuestionInFormDetails(UUID formId, QuestionDetailsDto question) {
        var cacheKey = CommonCacheNames.FORM_SERVICE_PREFIX + CommonCacheNames.SEPARATOR + FormCacheNames.FORM_DETAILS + CommonCacheNames.SEPARATOR + formId;

        var formDetails = (FormDetailsDto) redisTemplate.opsForValue().get(cacheKey);

        if (formDetails != null) {

            formDetails.getQuestions().add(question);
            formDetails.getQuestions().sort(Comparator.comparingInt(QuestionDetailsDto::getOrderIndex));

            redisTemplate.opsForValue().set(cacheKey, formDetails, Duration.ofMinutes(appConfiguration.getCacheDefaultTtlMinutes()));
        }
    }

    private void updateQuestionInFormDetails(UUID formId, QuestionDetailsDto updatedQuestion) {
        var cacheKey = CommonCacheNames.FORM_SERVICE_PREFIX + CommonCacheNames.SEPARATOR + FormCacheNames.FORM_DETAILS + CommonCacheNames.SEPARATOR + formId;

        var formDetails = (FormDetailsDto) redisTemplate.opsForValue().get(cacheKey);
        if (formDetails != null) {

            for (int i = 0; i < formDetails.getQuestions().size(); i++) {
                var q = formDetails.getQuestions().get(i);
                if (q.getId().equals(updatedQuestion.getId())) {
                    formDetails.getQuestions().set(i, updatedQuestion);
                    break;
                }
            }

            redisTemplate.opsForValue().set(cacheKey, formDetails, Duration.ofMinutes(appConfiguration.getCacheDefaultTtlMinutes()));
        }
    }

    private void deleteQuestionInFormDetails(UUID formId, Long questionId) {
        var cacheKey = CacheUtil.buildKey(FormCacheNames.FORM_DETAILS, formId);

        var formDetails = (FormDetailsDto) redisTemplate.opsForValue().get(cacheKey);

        if (formDetails != null) {

            formDetails.getQuestions().removeIf(q -> q.getId().equals(questionId));

            redisTemplate.opsForValue().set(cacheKey, formDetails, Duration.ofMinutes(appConfiguration.getCacheDefaultTtlMinutes()));
        }
    }

    private void putQuestionDetails(UUID formId, QuestionDetailsDto question) {
        var cacheKey = CacheUtil.buildKey(QuestionCacheNames.QUESTION_DETAILS, "formId=" + formId, "questionId=" + question.getId());

        redisTemplate.opsForValue().set(cacheKey, question, Duration.ofMinutes(appConfiguration.getCacheDefaultTtlMinutes()));
    }

    private void putQuestionSummary(UUID formId, QuestionSummaryDto question) {
        var cacheKey = CacheUtil.buildKey(QuestionCacheNames.QUESTION_SUMMARY, "formId=" + formId, "questionId=" + question.getId());

        redisTemplate.opsForValue().set(cacheKey, question, Duration.ofMinutes(appConfiguration.getCacheDefaultTtlMinutes()));
    }

    private void adjustQuestionOrderIndexInCache(UUID formId, Question updatedQuestion) {
        var formDetailsCacheKey = CacheUtil.buildKey(FormCacheNames.FORM_DETAILS, formId);
        var questionSummariesCacheKey = CacheUtil.buildKey(QuestionCacheNames.QUESTION_SUMMARIES, formId);

        var formDetailsCached = (FormDetailsDto) redisTemplate.opsForValue().get(formDetailsCacheKey);
        var questionSummariesCached = (QuestionSummariesDto) redisTemplate.opsForValue().get(questionSummariesCacheKey);

        if (formDetailsCached != null || questionSummariesCached != null) {
            var idOrderIndexMap = questionRepository.findAllIdAndOrderIndexByFormId(formId)
                    .stream()
                    .collect(Collectors.toMap(
                            QuestionIdAndOrderIndexProjection::getId,
                            Function.identity()
                    ));

            if (formDetailsCached != null) {
                formDetailsCached.getQuestions().forEach(q -> {
                    var idOrderIndex = idOrderIndexMap.get(q.getId());
                    if (idOrderIndex != null) {
                        q.setOrderIndex(idOrderIndex.getOrderIndex());
                    }
                });

                formDetailsCached.getQuestions().sort(Comparator.comparingInt(QuestionDetailsDto::getOrderIndex));

                redisTemplate.opsForValue().set(formDetailsCacheKey, formDetailsCached, Duration.ofMinutes(appConfiguration.getCacheDefaultTtlMinutes()));
            }

            if (questionSummariesCached != null) {
                questionSummariesCached.getQuestions().forEach(q -> {
                    var idOrderIndex = idOrderIndexMap.get(q.getId());
                    if (idOrderIndex != null) {
                        q.setOrderIndex(idOrderIndex.getOrderIndex());
                    }
                });

                questionSummariesCached.getQuestions().sort(Comparator.comparingInt(QuestionSummaryDto::getOrderIndex));

                redisTemplate.opsForValue().set(questionSummariesCacheKey, questionSummariesCached, Duration.ofMinutes(appConfiguration.getCacheDefaultTtlMinutes()));
            }
        }

        if (updatedQuestion == null) return;

        var questionDetailsCacheKey = CacheUtil.buildKey(QuestionCacheNames.QUESTION_DETAILS, "formId=" + formId, "questionId=" + updatedQuestion.getId());

        var questionDetailsCached = (QuestionDetailsDto) redisTemplate.opsForValue().get(questionDetailsCacheKey);

        if (questionDetailsCached != null) {
            questionDetailsCached.setOrderIndex(updatedQuestion.getOrderIndex());

            redisTemplate.opsForValue().set(questionDetailsCacheKey, questionDetailsCached, Duration.ofMinutes(appConfiguration.getCacheDefaultTtlMinutes()));
        }

        var questionSummaryCacheKey = CacheUtil.buildKey(QuestionCacheNames.QUESTION_SUMMARY, "formId=" + formId, "questionId=" + updatedQuestion.getId());

        var questionSummaryCached = (QuestionSummaryDto) redisTemplate.opsForValue().get(questionSummaryCacheKey);

        if (questionSummaryCached != null) {
            questionSummaryCached.setOrderIndex(updatedQuestion.getOrderIndex());
            redisTemplate.opsForValue().set(questionSummaryCacheKey, questionSummaryCached, Duration.ofMinutes(appConfiguration.getCacheDefaultTtlMinutes()));
        }
    }


}
