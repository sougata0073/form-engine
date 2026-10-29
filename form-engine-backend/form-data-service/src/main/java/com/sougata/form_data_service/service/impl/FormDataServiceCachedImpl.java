package com.sougata.form_data_service.service.impl;

import com.sougata.form_data_service.model.FormResponse;
import com.sougata.form_data_service.repository.FormResponseRepository;
import com.sougata.form_data_service.service.FormDataServiceCached;
import com.sougata.form_data_service.service.responseManager.ResponseManagerFactory;
import com.sougata.form_engine.constant.cache.FormDataCacheNames;
import com.sougata.form_engine.dto.formResponse.individual.FormResponseIndividualDtoNoPage;
import com.sougata.form_engine.dto.formResponse.individual.QuestionResponseIndividualDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FormDataServiceCachedImpl implements FormDataServiceCached {

    private final FormResponseRepository formResponseRepository;
    private final ResponseManagerFactory responseManagerFactory;

    @Override
    @Cacheable(
            cacheNames = {FormDataCacheNames.FORM_RESPONSE_NO_PAGE},
            key = "'formId=' + #formId + '::formResponseId=' + #formResponseId"
    )
    public FormResponseIndividualDtoNoPage getFormResponse(UUID formId, UUID formResponseId) {
        var formResponseKey = new FormResponse.PartitionKey(formId, formResponseId);

        var formResponse = formResponseRepository.findById(formResponseKey)
                .orElseThrow(
                        () -> new RuntimeException("Form response not found for form Id: " + formId + " and form response ID: " + formResponseId)
                );

        var responseFutures = new ArrayList<CompletableFuture<List<QuestionResponseIndividualDto>>>();

        Optional.ofNullable(formResponse.getRespondedQuestionMap())
                .orElse(Collections.emptyMap())
                .entrySet()
                .stream()
                .collect(Collectors.groupingBy(Map.Entry::getValue))
                .forEach(((questionType, entries) -> {
                    var questionIds = entries.stream().map(Map.Entry::getKey).collect(Collectors.toSet());
                    var manager = responseManagerFactory.get(questionType);

                    responseFutures.add(manager.getResponses(formId, formResponseId, questionIds));
                }));

        var responses = CompletableFuture.allOf(responseFutures.toArray(CompletableFuture[]::new))
                .thenApply(v -> responseFutures.stream()
                        .map(CompletableFuture::join)
                        .flatMap(List::stream)
                        .toList()
                ).join();

        return new FormResponseIndividualDtoNoPage(
                formResponse.getKey().getFormResponseId(),
                formResponse.getUserId(),
                responses
        );
    }

}
