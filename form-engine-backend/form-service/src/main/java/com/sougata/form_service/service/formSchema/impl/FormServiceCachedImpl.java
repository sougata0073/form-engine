package com.sougata.form_service.service.formSchema.impl;

import com.sougata.form_engine.constant.cache.FormCacheNames;
import com.sougata.form_engine.dto.form.FormDetailsDto;
import com.sougata.form_engine.dto.question.details.QuestionDetailsDto;
import com.sougata.form_service.exception.FormNotFoundException;
import com.sougata.form_service.model.formSchema.Form;
import com.sougata.form_service.model.formSchema.Question;
import com.sougata.form_service.repository.formSchema.AnyTypeQuestionRepositoryFactory;
import com.sougata.form_service.repository.formSchema.FormRepository;
import com.sougata.form_service.repository.formSchema.QuestionRepository;
import com.sougata.form_service.service.formSchema.FormServiceCached;
import com.sougata.form_service.service.formSchema.questionManager.QuestionManagerFactory;
import com.sougata.form_service.util.CacheUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FormServiceCachedImpl implements FormServiceCached {

    private final FormRepository formRepository;
    private final QuestionManagerFactory questionManagerFactory;
    private final AnyTypeQuestionRepositoryFactory anyTypeQuestionRepositoryFactory;
    private final QuestionRepository questionRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final Executor cacheLoaderExecutor;

    @Autowired
    @Lazy
    private FormServiceCached self;

    private final ConcurrentHashMap<UUID, CompletableFuture<?>> inFlight = new ConcurrentHashMap<>();

    @Override
    public FormDetailsDto getFormDetails(UUID formId) {

        var formDetailsCacheKey = CacheUtil.buildKey(FormCacheNames.FORM_DETAILS, formId);

        var formDetailsCached = (FormDetailsDto) redisTemplate.opsForValue().get(formDetailsCacheKey);

        if (formDetailsCached != null) {
            return formDetailsCached;
        }

        var future = inFlight.computeIfAbsent(
                formId,
                id -> CompletableFuture.supplyAsync(() -> self.loadFormDetailsFromDb(id), cacheLoaderExecutor)
        );

        try {
            return (FormDetailsDto) future.join();
        } finally {
            inFlight.remove(formId, future);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public FormDetailsDto loadFormDetailsFromDb(UUID formId) {

        Form f = formRepository.findById(formId).orElseThrow(() -> new FormNotFoundException(formId));

        List<QuestionDetailsDto> questionResponses = new ArrayList<>();

        var questions = questionRepository.findAllByFormId(formId);
        var questionIdMap = questions.stream().collect(Collectors.toMap(
                Question::getId,
                Function.identity()
        ));

        var questionTypeMap = questions.stream().collect(Collectors.groupingBy(Question::getQuestionType));

        questionTypeMap.forEach((qType, qs) -> {
            var repo = anyTypeQuestionRepositoryFactory.get(qType);
            var manager = questionManagerFactory.get(qType);

            var qIds = qs.stream().map(Question::getId).collect(Collectors.toList());

            var qResList = repo.findAllById((Iterable<Object>) (Iterable<?>) qIds).stream()
                    .map(q -> {
                        var parentQuestion = questionIdMap.get(q.getQuestionId());
                        return manager.toQuestionResDto(q, parentQuestion);
                    })
                    .toList();

            questionResponses.addAll(qResList);
        });

        questionResponses.sort(Comparator.comparingInt(QuestionDetailsDto::getOrderIndex));

        var formDetails = new FormDetailsDto(
                f.getId(),
                f.getName(),
                f.getTitle(),
                f.getDescription(),
                f.getPublished(),
                f.getAcceptingResponse(),
                f.getNotAcceptingResponseMessage(),
                f.getStopAcceptingResponseOn(),
                f.getStopAcceptingResponseAfterResponse(),
                f.getLastOpenedOn(),
                questionResponses
        );

        var formDetailsCacheKey = CacheUtil.buildKey(FormCacheNames.FORM_DETAILS, formId);

        redisTemplate.opsForValue().set(formDetailsCacheKey, formDetails);

        return formDetails;
    }
}
