package com.sougata.form_data_service.service.responseManager;

import com.sougata.form_data_service.model.FormResponse;
import com.sougata.form_data_service.model.MultipleChoiceGrid;
import com.sougata.form_data_service.model.MultipleChoiceGridRow;
import com.sougata.form_data_service.repository.MultipleChoiceGridRepository;
import com.sougata.form_data_service.repository.QuestionResponseRepository;
import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.question.responseputrequest.MultipleChoiceGridResponsePutReqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

@Service("MULTIPLE_CHOICE_GRID_RESPONSE_MANAGER")
public class MultipleChoiceGridManager extends ResponseManager<MultipleChoiceGridResponsePutReqDto> {

    private final MultipleChoiceGridRepository multipleChoiceGridRepository;

    @Autowired
    public MultipleChoiceGridManager(MultipleChoiceGridRepository multipleChoiceGridRepository, QuestionResponseRepository questionResponseRepository) {
        super(questionResponseRepository);
        this.multipleChoiceGridRepository = multipleChoiceGridRepository;
    }

    @Override
    @Transactional
    public void create(MultipleChoiceGridResponsePutReqDto response, FormResponse formResponse) {
        MultipleChoiceGrid multipleChoiceGrid = new MultipleChoiceGrid();

        var qr = createQuestionResponse(response.getQuestionId(), formResponse);

        var responses = response.getRows().stream().map(r -> {
            var row = new MultipleChoiceGridRow();

            row.setRowId(r.getRowId());
            row.setResponseColumnId(r.getResponseColumnId());
            row.setMultipleChoiceGrid(multipleChoiceGrid);

            return row;
        }).collect(Collectors.toCollection(ArrayList::new));

        multipleChoiceGrid.setQuestionResponse(qr);
        multipleChoiceGrid.setResponses(responses);

        multipleChoiceGridRepository.save(multipleChoiceGrid);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.MULTIPLE_CHOICE_GRID;
    }

    @Override
    public void deleteResponsesByQuestion(UUID formId, Long questionId) {
        multipleChoiceGridRepository.deleteAllByFormIdAndQuestionId(formId, questionId);
    }

    @Override
    public void deleteResponsesByFormResponse(UUID formId, Long formResponseId) {
        multipleChoiceGridRepository.deleteAllByFormIdAndFormResponseId(formId, formResponseId);
    }
}
