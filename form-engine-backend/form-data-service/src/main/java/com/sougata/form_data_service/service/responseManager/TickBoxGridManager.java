package com.sougata.form_data_service.service.responseManager;

import com.sougata.form_data_service.model.AnyTypeQuestionResponse;
import com.sougata.form_data_service.model.FormResponse;
import com.sougata.form_data_service.model.TickBoxGrid;
import com.sougata.form_data_service.repository.QuestionResponseRepository;
import com.sougata.form_data_service.repository.TickBoxGridRepository;
import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.question.responseputrequest.TickBoxGridResponsePutReqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public void create(TickBoxGridResponsePutReqDto response, FormResponse formResponse) {
        TickBoxGrid tickBoxGrid = new TickBoxGrid();

        var qr = createQuestionResponse(response.getQuestionId(), formResponse);

        tickBoxGrid.setKey(new AnyTypeQuestionResponse.PartitionKey(response.getQuestionId(), qr.getKey().getQuestionResponseId()));
        tickBoxGrid.setResponses(
                response.getRows().stream().collect(
                        Collectors.toMap(
                                TickBoxGridResponsePutReqDto.Row::getRowId, TickBoxGridResponsePutReqDto.Row::getResponseColumnIds
                        )
                )
        );

        tickBoxGridRepository.save(tickBoxGrid);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.TICK_BOX_GRID;
    }

    @Override
    public void deleteResponsesByQuestionId(Long questionId) {
        tickBoxGridRepository.deleteAllByQuestionId(questionId);
    }

    @Override
    public void deleteResponsesByQuestionIdAndQuestionResponseId(Long questionId, Long questionResponseId) {
        tickBoxGridRepository.deleteAllByQuestionIdAndQuestionResponseId(questionId, questionResponseId);
    }

}
