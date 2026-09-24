package com.sougata.form_response_service.service.responseManager;

import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.formResponse.individual.CheckboxResponseIndividualDto;
import com.sougata.form_engine.dto.formResponse.question.CheckboxResponseQuestionDto;
import com.sougata.form_engine.dto.formResponse.summary.CheckboxResponseSummaryDto;
import com.sougata.form_engine.dto.question.details.CheckboxDetailsDto;
import com.sougata.form_engine.dto.question.responseputrequest.CheckboxResponsePutReqDto;
import com.sougata.form_response_service.model.*;
import com.sougata.form_response_service.repository.*;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service("CHECKBOX_RESPONSE_MANAGER")
public class CheckboxResponseManager extends ResponseManager<
        CheckboxDetailsDto,
        CheckboxResponsePutReqDto,
        CheckboxResponseSummaryDto,
        CheckboxResponseQuestionDto,
        CheckboxResponseQuestionDto.Response,
        CheckboxResponseIndividualDto
        > {

    private final QuestionResponseSummaryRepository questionResponseSummaryRepository;
    private final QuestionResponseRepository questionResponseRepository;
    private final CheckboxResponseRepository checkboxResponseRepository;

    public CheckboxResponseManager(FormResponseSummaryRepository formResponseSummaryRepository, QuestionResponseSummaryRepository questionResponseSummaryRepository, QuestionResponseRepository questionResponseRepository, CheckboxResponseRepository checkboxResponseRepository) {
        super(formResponseSummaryRepository, questionResponseSummaryRepository);
        this.questionResponseSummaryRepository = questionResponseSummaryRepository;
        this.questionResponseRepository = questionResponseRepository;
        this.checkboxResponseRepository = checkboxResponseRepository;
    }

    @Override
    @Transactional
    public void onResponseSave(FormResponseSummary formResponseSummary, FormResponseIndividual formResponseIndividual, List<CheckboxResponsePutReqDto> questionResponsePutRequests) {

        var checkboxResponses = checkboxResponseRepository.findAllByFormId(formResponseSummary.getFormId());

        var checkboxResponsesMapByQuestionId = checkboxResponses
                .stream()
                .collect(Collectors.groupingBy(cr -> cr.getQuestionResponse().getQuestionResponseSummary().getQuestionId()));

        var questionResponseSummariesToSave = new ArrayList<QuestionResponseSummary>();
        var questionResponsesToSave = new ArrayList<QuestionResponse>();
        var checkboxResponsesToSave = new ArrayList<CheckboxResponse>();
        var missingOptionIdsMap = new HashMap<Long, Set<Long>>();

        questionResponsePutRequests.forEach(response -> {

            var checkboxResponsesForThisQuestion = checkboxResponsesMapByQuestionId.get(response.getQuestionId());

            if (checkboxResponsesForThisQuestion == null) {
                var questionResponseSummary = new QuestionResponseSummary();

                questionResponseSummary.setResponseCount(1L);
                questionResponseSummary.setQuestionId(response.getQuestionId());
                questionResponseSummary.setFormResponseSummary(formResponseSummary);

                questionResponseSummariesToSave.add(questionResponseSummary);
            }

            Set<Long> optionIdsForThisQuestion = checkboxResponsesForThisQuestion == null ? Set.of() :
                    checkboxResponsesForThisQuestion.stream().map(CheckboxResponse::getOptionId).collect(Collectors.toSet());

            var missingOptionIds = response.getResponseOptionIds()
                    .stream()
                    .filter(optionId -> !optionIdsForThisQuestion.contains(optionId))
                    .collect(Collectors.toSet());

            var alreadySavedOptionIds = new HashSet<>(response.getResponseOptionIds());
            alreadySavedOptionIds.removeAll(missingOptionIds);

            if (!alreadySavedOptionIds.isEmpty()) {
                checkboxResponseRepository.incrementResponseCountByOptionIds(
                        alreadySavedOptionIds, 1L
                );

                checkboxResponseRepository.insertFormResponseIndividualByOptionIds(
                        formResponseIndividual.getFormResponseId(),
                        alreadySavedOptionIds
                );
            }

            if (!missingOptionIds.isEmpty()) {
                missingOptionIdsMap.put(response.getQuestionId(), missingOptionIds);
            }

        });

        if (missingOptionIdsMap.isEmpty()) {
            return;
        }

        var alreadySavedQuestionResponseSummaries = questionResponseSummaryRepository.findAllByFormId(formResponseSummary.getFormId());
        ArrayList<QuestionResponseSummary> savedQuestionResponseSummaries = questionResponseSummariesToSave.isEmpty() ? new ArrayList<>() :
                new ArrayList<>(questionResponseSummaryRepository.saveAll(questionResponseSummariesToSave));

        savedQuestionResponseSummaries.addAll(alreadySavedQuestionResponseSummaries);

        var savedQuestionResponseSummariesMapByQuestionId = savedQuestionResponseSummaries
                .stream()
                .collect(Collectors.toMap(QuestionResponseSummary::getQuestionId, Function.identity()));

        missingOptionIdsMap.forEach((questionId, missingOptionIds) -> {

            var questionResponseSummary = savedQuestionResponseSummariesMapByQuestionId.get(questionId);

            if (questionResponseSummary == null) {
                throw new IllegalArgumentException("Question response summary not found for question ID: " + questionId);
            }

            missingOptionIds.forEach(_ -> {

                var questionResponseToSave = new QuestionResponse();

                questionResponseToSave.setQuestionResponseSummary(questionResponseSummary);
                questionResponseToSave.setFormResponseIndividuals(
                        List.of(formResponseIndividual)
                );

                questionResponsesToSave.add(questionResponseToSave);
            });

        });

        List<QuestionResponse> savedQuestionResponses = questionResponsesToSave.isEmpty() ? List.of() :
                questionResponseRepository.saveAll(questionResponsesToSave);

        var savedQuestionResponsesMapByQuestionId = savedQuestionResponses
                .stream()
                .collect(Collectors.groupingBy(qr -> qr.getQuestionResponseSummary().getQuestionId()));

        missingOptionIdsMap.forEach((questionId, missingOptionIds) -> {

            var missingOptionIdList = missingOptionIds.stream().toList();
            var questionResponsesForThisQuestion = savedQuestionResponsesMapByQuestionId.get(questionId);

            if (questionResponsesForThisQuestion == null) {
                throw new RuntimeException("Question responses not found for question ID: " + questionId);
            }

            if (missingOptionIds.size() != questionResponsesForThisQuestion.size()) {
                throw new RuntimeException(
                        "Invalid number of question responses saved. Number of missing option IDs: " + missingOptionIds.size() +
                                ". Number of question responses saved: " + questionResponsesForThisQuestion.size()
                );
            }

            for (int i = 0; i < questionResponsesForThisQuestion.size(); i++) {
                var checkboxResponseToSave = new CheckboxResponse();

                checkboxResponseToSave.setResponseCount(1L);
                checkboxResponseToSave.setOptionId(missingOptionIdList.get(i));
                checkboxResponseToSave.setQuestionResponse(questionResponsesForThisQuestion.get(i));

                checkboxResponsesToSave.add(checkboxResponseToSave);
            }

        });

        List<CheckboxResponse> savedCheckboxResponses = checkboxResponsesToSave.isEmpty() ? List.of() :
                checkboxResponseRepository.saveAll(checkboxResponsesToSave);
    }

    @Override
    public List<CheckboxResponseSummaryDto> getResponseSummaries(UUID formId, List<CheckboxDetailsDto> questionDetailsList) {
        var questionResponseSummaries = questionResponseSummaryRepository.findAllByFormId(formId);
        var checkboxResponses = checkboxResponseRepository.findAllByFormId(formId);

        var questionResponseSummariesMapByQuestionId = questionResponseSummaries
                .stream()
                .collect(Collectors.toMap(QuestionResponseSummary::getQuestionId, Function.identity()));

        var checkboxResponsesMapByQuestionId = checkboxResponses
                .stream()
                .collect(Collectors.groupingBy(cr -> cr.getQuestionResponse().getQuestionResponseSummary().getQuestionId()));

        var result = new ArrayList<CheckboxResponseSummaryDto>();

        questionDetailsList.forEach(qd -> {
            var questionResponseSummary = questionResponseSummariesMapByQuestionId.get(qd.getId());

            var cbSummary = new CheckboxResponseSummaryDto();

            cbSummary.setQuestionId(qd.getId());
            cbSummary.setQuestion(qd.getQuestion());
            cbSummary.setOrderIndex(qd.getOrderIndex());
            cbSummary.setQuestionType(qd.getQuestionType());
            cbSummary.setNumberOfResponses(
                    questionResponseSummary == null ? 0L : questionResponseSummary.getResponseCount()
            );

            var checkboxResponsesForThisQuestion = checkboxResponsesMapByQuestionId.get(qd.getId());

            Map<Long, CheckboxResponse> checkboxResponsesMapByOptionId = checkboxResponsesForThisQuestion == null
                    ? Map.of()
                    : checkboxResponsesForThisQuestion
                    .stream()
                    .collect(Collectors.toMap(CheckboxResponse::getOptionId, Function.identity()));

            var responses = qd.getOptions().stream().map(option -> {

                var cbResponse = checkboxResponsesMapByOptionId.get(option.getId());

                return new CheckboxResponseSummaryDto.Response(
                        option.getId(),
                        option.getOption(),
                        cbResponse == null ? 0L : cbResponse.getResponseCount()
                );

            }).toList();

            cbSummary.setResponses(responses);

            result.add(cbSummary);
        });

        return result;
    }

    @Override
    public CheckboxResponseSummaryDto getResponseSummary(Long questionId, CheckboxDetailsDto questionDetails, Pageable pageable) {
        var questionResponseSummaryOptional = questionResponseSummaryRepository.findByQuestionId(questionId);

        var cbSummary = new CheckboxResponseSummaryDto();

        cbSummary.setQuestionId(questionDetails.getId());
        cbSummary.setQuestion(questionDetails.getQuestion());
        cbSummary.setQuestionType(questionDetails.getQuestionType());
        cbSummary.setOrderIndex(questionDetails.getOrderIndex());
        cbSummary.setNumberOfResponses(
                questionResponseSummaryOptional.isEmpty()
                        ? 0L : questionResponseSummaryOptional.get().getResponseCount()
        );
        cbSummary.setResponses(List.of());

        return cbSummary;
    }

    @Override
    public CheckboxResponseQuestionDto getResponseByQuestion(UUID formId, Long questionId, Map<String, String> extraParams, Pageable pageable) {

//        var checkboxResponses = checkboxResponseRepository.findAllByQuestionId(questionId, pageable);
//
//        var crQuestion = new CheckboxResponseQuestionDto();
//
//        var responses = checkboxResponses.stream().map(cr -> {
//
//            var crQuestionResponse = new CheckboxResponseQuestionDto.Response();
//
//            crQuestionResponse.setQuestionId(questionId);
//            crQuestionResponse.setQuestionType(getQuestionType());
//
//            return crQuestionResponse;
//
//        }).toList();
//
//        crQuestion.setQuestionId(questionId);
//        crQuestion.setQuestionType(getQuestionType());
//        crQuestion.setResponses(responses);
//
//        var grouped = checkboxRepository.groupedByResponseOptions(formId, questionId, pageable);
//
//        var cb = new CheckboxResponseQuestionDto();
//
//        var responses = grouped.stream().map(g -> {
//            var res = new CheckboxResponseQuestionDto.Response();
//
//            res.setQuestionId(questionId);
//            res.setQuestionType(getQuestionType());
//            res.setResponseCount(g.get("responseCount", Long.class));
//
//            var opIdArray = g.get("optionIds", Long[].class);
//
//            res.setOptionIds(opIdArray == null ? null : Arrays.stream(opIdArray).map(Object::toString).toList());
//
//            var map = new HashMap<String, List<String>>();
//
//            map.put("optionIds", res.getOptionIds() == null ? List.of() : res.getOptionIds());
//
//            res.setFormResponsesIdentifier(IdUtil.generateCompressedEncodedId(map));
//
//            return res;
//        }).toList();
//
//        cb.setQuestionId(questionId);
//        cb.setQuestionType(getQuestionType());
//        cb.setResponses(responses);
//
//        return crQuestion;

        return null;
    }

    @Override
    public List<CheckboxResponseIndividualDto> getIndividualResponses(UUID formId, Long formResponseId) {
//        var responses = checkboxRepository.getOptionIdsByFormResponse(formResponseId);
//
//        return responses.stream().map(tuple -> {
//            var qId = tuple.get("questionId", Long.class);
//            var optionIds = Arrays.stream(tuple.get("optionIds", Long[].class)).map(Object::toString).toList();
//
//            var res = new CheckboxResponseIndividualDto();
//
//            res.setQuestionId(qId);
//            res.setQuestionType(getQuestionType());
//            res.setOptionIds(optionIds);
//
//            return res;
//        }).toList();

        return null;
    }

    @Override
    public List<Tuple> getFormResponseAndUserIds(UUID formId, Long questionId, String formResponsesIdentifier, Pageable pageable) {
//        var map = IdUtil.reconstructCompressedEncodedId(formResponsesIdentifier);
//
//        var optionIds = map.get("optionIds");
//
//        if (optionIds.isEmpty()) {
//            throw new IllegalArgumentException("Invalid Form Responses Identifier. Identifier: " + formResponsesIdentifier);
//        }
//
//        var firstOptionId = optionIds.getFirst();
//
//        var groupedResponse = firstOptionId == null ? new Long[]{null} : optionIds.stream().map(Long::parseLong).toArray(Long[]::new);
//
//        return checkboxRepository.getResponseIdsByGroupedResponse(formId, questionId, groupedResponse, pageable);

        return null;
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.CHECKBOX;
    }

}
