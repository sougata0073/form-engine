package com.sougata.form_data_service.service.responseManager;

import com.sougata.form_data_service.model.FormResponse;
import com.sougata.form_data_service.model.TickBoxGrid;
import com.sougata.form_data_service.model.TickBoxGridColumn;
import com.sougata.form_data_service.model.TickBoxGridRow;
import com.sougata.form_data_service.repository.QuestionResponseRepository;
import com.sougata.form_data_service.repository.TickBoxGridRepository;
import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.question.responseRequest.TickBoxGridResponsePutReqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

@Service("TICK_BOX_GRID_RESPONSE_MANAGER")
public class TickBoxGridManager extends ResponseManager<TickBoxGridResponsePutReqDto> {

    private final TickBoxGridRepository tickBoxGridRepository;

    @Autowired
    public TickBoxGridManager(TickBoxGridRepository tickBoxGridRepository, QuestionResponseRepository questionResponseRepository) {
        super(questionResponseRepository);
        this.tickBoxGridRepository = tickBoxGridRepository;
    }

    @Override
    @Transactional
    public void create(TickBoxGridResponsePutReqDto response, FormResponse formResponse) {
        TickBoxGrid tickBoxGrid = new TickBoxGrid();

        var qr = createQuestionResponse(response.getQuestionId(), formResponse);

        var responses = response.getRows().stream().map(r -> {
            var row = new TickBoxGridRow();

            var columns = r.getResponseColumnIds().stream().map(c -> {
                var column = new TickBoxGridColumn();

                column.setResponseOptionId(c);
                column.setTickBoxGridRow(row);

                return column;
            }).collect(Collectors.toCollection(ArrayList::new));

            row.setRowId(r.getRowId());
            row.setResponses(columns);
            row.setTickBoxGrid(tickBoxGrid);

            return row;
        }).collect(Collectors.toCollection(ArrayList::new));

        tickBoxGrid.setQuestionResponse(qr);
        tickBoxGrid.setResponses(responses);

        tickBoxGridRepository.save(tickBoxGrid);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.TICK_BOX_GRID;
    }

    @Override
    public void deleteResponsesByQuestion(UUID formId, Long questionId) {
        tickBoxGridRepository.deleteAllByFormIdAndQuestionId(formId, questionId);
    }

    @Override
    public void deleteResponsesByFormResponse(UUID formId, Long formResponseId) {
        tickBoxGridRepository.deleteAllByFormIdAndFormResponseId(formId, formResponseId);
    }
}
