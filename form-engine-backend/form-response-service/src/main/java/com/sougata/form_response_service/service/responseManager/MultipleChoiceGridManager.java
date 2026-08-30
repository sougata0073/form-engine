package com.sougata.form_response_service.service.responseManager;

import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.formResponse.individual.MultipleChoiceGridResponseIndividualDto;
import com.sougata.form_engine.dto.formResponse.question.MultipleChoiceGridResponseQuestionDto;
import com.sougata.form_engine.dto.formResponse.summary.MultipleChoiceGridResponseSummaryDto;
import com.sougata.form_engine.dto.question.details.MultipleChoiceGridDetailsDto;
import com.sougata.form_engine.util.IdUtil;
import com.sougata.form_response_service.projection.CommonResponseSummaryProjection;
import com.sougata.form_response_service.repository.MultipleChoiceGridRepository;
import jakarta.persistence.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service("MULTIPLE_CHOICE_GRID_RESPONSE_MANAGER")
public class MultipleChoiceGridManager extends ResponseManager<
        MultipleChoiceGridResponseSummaryDto,
        MultipleChoiceGridDetailsDto,
        MultipleChoiceGridResponseQuestionDto,
        MultipleChoiceGridResponseQuestionDto.Response,
        MultipleChoiceGridResponseIndividualDto
        > {

    private final MultipleChoiceGridRepository multipleChoiceGridRepository;

    @Autowired
    public MultipleChoiceGridManager(MultipleChoiceGridRepository multipleChoiceGridRepository) {
        this.multipleChoiceGridRepository = multipleChoiceGridRepository;
    }

    @Override
    public List<MultipleChoiceGridResponseSummaryDto> getResponseSummaries(
            UUID formId,
            List<MultipleChoiceGridDetailsDto> questionResponses) {

        var responseSummaries = multipleChoiceGridRepository.getResponseSummaries(formId);

        var summaryMap = responseSummaries.stream()
                .collect(Collectors.toMap(
                        CommonResponseSummaryProjection::questionId,
                        Function.identity()
                ));

        var groupedResponses = multipleChoiceGridRepository.getResponseOptionCount(formId)
                .stream()
                .collect(Collectors.groupingBy(
                        t -> t.get("questionId", Long.class),
                        Collectors.groupingBy(
                                t -> t.get("rowId", Long.class)
                        )
                ));

        var result = new ArrayList<MultipleChoiceGridResponseSummaryDto>();

        for (var qr : questionResponses) {

            var dto = new MultipleChoiceGridResponseSummaryDto();

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

                        Map<Long, Long> columnCountMap = rowTuples.stream()
                                .collect(Collectors.toMap(
                                        t -> t.get("responseColumnId", Long.class),
                                        t -> t.get("responseCount", Long.class)
                                ));

                        var columnResponses = qr.getColumns().stream()
                                .map(column ->
                                        new MultipleChoiceGridResponseSummaryDto.ColumnResponse(
                                                column.getId(),
                                                column.getColumn(),
                                                columnCountMap.getOrDefault(column.getId(), 0L)
                                        )
                                )
                                .toList();

                        return new MultipleChoiceGridResponseSummaryDto.RowResponse(
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
    public MultipleChoiceGridResponseSummaryDto getResponseSummary(UUID formId, Long questionId, MultipleChoiceGridDetailsDto questionRes, Pageable pageable) {
        var responseSummary = multipleChoiceGridRepository.getResponseSummary(formId, questionId);
        var res = new MultipleChoiceGridResponseSummaryDto();

        res.setQuestionId(questionRes.getId());
        res.setQuestion(questionRes.getQuestion());
        res.setQuestionType(getQuestionType());
        res.setOrderIndex(questionRes.getOrderIndex());
        res.setNumberOfResponses(responseSummary.numberOfResponses());
        res.setResponses(List.of());

        return res;
    }

    @Override
    public MultipleChoiceGridResponseQuestionDto getResponseByQuestion(UUID formId, Long questionId, Map<String, String> extraParams, Pageable pageable) {

        var rowIdString = extraParams.get("rowId");

        if (rowIdString == null) {
            throw new IllegalArgumentException("Row ID is required");
        }

        long rowId;

        try {
            rowId = Long.parseLong(rowIdString);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid row ID: " + rowIdString);
        }

        var grouped = multipleChoiceGridRepository.groupedByResponseRowColumn(
                formId,
                questionId,
                rowId,
                pageable
        );

        var mc = new MultipleChoiceGridResponseQuestionDto();

        var responses = grouped.stream().map(g -> {
            var res = new MultipleChoiceGridResponseQuestionDto.Response();

            res.setQuestionId(questionId);
            res.setQuestionType(getQuestionType());
            res.setColumnId(g.get("columnId", Long.class));
            res.setResponseCount(g.get("responseCount", Long.class));

            var map = new HashMap<String, List<String>>();

            map.put("rowId", List.of(rowIdString));
            map.put("columnId", List.of(res.getColumnId() == null ? "" : res.getColumnId().toString()));

            res.setFormResponsesIdentifier(IdUtil.generateCompressedEncodedId(map));

            return res;
        }).toList();

        mc.setQuestionId(questionId);
        mc.setQuestionType(getQuestionType());
        mc.setRowId(rowId);
        mc.setResponses(responses);

        return mc;
    }

    @Override
    public List<MultipleChoiceGridResponseIndividualDto> getIndividualResponses(UUID formId, Long formResponseId) {
        var responses = multipleChoiceGridRepository.getRowColumnIdsByFormResponse(formResponseId);

        return responses.stream().map(tuple -> {
            var qId = tuple.get("questionId", Long.class);
            var rowIds = tuple.get("rowIds", Long[].class);
            var columnIds = tuple.get("columnIds", Long[].class);

            var res = new MultipleChoiceGridResponseIndividualDto();

            res.setQuestionId(qId);
            res.setQuestionType(getQuestionType());

            var rows = new ArrayList<MultipleChoiceGridResponseIndividualDto.Row>();

            for (int i = 0; i < rowIds.length; i++) {
                rows.add(
                        new MultipleChoiceGridResponseIndividualDto.Row(
                                rowIds[i], columnIds[i]
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
        var columnId = map.get("columnId");

        if (rowId.isEmpty() || columnId.isEmpty()) {
            throw new IllegalArgumentException("Invalid Form Responses Identifier. Identifier: " + formResponsesIdentifier);
        }

        var rowIdResponse = rowId.getFirst() == null ? null :  Long.parseLong(rowId.getFirst());
        var columnIdResponse = columnId.getFirst() == null ? null : Long.parseLong(columnId.getFirst());

        return multipleChoiceGridRepository.getResponseIdsByGroupedResponse(formId, questionId, rowIdResponse, columnIdResponse, pageable);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.MULTIPLE_CHOICE_GRID;
    }

}
