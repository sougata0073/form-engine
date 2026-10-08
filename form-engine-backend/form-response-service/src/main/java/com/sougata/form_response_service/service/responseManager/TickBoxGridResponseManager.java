package com.sougata.form_response_service.service.responseManager;

import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.formResponse.individual.TickBoxGridResponseIndividualDto;
import com.sougata.form_engine.dto.formResponse.question.TickBoxGridResponseQuestionDto;
import com.sougata.form_engine.dto.formResponse.summary.TickBoxGridResponseSummaryDto;
import com.sougata.form_engine.dto.pgfunctionparameter.QuestionResponseBatches;
import com.sougata.form_engine.dto.question.details.TickBoxGridDetailsDto;
import com.sougata.form_engine.dto.question.responseputreqbatch.FormResponseInfoQuestionResponse;
import com.sougata.form_engine.dto.question.responseputreqbatch.TickBoxGridResponseBatch;
import com.sougata.form_engine.dto.question.responseputrequest.TickBoxGridResponsePutReqDto;
import com.sougata.form_engine.util.JsonUtil;
import com.sougata.form_response_service.model.AnyTypeQuestionResponse;
import com.sougata.form_response_service.model.QuestionResponseSummary;
import com.sougata.form_response_service.model.TickBoxGridResponse;
import com.sougata.form_response_service.repository.QuestionResponseSummaryRepository;
import com.sougata.form_response_service.repository.TickBoxGridResponseRepository;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service("TICK_BOX_GRID_RESPONSE_MANAGER")
@RequiredArgsConstructor
public class TickBoxGridResponseManager extends ResponseManager<
        TickBoxGridDetailsDto,
        TickBoxGridResponsePutReqDto,
        TickBoxGridResponseSummaryDto,
        TickBoxGridResponseQuestionDto,
        TickBoxGridResponseQuestionDto.Response,
        TickBoxGridResponseIndividualDto,
        TickBoxGridResponseBatch,
        TickBoxGridResponseBatch.Response
        > {

    private final TickBoxGridResponseRepository tickBoxGridRepository;
    private final QuestionResponseSummaryRepository questionResponseSummaryRepository;

    @Override
    public void saveBatched(List<TickBoxGridResponseBatch> tickBoxGridResponseBatches) {
        var responseBatches = new QuestionResponseBatches<>(tickBoxGridResponseBatches);
        var responseBatchesJson = JsonUtil.toJson(responseBatches);

        tickBoxGridRepository.createOrIncrement(
                responseBatchesJson
        );

    }

    @Override
    public List<TickBoxGridResponseSummaryDto> getResponseSummaries(UUID formId, List<TickBoxGridDetailsDto> questionDetailsList) {

        var questionResponseSummaries = questionResponseSummaryRepository.findAllByFormId(formId);
        var tickBoxGridResponses = tickBoxGridRepository.findAllByFormId(formId, Pageable.unpaged());

        var questionResponseSummariesMapByQuestionId = questionResponseSummaries
                .stream()
                .collect(Collectors.toMap(QuestionResponseSummary::getQuestionId, Function.identity()));

        var tickBoxGridResponsesMapByQuestionId = tickBoxGridResponses
                .stream()
                .collect(Collectors.groupingBy(AnyTypeQuestionResponse::getQuestionId));

        return questionDetailsList.stream().map(qd -> {
            var questionResponseSummary = questionResponseSummariesMapByQuestionId.get(qd.getId());

            var tbgSummary = new TickBoxGridResponseSummaryDto();

            tbgSummary.setQuestionId(qd.getId());
            tbgSummary.setQuestion(qd.getQuestion());
            tbgSummary.setOrderIndex(qd.getOrderIndex());
            tbgSummary.setQuestionType(qd.getQuestionType());
            tbgSummary.setNumberOfResponses(
                    questionResponseSummary == null ? 0L : questionResponseSummary.getResponseCount()
            );

            var tickBoxGridResponsesForThisQuestion = tickBoxGridResponsesMapByQuestionId.get(qd.getId());

            Map<Long, List<TickBoxGridResponse>> tickBoxGridResponsesGroupedByRowId = tickBoxGridResponsesForThisQuestion == null
                    ? Map.of()
                    : tickBoxGridResponsesForThisQuestion
                    .stream()
                    .collect(Collectors.groupingBy(TickBoxGridResponse::getRowId));

            var responses = qd.getRows().stream().map(row -> {

                var tickBoxGridResponsesForThisRow = tickBoxGridResponsesGroupedByRowId.get(row.getId());

                Map<Long, TickBoxGridResponse> tickBoxGridResponseMapByColumnId = tickBoxGridResponsesForThisRow == null
                        ? Map.of()
                        : tickBoxGridResponsesForThisRow
                        .stream()
                        .collect(Collectors.toMap(TickBoxGridResponse::getColumnId, Function.identity()));

                var rowResponse = new TickBoxGridResponseSummaryDto.RowResponse();

                var columnResponses = qd.getColumns().stream().map(column -> {

                    var tbgResponseByColumn = tickBoxGridResponseMapByColumnId.get(column.getId());

                    return new TickBoxGridResponseSummaryDto.ColumnResponse(
                            column.getId(),
                            column.getColumn(),
                            tbgResponseByColumn == null ? 0L : tbgResponseByColumn.getResponseCount()
                    );

                }).toList();

                rowResponse.setRowId(row.getId());
                rowResponse.setRow(row.getRow());
                rowResponse.setResponses(columnResponses);

                return rowResponse;

            }).toList();

            tbgSummary.setResponses(responses);

            return tbgSummary;

        }).toList();
    }

    @Override
    public TickBoxGridResponseSummaryDto getResponseSummary(Long questionId, TickBoxGridDetailsDto questionRes, Pageable pageable) {
        var questionResponseSummaryOptional = questionResponseSummaryRepository.findByQuestionId(questionId);
        var res = new TickBoxGridResponseSummaryDto();

        res.setNumberOfResponses(
                questionResponseSummaryOptional.isEmpty()
                        ? 0L : questionResponseSummaryOptional.get().getResponseCount()
        );
        res.setResponses(List.of());

        return res;
    }

    @Override
    public TickBoxGridResponseQuestionDto getResponseByQuestion(UUID formId, Long questionId, Map<String, String> extraParams, Pageable pageable) {

//        var rowIdString = extraParams.get("rowId");
//
//        if (rowIdString == null) {
//            throw new IllegalArgumentException("Row ID is required");
//        }
//
//        var rowId = Long.parseLong(rowIdString);
//
//        var grouped = tickBoxGridRepository.groupedByResponseRowColumn(
//                formId,
//                questionId,
//                rowId,
//                pageable
//        );
//
//        var tb = new TickBoxGridResponseQuestionDto();
//
//        var responses = grouped.stream().map(g -> {
//            var res = new TickBoxGridResponseQuestionDto.Response();
//
//            res.setQuestionId(questionId);
//            res.setQuestionType(getQuestionType());
//            res.setResponseCount(g.get("responseCount", Long.class));
//
//            var colIdArray = g.get("columnIds", Long[].class);
//
//            res.setColumnIds(colIdArray == null ? null : Arrays.stream(colIdArray).map(Object::toString).toList());
//
//            var map = new HashMap<String, List<String>>();
//
//            map.put("rowId", List.of(rowIdString));
//            map.put("columnIds", res.getColumnIds() == null ? List.of() : res.getColumnIds());
//
//            res.setFormResponsesIdentifier(IdUtil.generateCompressedEncodedId(map));
//
//            return res;
//        }).toList();
//
//        tb.setQuestionId(questionId);
//        tb.setQuestionType(getQuestionType());
//        tb.setRowId(rowId);
//        tb.setResponses(responses);
//
//        return tb;

        return null;
    }

    @Override
    public List<TickBoxGridResponseIndividualDto> getIndividualResponses(UUID formId, Long formResponseId) {
//        var responses = tickBoxGridRepository.getRowColumnIdsByFormResponse(formResponseId);
//
//        var questionIdMap = responses.stream().collect(
//                Collectors.groupingBy(tuple -> tuple.get("questionId", Long.class))
//        );
//
//        return questionIdMap.entrySet().stream().map(entry -> {
//            var qId = entry.getKey();
//            var rowIds = entry.getValue().stream().map(tuple -> tuple.get("rowId", Long.class)).toList();
//            var columnIds = entry.getValue().stream().map(tuple -> tuple.get("columnIds", Long[].class)).toList();
//
//            var res = new TickBoxGridResponseIndividualDto();
//
//            res.setQuestionId(qId);
//            res.setQuestionType(getQuestionType());
//
//            var rows = new ArrayList<TickBoxGridResponseIndividualDto.Row>();
//
//            for (int i = 0; i < rowIds.size(); i++) {
//                var rowId = rowIds.get(i);
//                var colIds = columnIds.get(i);
//                rows.add(
//                        new TickBoxGridResponseIndividualDto.Row(
//                                rowId, Arrays.stream(colIds).map(Object::toString).toList()
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
//        var columnIds = map.get("columnIds");
//
//        if (rowId.isEmpty() || columnIds.isEmpty()) {
//            throw new IllegalArgumentException("Invalid Form Responses Identifier. Identifier: " + formResponsesIdentifier);
//        }
//
//        var rowIdResponse = rowId.getFirst() == null ? null : Long.parseLong(rowId.getFirst());
//        var firstColumnId = columnIds.getFirst();
//
//        var columnIdsResponse = firstColumnId == null ? new Long[]{null} : columnIds.stream().map(Long::parseLong).toArray(Long[]::new);
//
//        return tickBoxGridRepository.getResponseIdsByGroupedResponse(formId, questionId, rowIdResponse, columnIdsResponse, pageable);

        return null;
    }

    @Override
    public TickBoxGridResponseBatch mapToBatchResponse(List<FormResponseInfoQuestionResponse<TickBoxGridResponsePutReqDto>> questionResponsePutReqs) {
        var batch = new TickBoxGridResponseBatch();

        var responseMap = new HashMap<String, ArrayList<UUID>>();

        questionResponsePutReqs.forEach(q ->
                q.getQuestionResponsePutReq().getRows().forEach(row ->
                        row.getResponseColumnIds().forEach(columnId -> {
                            var rowColumnString = String.format(
                                    "%d_%d",
                                    row.getRowId(),
                                    columnId
                            );
                            responseMap
                                    .computeIfAbsent(rowColumnString, _ -> new ArrayList<>())
                                    .add(q.getFormResponseId());

                        })
                )
        );

        var responses = responseMap.entrySet()
                .stream()
                .map(entry -> {
                    var res = new TickBoxGridResponseBatch.Response();

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
        return QuestionType.TICK_BOX_GRID;
    }

}
