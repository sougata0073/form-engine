package com.sougata.form_data_service.service.responseManager;

import com.sougata.form_data_service.model.AnyTypeQuestionResponse;
import com.sougata.form_data_service.model.MultipleChoiceGrid;
import com.sougata.form_data_service.repository.MultipleChoiceGridRepository;
import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.question.responseputrequest.MultipleChoiceGridResponsePutReqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service("MULTIPLE_CHOICE_GRID_RESPONSE_MANAGER")
public class MultipleChoiceGridManager extends ResponseManager<MultipleChoiceGridResponsePutReqDto> {

    private final MultipleChoiceGridRepository multipleChoiceGridRepository;

    @Autowired
    public MultipleChoiceGridManager(MultipleChoiceGridRepository multipleChoiceGridRepository) {
        this.multipleChoiceGridRepository = multipleChoiceGridRepository;
    }

    @Override
    @Async
    public CompletableFuture<Void> create(MultipleChoiceGridResponsePutReqDto response, UUID formResponseId) {
        MultipleChoiceGrid multipleChoiceGrid = new MultipleChoiceGrid();

        var key = new AnyTypeQuestionResponse.PartitionKey();
        key.setQuestionId(response.getQuestionId());
        key.setFormResponseId(formResponseId);

        multipleChoiceGrid.setKey(key);
        multipleChoiceGrid.setResponses(
                response.getRows()
                        .stream()
                        .collect(
                                Collectors.toMap(
                                        MultipleChoiceGridResponsePutReqDto.Row::getRowId,
                                        MultipleChoiceGridResponsePutReqDto.Row::getResponseColumnId
                                )
                        )
        );

        multipleChoiceGridRepository.save(multipleChoiceGrid);

        return CompletableFuture.completedFuture(null);
    }

    @Override
    @Async
    public CompletableFuture<Void> deleteAllByQuestionIdsAndFormResponseId(List<Long> questionIds, UUID formResponseId) {
        multipleChoiceGridRepository.deleteAllByQuestionIdsAndFormResponseId(questionIds, formResponseId);

        return CompletableFuture.completedFuture(null);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.MULTIPLE_CHOICE_GRID;
    }

}
