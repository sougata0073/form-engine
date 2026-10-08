package com.sougata.form_response_service.service.responseManager;

import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.formResponse.individual.LinearScaleResponseIndividualDto;
import com.sougata.form_engine.dto.formResponse.question.LinearScaleResponseQuestionDto;
import com.sougata.form_engine.dto.formResponse.summary.LinearScaleResponseSummaryDto;
import com.sougata.form_engine.dto.pgfunctionparameter.QuestionResponseBatches;
import com.sougata.form_engine.dto.question.details.LinearScaleDetailsDto;
import com.sougata.form_engine.dto.question.responseputreqbatch.FormResponseInfoQuestionResponse;
import com.sougata.form_engine.dto.question.responseputreqbatch.LinearScaleResponseBatch;
import com.sougata.form_engine.dto.question.responseputrequest.LinearScaleResponsePutReqDto;
import com.sougata.form_engine.util.IdUtil;
import com.sougata.form_engine.util.JsonUtil;
import com.sougata.form_response_service.model.AnyTypeQuestionResponse;
import com.sougata.form_response_service.model.LinearScaleResponse;
import com.sougata.form_response_service.model.QuestionResponseSummary;
import com.sougata.form_response_service.repository.LinearScaleResponseRepository;
import com.sougata.form_response_service.repository.QuestionResponseSummaryRepository;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service("LINEAR_SCALE_RESPONSE_MANAGER")
@RequiredArgsConstructor
public class LinearScaleResponseManager extends ResponseManager<
        LinearScaleDetailsDto,
        LinearScaleResponsePutReqDto,
        LinearScaleResponseSummaryDto,
        LinearScaleResponseQuestionDto,
        LinearScaleResponseQuestionDto.Response,
        LinearScaleResponseIndividualDto,
        LinearScaleResponseBatch,
        LinearScaleResponseBatch.Response
        > {

    private final LinearScaleResponseRepository linearScaleRepository;
    private final QuestionResponseSummaryRepository questionResponseSummaryRepository;

    @Override
    public void saveBatched(List<LinearScaleResponseBatch> linearScaleResponseBatches) {
        var responseBatches = new QuestionResponseBatches<>(linearScaleResponseBatches);
        var responseBatchesJson = JsonUtil.toJson(responseBatches);

        linearScaleRepository.createOrIncrement(
                responseBatchesJson
        );

    }

    @Override
    public List<LinearScaleResponseSummaryDto> getResponseSummaries(UUID formId, List<LinearScaleDetailsDto> questionDetailsList) {
        var questionResponseSummaries = questionResponseSummaryRepository.findAllByFormId(formId);
        var linearScaleResponses = linearScaleRepository.findAllByFormId(formId, Pageable.unpaged());

        var questionResponseSummariesMapByQuestionId = questionResponseSummaries
                .stream()
                .collect(Collectors.toMap(QuestionResponseSummary::getQuestionId, Function.identity()));

        var linearScaleResponsesMapByQuestionId = linearScaleResponses
                .stream()
                .collect(Collectors.groupingBy(AnyTypeQuestionResponse::getQuestionId));

        return questionDetailsList.stream().map(qd -> {
            var questionResponseSummary = questionResponseSummariesMapByQuestionId.get(qd.getId());

            var lsSummary = new LinearScaleResponseSummaryDto();

            lsSummary.setQuestionId(qd.getId());
            lsSummary.setQuestion(qd.getQuestion());
            lsSummary.setOrderIndex(qd.getOrderIndex());
            lsSummary.setQuestionType(qd.getQuestionType());
            lsSummary.setNumberOfResponses(
                    questionResponseSummary == null ? 0L : questionResponseSummary.getResponseCount()
            );

            var linearScaleResponsesForThisQuestion = linearScaleResponsesMapByQuestionId.get(qd.getId());

            Map<Integer, LinearScaleResponse> linearScaleResponsesMapByScale = linearScaleResponsesForThisQuestion == null
                    ? Map.of()
                    : linearScaleResponsesForThisQuestion
                    .stream()
                    .collect(Collectors.toMap(LinearScaleResponse::getScale, Function.identity()));

            var responses = new ArrayList<LinearScaleResponseSummaryDto.Response>();

            for (int i = qd.getFromNumber(); i <= qd.getToNumber(); i++) {
                var lsResponse = linearScaleResponsesMapByScale.get(i);

                responses.add(
                        new LinearScaleResponseSummaryDto.Response(
                                i,
                                lsResponse == null ? 0L : lsResponse.getResponseCount()
                        )
                );
            }

            lsSummary.setResponses(responses);

            return lsSummary;
        }).toList();
    }

    @Override
    public LinearScaleResponseSummaryDto getResponseSummary(Long questionId, LinearScaleDetailsDto questionRes, Pageable pageable) {
        var questionResponseSummaryOptional = questionResponseSummaryRepository.findByQuestionId(questionId);
        var res = new LinearScaleResponseSummaryDto();

        res.setNumberOfResponses(
                questionResponseSummaryOptional.isEmpty()
                        ? 0L : questionResponseSummaryOptional.get().getResponseCount()
        );
        res.setResponses(List.of());

        return res;
    }

    @Override
    public LinearScaleResponseQuestionDto getResponseByQuestion(UUID formId, Long questionId, Map<String, String> extraParams, Pageable pageable) {
        var grouped = linearScaleRepository.groupedByScale(questionId, pageable);

        var ls = new LinearScaleResponseQuestionDto();

        var responses = grouped.stream().map(g -> {
            var res = new LinearScaleResponseQuestionDto.Response();

            res.setQuestionId(questionId);
            res.setQuestionType(getQuestionType());
            res.setScale(g.get("scale", Integer.class));
            res.setResponseCount(g.get("responseCount", Long.class));

            var map = new HashMap<String, List<String>>();

            map.put("scale", List.of(res.getScale() == null ? "" : res.getScale().toString()));

            res.setFormResponsesIdentifier(IdUtil.generateCompressedEncodedId(map));

            return res;
        }).toList();

        ls.setResponses(responses);

        return ls;
    }

    @Override
    public List<LinearScaleResponseIndividualDto> getIndividualResponses(UUID formId, Long formResponseId) {
//        var responses = linearScaleRepository.getScalesByFormResponse(formResponseId);
//
//        return responses.stream().map(tuple -> {
//            var qId = tuple.get("questionId", Long.class);
//            var scale = tuple.get("scale", Integer.class);
//
//            var res = new LinearScaleResponseIndividualDto();
//
//            res.setQuestionId(qId);
//            res.setQuestionType(getQuestionType());
//            res.setScale(scale);
//
//            return res;
//        }).toList();

        return null;
    }

    @Override
    public List<Tuple> getFormResponseAndUserIds(UUID formId, Long questionId, String formResponsesIdentifier, Pageable pageable) {
//        var map = IdUtil.reconstructCompressedEncodedId(formResponsesIdentifier);
//
//        var scale = map.get("scale");
//
//        if (scale.isEmpty()) {
//            throw new IllegalArgumentException("Invalid Form Responses Identifier. Identifier: " + formResponsesIdentifier);
//        }
//
//        var groupedResponse = scale.getFirst() == null ? null : Integer.parseInt(scale.getFirst());
//
//        return linearScaleRepository.getResponseIdsByGroupedResponse(formId, questionId, groupedResponse, pageable);

        return null;
    }

    @Override
    public LinearScaleResponseBatch mapToBatchResponse(List<FormResponseInfoQuestionResponse<LinearScaleResponsePutReqDto>> questionResponsePutReqs) {
        var batch = new LinearScaleResponseBatch();

        var responseMap = new HashMap<Integer, ArrayList<UUID>>();

        questionResponsePutReqs.forEach(q ->
                responseMap
                        .computeIfAbsent(q.getQuestionResponsePutReq().getScale(), _ -> new ArrayList<>())
                        .add(q.getFormResponseId())
        );

        var responses = responseMap.entrySet()
                .stream()
                .map(entry -> {
                    var res = new LinearScaleResponseBatch.Response();

                    res.setScale(entry.getKey());
                    res.setFormResponseIds(entry.getValue());
                    res.setResponseCount((long) entry.getValue().size());

                    return res;
                }).toList();

        batch.setResponses(responses);

        return batch;
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.LINEAR_SCALE;
    }

}
