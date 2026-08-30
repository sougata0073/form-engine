package com.sougata.form_data_service.formValidation.service.questionSchemaManager;

import com.sougata.form_data_service.formValidation.exception.ResponseValidationException;
import com.sougata.form_data_service.formValidation.service.QuestionSchemaManager;
import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.question.details.MultipleChoiceGridDetailsDto;
import com.sougata.form_engine.dto.question.responseputrequest.MultipleChoiceGridResponsePutReqDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

@Service("MULTIPLE_CHOICE_GRID_QUESTION_SCHEMA_MANAGER")
public class MultipleChoiceGridSchemaManager extends QuestionSchemaManager<MultipleChoiceGridDetailsDto, MultipleChoiceGridResponsePutReqDto> {

    @Override
    public boolean validateResponse(MultipleChoiceGridResponsePutReqDto validationDto, MultipleChoiceGridDetailsDto mcg) {
        if (validationDto.getRows().size() > mcg.getRows().size()) {
            throw new ResponseValidationException(
                    "The response contains more rows than are available. Available rows: "
                            + mcg.getRows().size()
                            + ", received: "
                            + validationDto.getRows().size() + "."
            );
        }

        if (mcg.getEachRowRequired() &&
                (validationDto.getRows().size() != mcg.getRows().size()
                        || validationDto.getRows().stream().anyMatch(r -> r.getResponseColumnId() == null))) {
            throw new ResponseValidationException(
                    "A response is required for every row. Expected responses for "
                            + mcg.getRows().size()
                            + " rows, but received "
                            + validationDto.getRows().size()
                            + ", or one or more rows have no selected column."
            );
        }

        var rowSet = new HashSet<>(mcg.getRows().stream().map(MultipleChoiceGridDetailsDto.Row::getId).toList());
        var columnSet = new HashSet<>(mcg.getColumns().stream().map(MultipleChoiceGridDetailsDto.Column::getId).toList());
        var invalidRows = new ArrayList<Long>();
        var invalidColumns = new HashMap<Long, ArrayList<Long>>();

        validationDto.getRows().forEach(r -> {
            if (!rowSet.contains(r.getRowId())) {
                invalidRows.add(r.getRowId());
            }
            if (!columnSet.contains(r.getResponseColumnId())) {
                if (invalidColumns.containsKey(r.getRowId())) {
                    invalidColumns.get(r.getRowId()).add(r.getResponseColumnId());
                } else {
                    var list = new ArrayList<Long>();
                    list.add(r.getResponseColumnId());
                    invalidColumns.put(r.getRowId(), list);
                }
            }
        });

        if (!invalidRows.isEmpty()) {
            throw new ResponseValidationException(
                    "The response contains invalid row IDs: " + invalidRows
            );
        }

        if (!invalidColumns.isEmpty()) {
            throw new ResponseValidationException(
                    "The response contains invalid column IDs. Invalid columns by row: " + invalidColumns
            );
        }

        return true;

    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.MULTIPLE_CHOICE_GRID;
    }
}
