package com.sougata.form_data_service.formValidation.service.questionSchemaManager;

import com.sougata.form_data_service.formValidation.exception.ResponseValidationException;
import com.sougata.form_data_service.formValidation.service.QuestionSchemaManager;
import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.question.details.TickBoxGridDetailsDto;
import com.sougata.form_engine.dto.question.responseRequest.TickBoxGridResponsePutReqDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

@Service("TICK_BOX_GRID_QUESTION_SCHEMA_MANAGER")
public class TickBoxGridSchemaManager extends QuestionSchemaManager<TickBoxGridDetailsDto, TickBoxGridResponsePutReqDto> {

    @Override
    public boolean validateResponse(TickBoxGridResponsePutReqDto validationDto, TickBoxGridDetailsDto tbg) {
        if (validationDto.getRows().size() > tbg.getRows().size()) {
            throw new ResponseValidationException(
                    "The number of response rows cannot exceed the number of available rows. Available rows: "
                            + tbg.getRows().size()
                            + ", received: "
                            + validationDto.getRows().size()
            );
        }

        var validRowIds = new HashSet<>(tbg.getRows().stream()
                .map(TickBoxGridDetailsDto.Row::getId)
                .toList());

        var validColumnIds = new HashSet<>(tbg.getColumns().stream()
                .map(TickBoxGridDetailsDto.Column::getId)
                .toList());

        var invalidRowIds = new ArrayList<Long>();
        var invalidColumnIds = new HashMap<Long, ArrayList<Long>>();

        validationDto.getRows().forEach(r -> {
            if (!validRowIds.contains(r.getRowId())) {
                invalidRowIds.add(r.getRowId());
                return;
            }

            r.getResponseColumnIds().forEach(columnId -> {
                if (columnId == null || !validColumnIds.contains(columnId)) {
                    invalidColumnIds
                            .computeIfAbsent(r.getRowId(), k -> new ArrayList<>())
                            .add(columnId);
                }
            });
        });

        if (!invalidRowIds.isEmpty()) {
            throw new ResponseValidationException(
                    "The following row IDs are not valid: " + invalidRowIds
            );
        }

        if (!invalidColumnIds.isEmpty()) {
            throw new ResponseValidationException(
                    "The response contains invalid column IDs. Invalid columns by row: " + invalidColumnIds
            );
        }

        if (tbg.getEachRowRequired()) {

            if (validationDto.getRows().size() != tbg.getRows().size()) {
                throw new ResponseValidationException(
                        "A response is required for every row. Expected responses for "
                                + tbg.getRows().size()
                                + " rows, but received "
                                + validationDto.getRows().size() + "."
                );
            }

            var rowsWithoutSelection = validationDto.getRows().stream()
                    .filter(r -> r.getResponseColumnIds().isEmpty())
                    .map(TickBoxGridResponsePutReqDto.Row::getRowId)
                    .toList();

            if (!rowsWithoutSelection.isEmpty()) {
                throw new ResponseValidationException(
                        "The following row IDs must contain at least one selected column: "
                                + rowsWithoutSelection
                );
            }
        }

        return true;
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.TICK_BOX_GRID;
    }

}
