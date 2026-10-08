package com.sougata.form_response_service.service.responseManager;

import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.formResponse.individual.DropdownResponseIndividualDto;
import com.sougata.form_engine.dto.formResponse.question.DateTimeResponseQuestionDto;
import com.sougata.form_engine.dto.formResponse.question.DropdownResponseQuestionDto;
import com.sougata.form_engine.dto.formResponse.summary.DropdownResponseSummaryDto;
import com.sougata.form_engine.dto.pgfunctionparameter.QuestionResponseBatches;
import com.sougata.form_engine.dto.question.details.DropdownDetailsDto;
import com.sougata.form_engine.dto.question.responseputreqbatch.DropdownResponseBatch;
import com.sougata.form_engine.dto.question.responseputreqbatch.FormResponseInfoQuestionResponse;
import com.sougata.form_engine.dto.question.responseputrequest.DropdownResponsePutReqDto;
import com.sougata.form_engine.util.IdUtil;
import com.sougata.form_engine.util.JsonUtil;
import com.sougata.form_response_service.model.AnyTypeQuestionResponse;
import com.sougata.form_response_service.model.DropdownResponse;
import com.sougata.form_response_service.model.QuestionResponseSummary;
import com.sougata.form_response_service.repository.DropdownResponseRepository;
import com.sougata.form_response_service.repository.QuestionResponseSummaryRepository;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service("DROPDOWN_RESPONSE_MANAGER")
@RequiredArgsConstructor
public class DropdownResponseManager extends ResponseManager<
        DropdownDetailsDto,
        DropdownResponsePutReqDto,
        DropdownResponseSummaryDto,
        DropdownResponseQuestionDto,
        DropdownResponseQuestionDto.Response,
        DropdownResponseIndividualDto,
        DropdownResponseBatch,
        DropdownResponseBatch.Response
        > {

    private final DropdownResponseRepository dropdownRepository;
    private final QuestionResponseSummaryRepository questionResponseSummaryRepository;

    @Override
    public void saveBatched(List<DropdownResponseBatch> dropdownResponseBatches) {
        var responseBatches = new QuestionResponseBatches<>(dropdownResponseBatches);
        var responseBatchesJson = JsonUtil.toJson(responseBatches);

        dropdownRepository.createOrIncrement(
                responseBatchesJson
        );

    }

    @Override
    public List<DropdownResponseSummaryDto> getResponseSummaries(UUID formId, List<DropdownDetailsDto> questionDetailsList) {
        var questionResponseSummaries = questionResponseSummaryRepository.findAllByFormId(formId);
        var dropdownResponses = dropdownRepository.findAllByFormId(formId, Pageable.unpaged());

        var questionResponseSummariesMapByQuestionId = questionResponseSummaries
                .stream()
                .collect(Collectors.toMap(QuestionResponseSummary::getQuestionId, Function.identity()));

        var dropdownResponsesMapByQuestionId = dropdownResponses
                .stream()
                .collect(Collectors.groupingBy(AnyTypeQuestionResponse::getQuestionId));

        return questionDetailsList.stream().map(qd -> {
            var questionResponseSummary = questionResponseSummariesMapByQuestionId.get(qd.getId());

            var ddSummary = new DropdownResponseSummaryDto();

            ddSummary.setQuestionId(qd.getId());
            ddSummary.setQuestion(qd.getQuestion());
            ddSummary.setOrderIndex(qd.getOrderIndex());
            ddSummary.setQuestionType(qd.getQuestionType());
            ddSummary.setNumberOfResponses(
                    questionResponseSummary == null ? 0L : questionResponseSummary.getResponseCount()
            );

            var dropdownResponsesForThisQuestion = dropdownResponsesMapByQuestionId.get(qd.getId());

            Map<Long, DropdownResponse> dropdownResponsesMapByOptionId = dropdownResponsesForThisQuestion == null
                    ? Map.of()
                    : dropdownResponsesForThisQuestion
                    .stream()
                    .collect(Collectors.toMap(DropdownResponse::getOptionId, Function.identity()));

            var responses = qd.getOptions().stream().map(option -> {

                var cbResponse = dropdownResponsesMapByOptionId.get(option.getId());

                return new DropdownResponseSummaryDto.Response(
                        option.getId(),
                        option.getOption(),
                        cbResponse == null ? 0L : cbResponse.getResponseCount()
                );

            }).toList();

            ddSummary.setResponses(responses);

            return ddSummary;
        }).toList();
    }

    @Override
    public DropdownResponseSummaryDto getResponseSummary(Long questionId, DropdownDetailsDto questionDetails, Pageable pageable) {
        var questionResponseSummaryOptional = questionResponseSummaryRepository.findByQuestionId(questionId);

        var ddSummary = new DropdownResponseSummaryDto();

        ddSummary.setNumberOfResponses(
                questionResponseSummaryOptional.isEmpty()
                        ? 0L : questionResponseSummaryOptional.get().getResponseCount()
        );
        ddSummary.setResponses(List.of());

        return ddSummary;
    }

    @Override
    public DropdownResponseQuestionDto getResponseByQuestion(UUID formId, Long questionId, Map<String, String> extraParams, Pageable pageable) {
        var dropdownResponseQuestion = new DropdownResponseQuestionDto();

        var groupedByOptionIds = dropdownRepository.groupedByOptionIds(questionId, pageable);

        var responses = groupedByOptionIds.stream().map(tuple -> {
            var res = new DropdownResponseQuestionDto.Response();

            res.setResponseCount(tuple.get("responseCount", Long.class));
            res.setOptionId(tuple.get("optionId", Long.class));

            var formResponseIdentifierMap = new HashMap<String, List<String>>();
            formResponseIdentifierMap.put(
                    "optionId",
                    List.of(res.getOptionId() == null ? "" : res.getOptionId().toString())
            );

            res.setFormResponsesIdentifier(IdUtil.generateCompressedEncodedId(formResponseIdentifierMap));

            return res;

        }).toList();

        dropdownResponseQuestion.setResponses(responses);

        return dropdownResponseQuestion;
    }

    @Override
    public List<DropdownResponseIndividualDto> getIndividualResponses(UUID formId, Long formResponseId) {
//        var responses = dropdownRepository.getOptionIdsByFormResponse(formResponseId);
//
//        return responses.stream().map(tuple -> {
//            var qId = tuple.get("questionId", Long.class);
//            var optionId = tuple.get("optionId", Long.class);
//
//            var res = new DropdownResponseIndividualDto();
//
//            res.setQuestionId(qId);
//            res.setQuestionType(getQuestionType());
//            res.setOptionId(optionId);
//
//            return res;
//        }).toList();

        return null;
    }

    @Override
    public List<Tuple> getFormResponseAndUserIds(UUID formId, Long questionId, String formResponsesIdentifier, Pageable pageable) {
//        var map = IdUtil.reconstructCompressedEncodedId(formResponsesIdentifier);
//
//        var optionId = map.get("optionId");
//
//        if (optionId.isEmpty()) {
//            throw new IllegalArgumentException("Invalid Form Responses Identifier. Identifier: " + formResponsesIdentifier);
//        }
//
//        var groupedResponse = optionId.getFirst() == null ? null : Long.parseLong(optionId.getFirst());
//
//        return dropdownRepository.getResponseIdsByGroupedResponse(formId, questionId, groupedResponse, pageable);

        return null;
    }

    @Override
    public DropdownResponseBatch mapToBatchResponse(List<FormResponseInfoQuestionResponse<DropdownResponsePutReqDto>> questionResponsePutReqs) {
        var batch = new DropdownResponseBatch();

        var responseMap = new HashMap<Long, ArrayList<UUID>>();

        questionResponsePutReqs.forEach(q ->
                responseMap
                        .computeIfAbsent(q.getQuestionResponsePutReq().getResponseOptionId(), _ -> new ArrayList<>())
                        .add(q.getFormResponseId())
        );

        var responses = responseMap.entrySet()
                .stream()
                .map(entry -> {
                    var res = new DropdownResponseBatch.Response();

                    res.setOptionId(entry.getKey());
                    res.setFormResponseIds(entry.getValue());
                    res.setResponseCount((long) entry.getValue().size());

                    return res;
                }).toList();

        batch.setResponses(responses);

        return batch;
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.DROPDOWN;
    }

}
