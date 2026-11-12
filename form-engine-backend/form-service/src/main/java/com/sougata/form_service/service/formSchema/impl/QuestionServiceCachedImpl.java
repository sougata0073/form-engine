package com.sougata.form_service.service.formSchema.impl;

import com.sougata.form_engine.constant.cache.QuestionCacheNames;
import com.sougata.form_engine.dto.question.details.QuestionDetailsDto;
import com.sougata.form_engine.dto.question.summary.QuestionSummariesDto;
import com.sougata.form_engine.dto.question.summary.QuestionSummaryDto;
import com.sougata.form_service.exception.QuestionNotFoundException;
import com.sougata.form_service.projection.QuestionSummaryProjection;
import com.sougata.form_service.repository.formSchema.QuestionRepository;
import com.sougata.form_service.service.formSchema.QuestionServiceCached;
import com.sougata.form_service.service.formSchema.questionManager.QuestionManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QuestionServiceCachedImpl implements QuestionServiceCached {

    private final QuestionManagerFactory questionManagerFactory;
    private final QuestionRepository questionRepository;

    @Override
    @Cacheable(
            cacheNames = {QuestionCacheNames.QUESTION_DETAILS},
            key = "'formId=' + #formId + '::questionId=' + #questionId"
    )
    @Transactional(readOnly = true)
    public QuestionDetailsDto getQuestionDetails(UUID formId, Long questionId) {

        var qType = questionRepository.findQuestionTypeById(questionId)
                .orElseThrow(() -> new QuestionNotFoundException(questionId))
                .getQuestionType();

        var manager = questionManagerFactory.get(qType);

        return manager.get(formId, questionId);
    }

    @Override
    @Cacheable(cacheNames = {QuestionCacheNames.QUESTION_SUMMARIES}, key = "#formId")
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
    @Cacheable(
            cacheNames = {QuestionCacheNames.QUESTION_SUMMARY},
            key = "'formId=' + #formId + '::questionId=' + #questionId"
    )
    @Transactional(readOnly = true)
    public QuestionSummaryDto getQuestionSummary(UUID formId, Long questionId) {

        var q = questionRepository.findQuestionSummaryById(questionId)
                .orElseThrow(() -> new QuestionNotFoundException(questionId));

        return new QuestionSummaryDto(q.getId(), q.getQuestion(), q.getQuestionType(), q.getOrderIndex());
    }

}
