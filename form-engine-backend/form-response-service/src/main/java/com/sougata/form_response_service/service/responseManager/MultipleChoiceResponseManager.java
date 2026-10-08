package com.sougata.form_response_service.service.responseManager;

import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.formResponse.individual.MultipleChoiceResponseIndividualDto;
import com.sougata.form_engine.dto.formResponse.question.MultipleChoiceResponseQuestionDto;
import com.sougata.form_engine.dto.formResponse.summary.MultipleChoiceResponseSummaryDto;
import com.sougata.form_engine.dto.pgfunctionparameter.QuestionResponseBatches;
import com.sougata.form_engine.dto.question.details.MultipleChoiceDetailsDto;
import com.sougata.form_engine.dto.question.responseputreqbatch.FormResponseInfoQuestionResponse;
import com.sougata.form_engine.dto.question.responseputreqbatch.MultipleChoiceResponseBatch;
import com.sougata.form_engine.dto.question.responseputrequest.MultipleChoiceResponsePutReqDto;
import com.sougata.form_engine.util.IdUtil;
import com.sougata.form_engine.util.JsonUtil;
import com.sougata.form_response_service.model.AnyTypeQuestionResponse;
import com.sougata.form_response_service.model.MultipleChoiceResponse;
import com.sougata.form_response_service.model.QuestionResponseSummary;
import com.sougata.form_response_service.repository.MultipleChoiceResponseRepository;
import com.sougata.form_response_service.repository.QuestionResponseSummaryRepository;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service("MULTIPLE_CHOICE_RESPONSE_MANAGER")
@RequiredArgsConstructor
public class MultipleChoiceResponseManager extends ResponseManager<
        MultipleChoiceDetailsDto,
        MultipleChoiceResponsePutReqDto,
        MultipleChoiceResponseSummaryDto,
        MultipleChoiceResponseQuestionDto,
        MultipleChoiceResponseQuestionDto.Response,
        MultipleChoiceResponseIndividualDto,
        MultipleChoiceResponseBatch,
        MultipleChoiceResponseBatch.Response
        > {

    private final MultipleChoiceResponseRepository multipleChoiceRepository;
    private final QuestionResponseSummaryRepository questionResponseSummaryRepository;

    @Override
    public void saveBatched(List<MultipleChoiceResponseBatch> multipleChoiceResponseBatches) {
        var responseBatches = new QuestionResponseBatches<>(multipleChoiceResponseBatches);
        var responseBatchesJson = JsonUtil.toJson(responseBatches);

        multipleChoiceRepository.createOrIncrement(
                responseBatchesJson
        );

    }

    @Override
    public List<MultipleChoiceResponseSummaryDto> getResponseSummaries(UUID formId, List<MultipleChoiceDetailsDto> questionDetailsList) {
        var questionResponseSummaries = questionResponseSummaryRepository.findAllByFormId(formId);
        var multipleChoiceResponses = multipleChoiceRepository.findAllByFormId(formId, Pageable.unpaged());

        var questionResponseSummariesMapByQuestionId = questionResponseSummaries
                .stream()
                .collect(Collectors.toMap(QuestionResponseSummary::getQuestionId, Function.identity()));

        var multipleChoiceResponsesMapByQuestionId = multipleChoiceResponses
                .stream()
                .collect(Collectors.groupingBy(AnyTypeQuestionResponse::getQuestionId));

        return questionDetailsList.stream().map(qd -> {
            var questionResponseSummary = questionResponseSummariesMapByQuestionId.get(qd.getId());

            var mcSummary = new MultipleChoiceResponseSummaryDto();

            mcSummary.setQuestionId(qd.getId());
            mcSummary.setQuestion(qd.getQuestion());
            mcSummary.setOrderIndex(qd.getOrderIndex());
            mcSummary.setQuestionType(qd.getQuestionType());
            mcSummary.setNumberOfResponses(
                    questionResponseSummary == null ? 0L : questionResponseSummary.getResponseCount()
            );

            var multipleChoiceResponsesForThisQuestion = multipleChoiceResponsesMapByQuestionId.get(qd.getId());

            Map<Long, MultipleChoiceResponse> multipleChoiceResponsesMapByOptionId = multipleChoiceResponsesForThisQuestion == null
                    ? Map.of()
                    : multipleChoiceResponsesForThisQuestion
                    .stream()
                    .collect(Collectors.toMap(MultipleChoiceResponse::getOptionId, Function.identity()));

            var responses = qd.getOptions().stream().map(option -> {

                var cbResponse = multipleChoiceResponsesMapByOptionId.get(option.getId());

                return new MultipleChoiceResponseSummaryDto.Response(
                        option.getId(),
                        option.getOption(),
                        cbResponse == null ? 0L : cbResponse.getResponseCount()
                );

            }).toList();

            mcSummary.setResponses(responses);

            return mcSummary;
        }).toList();
    }

    @Override
    public MultipleChoiceResponseSummaryDto getResponseSummary(Long questionId, MultipleChoiceDetailsDto questionDetails, Pageable pageable) {
        var questionResponseSummaryOptional = questionResponseSummaryRepository.findByQuestionId(questionId);

        var mcSummary = new MultipleChoiceResponseSummaryDto();

        mcSummary.setNumberOfResponses(
                questionResponseSummaryOptional.isEmpty()
                        ? 0L : questionResponseSummaryOptional.get().getResponseCount()
        );
        mcSummary.setResponses(List.of());

        return mcSummary;
    }

    @Override
    public MultipleChoiceResponseQuestionDto getResponseByQuestion(UUID formId, Long questionId, Map<String, String> extraParams, Pageable pageable) {
        var grouped = multipleChoiceRepository.groupedByOptionIds(questionId, pageable);

        var mc = new MultipleChoiceResponseQuestionDto();

        var responses = grouped.stream().map(g -> {
            var res = new MultipleChoiceResponseQuestionDto.Response();

            res.setOptionId(g.get("optionId", Long.class));
            res.setResponseCount(g.get("responseCount", Long.class));

            var map = new HashMap<String, List<String>>();

            map.put("optionId", List.of(res.getOptionId() == null ? "" : res.getOptionId().toString()));

            res.setFormResponsesIdentifier(IdUtil.generateCompressedEncodedId(map));

            return res;
        }).toList();

        mc.setResponses(responses);

        return mc;
    }

    @Override
    public List<MultipleChoiceResponseIndividualDto> getIndividualResponses(UUID formId, Long formResponseId) {
//        var responses = multipleChoiceRepository.getOptionIdsByFormResponse(formResponseId);
//
//        return responses.stream().map(tuple -> {
//            var qId = tuple.get("questionId", Long.class);
//            var optionId = tuple.get("optionId", Long.class);
//
//            var res = new MultipleChoiceResponseIndividualDto();
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
//        return multipleChoiceRepository.getResponseIdsByGroupedResponse(formId, questionId, groupedResponse, pageable);

        return null;
    }

    @Override
    public MultipleChoiceResponseBatch mapToBatchResponse(List<FormResponseInfoQuestionResponse<MultipleChoiceResponsePutReqDto>> questionResponsePutReqs) {
        var batch = new MultipleChoiceResponseBatch();

        var responseMap = new HashMap<Long, ArrayList<UUID>>();

        questionResponsePutReqs.forEach(q ->
                responseMap
                        .computeIfAbsent(q.getQuestionResponsePutReq().getResponseOptionId(), _ -> new ArrayList<>())
                        .add(q.getFormResponseId())
        );

        var responses = responseMap.entrySet()
                .stream()
                .map(entry -> {
                    var res = new MultipleChoiceResponseBatch.Response();

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
        return QuestionType.MULTIPLE_CHOICE;
    }

}
