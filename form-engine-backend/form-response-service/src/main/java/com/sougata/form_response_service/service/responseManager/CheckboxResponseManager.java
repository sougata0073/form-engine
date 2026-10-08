package com.sougata.form_response_service.service.responseManager;

import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.formResponse.individual.CheckboxResponseIndividualDto;
import com.sougata.form_engine.dto.formResponse.question.CheckboxResponseQuestionDto;
import com.sougata.form_engine.dto.formResponse.summary.CheckboxResponseSummaryDto;
import com.sougata.form_engine.dto.pgfunctionparameter.QuestionResponseBatches;
import com.sougata.form_engine.dto.question.details.CheckboxDetailsDto;
import com.sougata.form_engine.dto.question.responseputreqbatch.CheckboxResponseBatch;
import com.sougata.form_engine.dto.question.responseputreqbatch.FormResponseInfoQuestionResponse;
import com.sougata.form_engine.dto.question.responseputrequest.CheckboxResponsePutReqDto;
import com.sougata.form_engine.util.IdUtil;
import com.sougata.form_engine.util.JsonUtil;
import com.sougata.form_response_service.model.AnyTypeQuestionResponse;
import com.sougata.form_response_service.model.CheckboxResponse;
import com.sougata.form_response_service.model.QuestionResponseSummary;
import com.sougata.form_response_service.repository.CheckboxResponseRepository;
import com.sougata.form_response_service.repository.QuestionResponseSummaryRepository;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service("CHECKBOX_RESPONSE_MANAGER")
@RequiredArgsConstructor
public class CheckboxResponseManager extends ResponseManager<
        CheckboxDetailsDto,
        CheckboxResponsePutReqDto,
        CheckboxResponseSummaryDto,
        CheckboxResponseQuestionDto,
        CheckboxResponseQuestionDto.Response,
        CheckboxResponseIndividualDto,
        CheckboxResponseBatch,
        CheckboxResponseBatch.Response
        > {

    private final QuestionResponseSummaryRepository questionResponseSummaryRepository;
    private final CheckboxResponseRepository checkboxResponseRepository;

    @Override
    public void saveBatched(List<CheckboxResponseBatch> checkboxResponseBatches) {
        var responseBatches = new QuestionResponseBatches<>(checkboxResponseBatches);
        var responseBatchesJson = JsonUtil.toJson(responseBatches);

        checkboxResponseRepository.createOrIncrement(
                responseBatchesJson
        );

    }

    @Override
    public List<CheckboxResponseSummaryDto> getResponseSummaries(UUID formId, List<CheckboxDetailsDto> questionDetailsList) {
        var questionResponseSummaries = questionResponseSummaryRepository.findAllByFormId(formId);
        var checkboxResponses = checkboxResponseRepository.findAllByFormId(formId, Pageable.unpaged());

        var questionResponseSummariesMapByQuestionId = questionResponseSummaries
                .stream()
                .collect(Collectors.toMap(QuestionResponseSummary::getQuestionId, Function.identity()));

        var checkboxResponsesMapByQuestionId = checkboxResponses
                .stream()
                .collect(Collectors.groupingBy(AnyTypeQuestionResponse::getQuestionId));

        return questionDetailsList.stream().map(qd -> {
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

            return cbSummary;
        }).toList();
    }

    @Override
    public CheckboxResponseSummaryDto getResponseSummary(Long questionId, CheckboxDetailsDto questionDetails, Pageable pageable) {
        var questionResponseSummaryOptional = questionResponseSummaryRepository.findByQuestionId(questionId);

        var cbSummary = new CheckboxResponseSummaryDto();

        cbSummary.setNumberOfResponses(
                questionResponseSummaryOptional.isEmpty()
                        ? 0L : questionResponseSummaryOptional.get().getResponseCount()
        );
        cbSummary.setResponses(List.of());

        return cbSummary;
    }

    @Override
    public CheckboxResponseQuestionDto getResponseByQuestion(UUID formId, Long questionId, Map<String, String> extraParams, Pageable pageable) {

        var checkboxResponseQuestion = new CheckboxResponseQuestionDto();

        var groupedByOptionIds = checkboxResponseRepository.groupedByOptionIds(questionId, pageable);

        var responses = groupedByOptionIds.stream().map(tuple -> {
            var res = new CheckboxResponseQuestionDto.Response();

            var optionIdArray = tuple.get("optionIds", Long[].class);

            res.setResponseCount(tuple.get("responseCount", Long.class));
            res.setOptionIds(optionIdArray == null ? null : Arrays.stream(optionIdArray).map(Object::toString).toList());

            var formResponseIdentifierMap = new HashMap<String, List<String>>();
            formResponseIdentifierMap.put(
                    "optionIds",
                    res.getOptionIds() == null ? List.of() : res.getOptionIds()
            );

            res.setFormResponsesIdentifier(IdUtil.generateCompressedEncodedId(formResponseIdentifierMap));

            return res;

        }).toList();

        checkboxResponseQuestion.setResponses(responses);

        return checkboxResponseQuestion;
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
        var map = IdUtil.reconstructCompressedEncodedId(formResponsesIdentifier);

        var optionIds = map.get("optionIds");

        if (optionIds.isEmpty()) {
            throw new IllegalArgumentException("Invalid Form Responses Identifier. Identifier: " + formResponsesIdentifier);
        }

        var firstOptionId = optionIds.getFirst();

        var groupedResponse = firstOptionId == null ? new Long[]{null} : optionIds.stream().map(Long::parseLong).toArray(Long[]::new);

        return checkboxResponseRepository.getResponseIdsByGroupedResponse(questionId, groupedResponse, pageable);
    }

    @Override
    public CheckboxResponseBatch mapToBatchResponse(List<FormResponseInfoQuestionResponse<CheckboxResponsePutReqDto>> questionResponsePutReqs) {
        var batch = new CheckboxResponseBatch();

        var responseMap = new HashMap<Long, ArrayList<UUID>>();

        questionResponsePutReqs.forEach(q ->
                q.getQuestionResponsePutReq().getResponseOptionIds().forEach(optionId ->
                        responseMap.computeIfAbsent(optionId, _ -> new ArrayList<>()).add(q.getFormResponseId())
                )
        );

        var responses = responseMap.entrySet()
                .stream()
                .map(entry -> {
                    var res = new CheckboxResponseBatch.Response();

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
        return QuestionType.CHECKBOX;
    }

}
