package com.sougata.form_service.service.formSchema.impl;

import com.sougata.form_engine.constant.cache.CommonCacheNames;
import com.sougata.form_engine.constant.cache.FormCacheNames;
import com.sougata.form_engine.constant.cache.QuestionCacheNames;
import com.sougata.form_engine.constant.messaging.MessagingChannelNames;
import com.sougata.form_engine.dto.form.FormDetailsDto;
import com.sougata.form_engine.dto.messaging.QuestionCreatedMessage;
import com.sougata.form_engine.dto.messaging.QuestionDeleteMessage;
import com.sougata.form_engine.dto.messaging.QuestionUpdatedMessage;
import com.sougata.form_engine.dto.others.SuccessMessageDto;
import com.sougata.form_engine.dto.question.details.MultipleQuestionDetailsDto;
import com.sougata.form_engine.dto.question.details.QuestionDetailsDto;
import com.sougata.form_engine.dto.question.schemaaddrequest.QuestionAddReqDto;
import com.sougata.form_engine.dto.question.schemaupdatereq.MultipleQuestionUpdateReqDto;
import com.sougata.form_engine.dto.question.schemaupdatereq.QuestionOrderUpdateReqDto;
import com.sougata.form_engine.dto.question.summary.QuestionSummariesDto;
import com.sougata.form_engine.dto.question.summary.QuestionSummaryDto;
import com.sougata.form_service.configuration.AppConfiguration;
import com.sougata.form_service.exception.QuestionNotFoundException;
import com.sougata.form_service.projection.QuestionIdAndOrderIndexProjection;
import com.sougata.form_service.projection.QuestionSummaryProjection;
import com.sougata.form_service.repository.formSchema.AnyTypeQuestionRepositoryFactory;
import com.sougata.form_service.repository.formSchema.QuestionRepository;
import com.sougata.form_service.service.formSchema.QuestionService;
import com.sougata.form_service.service.formSchema.questionManager.QuestionManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
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
    private final AnyTypeQuestionRepositoryFactory anyTypeQuestionRepositoryFactory;
    private final QuestionRepository questionRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final AppConfiguration appConfiguration;

    @Override
    public QuestionDetailsDto createQuestion(UUID formId, QuestionAddReqDto dto) {
        var questionManager = questionManagerFactory.get(dto.getQuestionType());
        var questionDetails = questionManager.create(formId, dto);

        addQuestionInQuestionSummaries(formId, questionDetails);
        addQuestionInFormDetails(formId, questionDetails);
        putQuestionDetails(questionDetails);
        putQuestionSummary(new QuestionSummaryDto(questionDetails.getId(), questionDetails.getQuestion(), questionDetails.getQuestionType(), questionDetails.getOrderIndex()));

        redisTemplate.convertAndSend(MessagingChannelNames.QUESTION_CREATED, new QuestionCreatedMessage<>(formId, questionDetails));

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

                redisTemplate.convertAndSend(MessagingChannelNames.QUESTION_DELETED, new QuestionDeleteMessage(formId, questionId));

                updatedQuestionDetails = newManager.create(formId, questionId, question.getAddReqForQuestionTypeUpdate());
            }

            updateQuestionInFormDetails(formId, updatedQuestionDetails);
            updateQuestionInQuestionSummaries(formId, updatedQuestionDetails);
            putQuestionDetails(updatedQuestionDetails);
            putQuestionSummary(new QuestionSummaryDto(updatedQuestionDetails.getId(), updatedQuestionDetails.getQuestion(), updatedQuestionDetails.getQuestionType(), updatedQuestionDetails.getOrderIndex()));

            questionDetailsList.add(updatedQuestionDetails);

            redisTemplate.convertAndSend(MessagingChannelNames.QUESTION_UPDATED, new QuestionUpdatedMessage<>(formId, updatedQuestionDetails, question.getUpdateFields()));

        });

        return new MultipleQuestionDetailsDto(questionDetailsList);
    }

    @Override
    @Transactional
    public SuccessMessageDto deleteQuestion(UUID formId, Long questionId) {

        var question = questionRepository.findQuestionSummaryById(questionId)
                .orElseThrow(() -> new QuestionNotFoundException(questionId));

        questionRepository.deleteQuestion(questionId);

        questionRepository.setQuestionOrderAfterDeleteQuestion(formId, question.getOrderIndex());

        deleteQuestionInFormDetails(formId, questionId);
        deleteQuestionInQuestionSummaries(formId, questionId);

        redisTemplate.convertAndSend(MessagingChannelNames.QUESTION_DELETED, new QuestionDeleteMessage(formId, questionId));

        return SuccessMessageDto.create("Question deleted successfully with question ID: " + questionId);
    }

    @Override
    @Transactional(readOnly = true)
    public QuestionDetailsDto getQuestion(UUID formId, Long questionId) {

        var qType = questionRepository.findQuestionTypeById(questionId)
                .orElseThrow(() -> new QuestionNotFoundException(questionId))
                .getQuestionType();

        var manager = questionManagerFactory.get(qType);

        return manager.get(formId, questionId);
    }

    @Override
    public QuestionSummariesDto getQuestionSummaries(UUID formId) {
        var questionProjections = new ArrayList<>(
                questionRepository.findQuestionSummariesByFormId(formId)
        );

        questionProjections.sort(Comparator.comparingInt(QuestionSummaryProjection::getOrderIndex));

        var questions = questionProjections
                .stream()
                .map(q ->
                        new QuestionSummaryDto(q.getId(), q.getQuestion(), q.getQuestionType(), q.getOrderIndex())
                ).toList();

        return new QuestionSummariesDto(questions);
    }

    @Override
    @Transactional(readOnly = true)
    public QuestionSummaryDto getQuestionSummary(UUID formId, Long questionId) {

        var q = questionRepository.findQuestionSummaryById(questionId)
                .orElseThrow(() -> new QuestionNotFoundException(questionId));

        return new QuestionSummaryDto(q.getId(), q.getQuestion(), q.getQuestionType(), q.getOrderIndex());
    }

    @Override
    @Transactional
    public SuccessMessageDto updateOrderIndex(UUID formId, Long questionId, QuestionOrderUpdateReqDto req) {

        var question = questionRepository.findById(questionId)
                .orElseThrow(() -> new QuestionNotFoundException(questionId));

        var prevIndex = question.getOrderIndex();

        if (!Objects.equals(req.getCurrentIndex(), prevIndex)) {
            question.setOrderIndex(req.getCurrentIndex());

            questionRepository.save(question);
            questionRepository.updateNextQuestionOrderIndexes(formId, questionId, prevIndex, req.getCurrentIndex());

            var formDetailsCacheKey = CommonCacheNames.FORM_SERVICE_PREFIX + CommonCacheNames.SEPARATOR + FormCacheNames.FORM_DETAILS + CommonCacheNames.SEPARATOR + formId;
            var questionSummariesCacheKey = CommonCacheNames.FORM_SERVICE_PREFIX + CommonCacheNames.SEPARATOR + QuestionCacheNames.QUESTION_SUMMARIES + CommonCacheNames.SEPARATOR + formId;

            if (redisTemplate.hasKey(formDetailsCacheKey) || redisTemplate.hasKey(questionSummariesCacheKey)) {
                var idOrderIndexMap = questionRepository.findAllIdAndOrderIndexByFormId(formId)
                        .stream()
                        .collect(Collectors.toMap(
                                QuestionIdAndOrderIndexProjection::getId,
                                Function.identity()
                        ));

                var formDetailsCached = (FormDetailsDto) redisTemplate.opsForValue().get(formDetailsCacheKey);

                if (formDetailsCached != null) {
                    formDetailsCached.getQuestions().forEach(q -> {
                        q.setOrderIndex(idOrderIndexMap.get(q.getId()).getOrderIndex());
                    });

                    formDetailsCached.getQuestions().sort(Comparator.comparingInt(QuestionDetailsDto::getOrderIndex));

                    redisTemplate.opsForValue().set(formDetailsCacheKey, formDetailsCached, Duration.ofMinutes(appConfiguration.getCacheDefaultTtlMinutes()));
                }

                var questionSummariesCached = (QuestionSummariesDto) redisTemplate.opsForValue().get(questionSummariesCacheKey);

                if (questionSummariesCached != null) {
                    questionSummariesCached.getQuestions().forEach(q -> {
                        q.setOrderIndex(idOrderIndexMap.get(q.getId()).getOrderIndex());
                    });

                    questionSummariesCached.getQuestions().sort(Comparator.comparingInt(QuestionSummaryDto::getOrderIndex));

                    redisTemplate.opsForValue().set(questionSummariesCacheKey, questionSummariesCached, Duration.ofMinutes(appConfiguration.getCacheDefaultTtlMinutes()));
                }
            }

            var questionDetailsCacheKey = CommonCacheNames.FORM_SERVICE_PREFIX + CommonCacheNames.SEPARATOR + QuestionCacheNames.QUESTION_DETAILS + CommonCacheNames.SEPARATOR + questionId;

            if (redisTemplate.hasKey(questionDetailsCacheKey)) {
                var questionDetailsCached = (QuestionDetailsDto) redisTemplate.opsForValue().get(questionDetailsCacheKey);

                questionDetailsCached.setOrderIndex(req.getCurrentIndex());

                redisTemplate.opsForValue().set(questionDetailsCacheKey, questionDetailsCached, Duration.ofMinutes(appConfiguration.getCacheDefaultTtlMinutes()));
            }

            var questionSummaryCacheKey = CommonCacheNames.FORM_SERVICE_PREFIX + CommonCacheNames.SEPARATOR + QuestionCacheNames.QUESTION_SUMMARY + CommonCacheNames.SEPARATOR + questionId;

            if (redisTemplate.hasKey(questionSummaryCacheKey)) {
                var questionSummaryCached = (QuestionSummaryDto) redisTemplate.opsForValue().get(questionDetailsCacheKey);

                questionSummaryCached.setOrderIndex(req.getCurrentIndex());

                redisTemplate.opsForValue().set(questionSummaryCacheKey, questionSummaryCached, Duration.ofMinutes(appConfiguration.getCacheDefaultTtlMinutes()));
            }

        }

        return SuccessMessageDto.create(
                "Question order updated successfully previous order: " + prevIndex + ". current index : " + req.getCurrentIndex()
        );
    }

    private void addQuestionInQuestionSummaries(UUID formId, QuestionDetailsDto question) {

        var cacheKey = CommonCacheNames.FORM_SERVICE_PREFIX + CommonCacheNames.SEPARATOR + QuestionCacheNames.QUESTION_SUMMARIES + CommonCacheNames.SEPARATOR + formId;

        if (redisTemplate.hasKey(cacheKey)) {
            var prevSummaries = (QuestionSummariesDto) redisTemplate.opsForValue().get(cacheKey);

            var newSummary = new QuestionSummaryDto(
                    question.getId(),
                    question.getQuestion(),
                    question.getQuestionType(),
                    question.getOrderIndex()
            );

            prevSummaries.getQuestions().add(newSummary);
            prevSummaries.getQuestions().sort(Comparator.comparingInt(QuestionSummaryDto::getOrderIndex));

            redisTemplate.opsForValue().set(cacheKey, prevSummaries, Duration.ofMinutes(appConfiguration.getCacheDefaultTtlMinutes()));
        }
    }

    private void deleteQuestionInQuestionSummaries(UUID formId, Long questionId) {

        var cacheKey = CommonCacheNames.FORM_SERVICE_PREFIX + CommonCacheNames.SEPARATOR + QuestionCacheNames.QUESTION_SUMMARIES + CommonCacheNames.SEPARATOR + formId;

        if (redisTemplate.hasKey(cacheKey)) {
            var prevSummaries = (QuestionSummariesDto) redisTemplate.opsForValue().get(cacheKey);

            prevSummaries.getQuestions().removeIf(q -> q.getId().equals(questionId));
            prevSummaries.getQuestions().sort(Comparator.comparingInt(QuestionSummaryDto::getOrderIndex));

            redisTemplate.opsForValue().set(cacheKey, prevSummaries, Duration.ofMinutes(appConfiguration.getCacheDefaultTtlMinutes()));
        }
    }

    private void updateQuestionInQuestionSummaries(UUID formId, QuestionDetailsDto question) {
        var cacheKey = CommonCacheNames.FORM_SERVICE_PREFIX + CommonCacheNames.SEPARATOR + QuestionCacheNames.QUESTION_SUMMARIES + CommonCacheNames.SEPARATOR + formId;

        if (redisTemplate.hasKey(cacheKey)) {
            var prevSummaries = (QuestionSummariesDto) redisTemplate.opsForValue().get(cacheKey);

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

        if (redisTemplate.hasKey(cacheKey)) {
            var formDetails = (FormDetailsDto) redisTemplate.opsForValue().get(cacheKey);

            formDetails.getQuestions().add(question);
            formDetails.getQuestions().sort(Comparator.comparingInt(QuestionDetailsDto::getOrderIndex));

            redisTemplate.opsForValue().set(cacheKey, formDetails, Duration.ofMinutes(appConfiguration.getCacheDefaultTtlMinutes()));
        }
    }

    private void updateQuestionInFormDetails(UUID formId, QuestionDetailsDto question) {
        var cacheKey = CommonCacheNames.FORM_SERVICE_PREFIX + CommonCacheNames.SEPARATOR + FormCacheNames.FORM_DETAILS + CommonCacheNames.SEPARATOR + formId;

        if (redisTemplate.hasKey(cacheKey)) {
            var formDetails = (FormDetailsDto) redisTemplate.opsForValue().get(cacheKey);

            formDetails.getQuestions().forEach(q -> {
                if (q.getId().equals(question.getId())) {
                    BeanUtils.copyProperties(question, q);
                }
            });

            redisTemplate.opsForValue().set(cacheKey, formDetails, Duration.ofMinutes(appConfiguration.getCacheDefaultTtlMinutes()));
        }
    }

    private void deleteQuestionInFormDetails(UUID formId, Long questionId) {
        var cacheKey = CommonCacheNames.FORM_SERVICE_PREFIX + CommonCacheNames.SEPARATOR + FormCacheNames.FORM_DETAILS + CommonCacheNames.SEPARATOR + formId;

        if (redisTemplate.hasKey(cacheKey)) {
            var formDetails = (FormDetailsDto) redisTemplate.opsForValue().get(cacheKey);

            formDetails.getQuestions().removeIf(q -> q.getId().equals(questionId));

            redisTemplate.opsForValue().set(cacheKey, formDetails, Duration.ofMinutes(appConfiguration.getCacheDefaultTtlMinutes()));
        }
    }

    private void putQuestionDetails(QuestionDetailsDto question) {
        var cacheKey = CommonCacheNames.FORM_SERVICE_PREFIX + CommonCacheNames.SEPARATOR + QuestionCacheNames.QUESTION_DETAILS + CommonCacheNames.SEPARATOR + question.getId();

        redisTemplate.opsForValue().set(cacheKey, question, Duration.ofMinutes(appConfiguration.getCacheDefaultTtlMinutes()));
    }

    private void putQuestionSummary(QuestionSummaryDto question) {
        var cacheKey = CommonCacheNames.FORM_SERVICE_PREFIX + CommonCacheNames.SEPARATOR + QuestionCacheNames.QUESTION_SUMMARY + CommonCacheNames.SEPARATOR + question.getId();

        redisTemplate.opsForValue().set(cacheKey, question, Duration.ofMinutes(appConfiguration.getCacheDefaultTtlMinutes()));
    }


}
