package com.sougata.form_response_service.service.responseManager;

import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.formResponse.individual.MultipleChoiceGridResponseIndividualDto;
import com.sougata.form_engine.dto.formResponse.question.MultipleChoiceGridResponseQuestionDto;
import com.sougata.form_engine.dto.formResponse.summary.MultipleChoiceGridResponseSummaryDto;
import com.sougata.form_engine.dto.pgfunctionparameter.QuestionResponseBatches;
import com.sougata.form_engine.dto.question.details.MultipleChoiceGridDetailsDto;
import com.sougata.form_engine.dto.question.responseputreqbatch.FormResponseInfoQuestionResponse;
import com.sougata.form_engine.dto.question.responseputreqbatch.MultipleChoiceGridResponseBatch;
import com.sougata.form_engine.dto.question.responseputrequest.MultipleChoiceGridResponsePutReqDto;
import com.sougata.form_engine.util.JsonUtil;
import com.sougata.form_response_service.model.AnyTypeQuestionResponse;
import com.sougata.form_response_service.model.MultipleChoiceGridResponse;
import com.sougata.form_response_service.model.QuestionResponseSummary;
import com.sougata.form_response_service.repository.MultipleChoiceGridResponseRepository;
import com.sougata.form_response_service.repository.QuestionResponseSummaryRepository;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service("MULTIPLE_CHOICE_GRID_RESPONSE_MANAGER")
@RequiredArgsConstructor
public class MultipleChoiceGridResponseManager extends ResponseManager<
        MultipleChoiceGridDetailsDto,
        MultipleChoiceGridResponsePutReqDto,
        MultipleChoiceGridResponseSummaryDto,
        MultipleChoiceGridResponseQuestionDto,
        MultipleChoiceGridResponseQuestionDto.Response,
        MultipleChoiceGridResponseIndividualDto,
        MultipleChoiceGridResponseBatch,
        MultipleChoiceGridResponseBatch.Response
        > {

    private final MultipleChoiceGridResponseRepository multipleChoiceGridRepository;
    private final QuestionResponseSummaryRepository questionResponseSummaryRepository;

    @Override
    public void saveBatched(List<MultipleChoiceGridResponseBatch> multipleChoiceGridResponseBatches) {
        var responseBatches = new QuestionResponseBatches<>(multipleChoiceGridResponseBatches);
        var responseBatchesJson = JsonUtil.toJson(responseBatches);

        multipleChoiceGridRepository.createOrIncrement(
                responseBatchesJson
        );

    }

    @Override
    public List<MultipleChoiceGridResponseSummaryDto> getResponseSummaries(UUID formId, List<MultipleChoiceGridDetailsDto> questionDetailsList) {

        var questionResponseSummaries = questionResponseSummaryRepository.findAllByFormId(formId);
        var multipleChoiceGridResponses = multipleChoiceGridRepository.findAllByFormId(formId, Pageable.unpaged());

        var questionResponseSummariesMapByQuestionId = questionResponseSummaries
                .stream()
                .collect(Collectors.toMap(QuestionResponseSummary::getQuestionId, Function.identity()));

        var multipleChoiceGridResponsesMapByQuestionId = multipleChoiceGridResponses
                .stream()
                .collect(Collectors.groupingBy(AnyTypeQuestionResponse::getQuestionId));

        return questionDetailsList.stream().map(qd -> {
            var questionResponseSummary = questionResponseSummariesMapByQuestionId.get(qd.getId());

            var mcgSummary = new MultipleChoiceGridResponseSummaryDto();

            mcgSummary.setQuestionId(qd.getId());
            mcgSummary.setQuestion(qd.getQuestion());
            mcgSummary.setOrderIndex(qd.getOrderIndex());
            mcgSummary.setQuestionType(qd.getQuestionType());
            mcgSummary.setNumberOfResponses(
                    questionResponseSummary == null ? 0L : questionResponseSummary.getResponseCount()
            );

            var multipleChoiceGridResponsesForThisQuestion = multipleChoiceGridResponsesMapByQuestionId.get(qd.getId());

            Map<Long, List<MultipleChoiceGridResponse>> multipleChoiceGridResponsesGroupedByRowId = multipleChoiceGridResponsesForThisQuestion == null
                    ? Map.of()
                    : multipleChoiceGridResponsesForThisQuestion
                    .stream()
                    .collect(Collectors.groupingBy(MultipleChoiceGridResponse::getRowId));

            var responses = qd.getRows().stream().map(row -> {

                var multipleChoiceGridResponsesForThisRow = multipleChoiceGridResponsesGroupedByRowId.get(row.getId());

                Map<Long, MultipleChoiceGridResponse> multipleChoiceGridResponseMapByColumnId = multipleChoiceGridResponsesForThisRow == null
                        ? Map.of()
                        : multipleChoiceGridResponsesForThisRow
                        .stream()
                        .collect(Collectors.toMap(MultipleChoiceGridResponse::getColumnId, Function.identity()));

                var rowResponse = new MultipleChoiceGridResponseSummaryDto.RowResponse();

                var columnResponses = qd.getColumns().stream().map(column -> {

                    var mcgResponseByColumn = multipleChoiceGridResponseMapByColumnId.get(column.getId());

                    return new MultipleChoiceGridResponseSummaryDto.ColumnResponse(
                            column.getId(),
                            column.getColumn(),
                            mcgResponseByColumn == null ? 0L : mcgResponseByColumn.getResponseCount()
                    );

                }).toList();

                rowResponse.setRowId(row.getId());
                rowResponse.setRow(row.getRow());
                rowResponse.setResponses(columnResponses);

                return rowResponse;

            }).toList();

            mcgSummary.setResponses(responses);

            return mcgSummary;

        }).toList();
    }

    @Override
    public MultipleChoiceGridResponseSummaryDto getResponseSummary(Long questionId, MultipleChoiceGridDetailsDto questionRes, Pageable pageable) {
        var questionResponseSummaryOptional = questionResponseSummaryRepository.findByQuestionId(questionId);
        var res = new MultipleChoiceGridResponseSummaryDto();

        res.setNumberOfResponses(
                questionResponseSummaryOptional.isEmpty()
                        ? 0L : questionResponseSummaryOptional.get().getResponseCount()
        );
        res.setResponses(List.of());

        return res;

    }

    @Override
    public MultipleChoiceGridResponseQuestionDto getResponseByQuestion(UUID formId, Long questionId, Map<String, String> extraParams, Pageable pageable) {

//        var rowIdString = extraParams.get("rowId");
//
//        if (rowIdString == null) {
//            throw new IllegalArgumentException("Row ID is required");
//        }
//
//        long rowId;
//
//        try {
//            rowId = Long.parseLong(rowIdString);
//        } catch (NumberFormatException e) {
//            throw new IllegalArgumentException("Invalid row ID: " + rowIdString);
//        }
//
//        var grouped = multipleChoiceGridRepository.groupedByResponseRowColumn(
//                formId,
//                questionId,
//                rowId,
//                pageable
//        );
//
//        var mc = new MultipleChoiceGridResponseQuestionDto();
//
//        var responses = grouped.stream().map(g -> {
//            var res = new MultipleChoiceGridResponseQuestionDto.Response();
//
//            res.setQuestionId(questionId);
//            res.setQuestionType(getQuestionType());
//            res.setColumnId(g.get("columnId", Long.class));
//            res.setResponseCount(g.get("responseCount", Long.class));
//
//            var map = new HashMap<String, List<String>>();
//
//            map.put("rowId", List.of(rowIdString));
//            map.put("columnId", List.of(res.getColumnId() == null ? "" : res.getColumnId().toString()));
//
//            res.setFormResponsesIdentifier(IdUtil.generateCompressedEncodedId(map));
//
//            return res;
//        }).toList();
//
//        mc.setQuestionId(questionId);
//        mc.setQuestionType(getQuestionType());
//        mc.setRowId(rowId);
//        mc.setResponses(responses);
//
//        return mc;

        return null;
    }

    @Override
    public List<MultipleChoiceGridResponseIndividualDto> getIndividualResponses(UUID formId, Long formResponseId) {
//        var responses = multipleChoiceGridRepository.getRowColumnIdsByFormResponse(formResponseId);
//
//        return responses.stream().map(tuple -> {
//            var qId = tuple.get("questionId", Long.class);
//            var rowIds = tuple.get("rowIds", Long[].class);
//            var columnIds = tuple.get("columnIds", Long[].class);
//
//            var res = new MultipleChoiceGridResponseIndividualDto();
//
//            res.setQuestionId(qId);
//            res.setQuestionType(getQuestionType());
//
//            var rows = new ArrayList<MultipleChoiceGridResponseIndividualDto.Row>();
//
//            for (int i = 0; i < rowIds.length; i++) {
//                rows.add(
//                        new MultipleChoiceGridResponseIndividualDto.Row(
//                                rowIds[i], columnIds[i]
//                        )
//                );
//            }
//
//            res.setRows(rows);
//
//            return res;
//        }).toList();

        return null;
    }

    @Override
    public List<Tuple> getFormResponseAndUserIds(UUID formId, Long questionId, String formResponsesIdentifier, Pageable pageable) {
//        var map = IdUtil.reconstructCompressedEncodedId(formResponsesIdentifier);
//
//        var rowId = map.get("rowId");
//        var columnId = map.get("columnId");
//
//        if (rowId.isEmpty() || columnId.isEmpty()) {
//            throw new IllegalArgumentException("Invalid Form Responses Identifier. Identifier: " + formResponsesIdentifier);
//        }
//
//        var rowIdResponse = rowId.getFirst() == null ? null :  Long.parseLong(rowId.getFirst());
//        var columnIdResponse = columnId.getFirst() == null ? null : Long.parseLong(columnId.getFirst());
//
//        return multipleChoiceGridRepository.getResponseIdsByGroupedResponse(formId, questionId, rowIdResponse, columnIdResponse, pageable);

        return null;
    }

    @Override
    public MultipleChoiceGridResponseBatch mapToBatchResponse(List<FormResponseInfoQuestionResponse<MultipleChoiceGridResponsePutReqDto>> questionResponsePutReqs) {
        var batch = new MultipleChoiceGridResponseBatch();

        var responseMap = new HashMap<String, ArrayList<UUID>>();

        questionResponsePutReqs.forEach(q ->
                q.getQuestionResponsePutReq().getRows().forEach(row -> {
                            var rowColumnString = String.format(
                                    "%d_%d",
                                    row.getRowId(),
                                    row.getResponseColumnId()
                            );
                            responseMap
                                    .computeIfAbsent(rowColumnString, _ -> new ArrayList<>())
                                    .add(q.getFormResponseId());
                        }
                )
        );

        var responses = responseMap.entrySet()
                .stream()
                .map(entry -> {
                    var res = new MultipleChoiceGridResponseBatch.Response();

                    var rowColumnString = entry.getKey();
                    var rowColumnArray = rowColumnString.split("_");

                    res.setRowId(Long.parseLong(rowColumnArray[0]));
                    res.setColumnId(Long.parseLong(rowColumnArray[1]));
                    res.setFormResponseIds(entry.getValue());
                    res.setResponseCount((long) entry.getValue().size());

                    return res;
                }).toList();

        batch.setResponses(responses);

        return batch;
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.MULTIPLE_CHOICE_GRID;
    }

}
