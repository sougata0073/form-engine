package com.sougata.form_response_service.service.responseManager;

import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.formResponse.individual.TickBoxGridResponseIndividualDto;
import com.sougata.form_engine.dto.formResponse.question.TickBoxGridResponseQuestionDto;
import com.sougata.form_engine.dto.formResponse.summary.TickBoxGridResponseSummaryDto;
import com.sougata.form_engine.dto.question.details.TickBoxGridDetailsDto;
import com.sougata.form_engine.util.IdUtil;
import com.sougata.form_response_service.projection.CommonResponseSummaryProjection;
import com.sougata.form_response_service.repository.TickBoxGridRepository;
import jakarta.persistence.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service("TICK_BOX_GRID_RESPONSE_MANAGER")
public class TickBoxGridManager extends ResponseManager<
        TickBoxGridResponseSummaryDto,
        TickBoxGridDetailsDto,
        TickBoxGridResponseQuestionDto,
        TickBoxGridResponseQuestionDto.Response,
        TickBoxGridResponseIndividualDto
        > {

    private final TickBoxGridRepository tickBoxGridRepository;

    @Autowired
    public TickBoxGridManager(TickBoxGridRepository tickBoxGridRepository) {
        this.tickBoxGridRepository = tickBoxGridRepository;
    }

    @Override
    public List<TickBoxGridResponseSummaryDto> getResponseSummaries(
            UUID formId,
            List<TickBoxGridDetailsDto> questionResponses
    ) {

        var responseSummaries = tickBoxGridRepository.getResponseSummaries(formId);

        var summaryMap = responseSummaries.stream()
                .collect(Collectors.toMap(
                        CommonResponseSummaryProjection::questionId,
                        Function.identity()
                ));

        var groupedResponses = tickBoxGridRepository.getResponseOptionCount(formId)
                .stream()
                .collect(Collectors.groupingBy(
                        t -> t.get("questionId", Long.class),
                        Collectors.groupingBy(
                                t -> t.get("rowId", Long.class)
                        )
                ));

        var result = new ArrayList<TickBoxGridResponseSummaryDto>();

        for (var qr : questionResponses) {

            var dto = new TickBoxGridResponseSummaryDto();

            dto.setQuestionId(qr.getId());
            dto.setQuestion(qr.getQuestion());
            dto.setOrderIndex(qr.getOrderIndex());
            dto.setQuestionType(getQuestionType());

            var summary = summaryMap.get(qr.getId());
            dto.setNumberOfResponses(summary == null ? 0L : summary.numberOfResponses());

            var rowResponses = qr.getRows().stream()
                    .map(row -> {

                        var rowTuples = groupedResponses
                                .getOrDefault(qr.getId(), Collections.emptyMap())
                                .getOrDefault(row.getId(), Collections.emptyList());

                        Map<Long, Long> optionCountMap = rowTuples.stream()
                                .collect(Collectors.toMap(
                                        t -> t.get("responseOptionId", Long.class),
                                        t -> t.get("responseCount", Long.class)
                                ));

                        var columnResponses = qr.getColumns().stream()
                                .map(column ->
                                        new TickBoxGridResponseSummaryDto.ColumnResponse(
                                                column.getId(),
                                                column.getColumn(),
                                                optionCountMap.getOrDefault(column.getId(), 0L)
                                        )
                                )
                                .toList();

                        return new TickBoxGridResponseSummaryDto.RowResponse(
                                row.getId(),
                                row.getRow(),
                                columnResponses
                        );
                    })
                    .toList();

            dto.setResponses(rowResponses);

            result.add(dto);
        }

        return result;
    }

    @Override
    public TickBoxGridResponseSummaryDto getResponseSummary(UUID formId, Long questionId, TickBoxGridDetailsDto questionRes, Pageable pageable) {
        var responseSummary = tickBoxGridRepository.getResponseSummary(formId, questionId);
        var res = new TickBoxGridResponseSummaryDto();

        res.setQuestionId(questionRes.getId());
        res.setQuestion(questionRes.getQuestion());
        res.setQuestionType(getQuestionType());
        res.setOrderIndex(questionRes.getOrderIndex());
        res.setNumberOfResponses(responseSummary.numberOfResponses());
        res.setResponses(List.of());

        return res;
    }

    @Override
    public TickBoxGridResponseQuestionDto getResponseByQuestion(UUID formId, Long questionId, Map<String, String> extraParams, Pageable pageable) {

        var rowIdString = extraParams.get("rowId");

        if (rowIdString == null) {
            throw new IllegalArgumentException("Row ID is required");
        }

        var rowId = Long.parseLong(rowIdString);

        var grouped = tickBoxGridRepository.groupedByResponseRowColumn(
                questionId,
                rowId,
                pageable
        );

        var tb = new TickBoxGridResponseQuestionDto();

        var responses = grouped.stream().map(g -> {
            var res = new TickBoxGridResponseQuestionDto.Response();

            res.setQuestionId(questionId);
            res.setQuestionType(getQuestionType());
            res.setResponseCount(g.get("responseCount", Long.class));

            var colIdArray = g.get("columnIds", Long[].class);

            res.setColumnIds(colIdArray == null ? null : Arrays.stream(colIdArray).map(Object::toString).toList());

            var map = new HashMap<String, List<String>>();

            map.put("rowId", List.of(rowIdString));
            map.put("columnIds", res.getColumnIds() == null ? List.of() : res.getColumnIds());

            res.setFormResponsesIdentifier(IdUtil.generateCompressedEncodedId(map));

            return res;
        }).toList();

        tb.setQuestionId(questionId);
        tb.setQuestionType(getQuestionType());
        tb.setRowId(rowId);
        tb.setResponses(responses);

        return tb;
    }

    @Override
    public List<TickBoxGridResponseIndividualDto> getIndividualResponses(UUID formId, Long formResponseId) {
        var responses = tickBoxGridRepository.getRowColumnIdsByFormResponse(formResponseId);

        var questionIdMap = responses.stream().collect(
                Collectors.groupingBy(tuple -> tuple.get("questionId", Long.class))
        );

        return questionIdMap.entrySet().stream().map(entry -> {
            var qId = entry.getKey();
            var rowIds = entry.getValue().stream().map(tuple -> tuple.get("rowId", Long.class)).toList();
            var columnIds = entry.getValue().stream().map(tuple -> tuple.get("columnIds", Long[].class)).toList();

            var res = new TickBoxGridResponseIndividualDto();

            res.setQuestionId(qId);
            res.setQuestionType(getQuestionType());

            var rows = new ArrayList<TickBoxGridResponseIndividualDto.Row>();

            for (int i = 0; i < rowIds.size(); i++) {
                var rowId = rowIds.get(i);
                var colIds = columnIds.get(i);
                rows.add(
                        new TickBoxGridResponseIndividualDto.Row(
                                rowId, Arrays.stream(colIds).map(Object::toString).toList()
                        )
                );
            }

            res.setRows(rows);

            return res;
        }).toList();
    }

    @Override
    public List<Tuple> getFormResponseAndUserIds(UUID formId, Long questionId, String formResponsesIdentifier, Pageable pageable) {
        var map = IdUtil.reconstructCompressedEncodedId(formResponsesIdentifier);

        var rowId = map.get("rowId");
        var columnIds = map.get("columnIds");

        if (rowId.isEmpty() || columnIds.isEmpty()) {
            throw new IllegalArgumentException("Invalid Form Responses Identifier. Identifier: " + formResponsesIdentifier);
        }

        var rowIdResponse = rowId.getFirst() == null ? null : Long.parseLong(rowId.getFirst());
        var firstColumnId = columnIds.getFirst();

        var columnIdsResponse = firstColumnId == null ? new Long[]{null} : columnIds.stream().map(Long::parseLong).toArray(Long[]::new);

        return tickBoxGridRepository.getResponseIdsByGroupedResponse(questionId, rowIdResponse, columnIdsResponse, pageable);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.TICK_BOX_GRID;
    }

}
