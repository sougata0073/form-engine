package com.sougata.form_data_service.service.responseManager;

import com.sougata.form_data_service.model.AnyTypeQuestionResponse;
import com.sougata.form_data_service.model.MultipleChoiceGrid;
import com.sougata.form_data_service.repository.MultipleChoiceGridRepository;
import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.formResponse.individual.MultipleChoiceGridResponseIndividualDto;
import com.sougata.form_engine.dto.question.responseputrequest.MultipleChoiceGridResponsePutReqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service("MULTIPLE_CHOICE_GRID_RESPONSE_MANAGER")
public class MultipleChoiceGridManager extends ResponseManager<MultipleChoiceGridResponsePutReqDto, MultipleChoiceGridResponseIndividualDto> {

    private final MultipleChoiceGridRepository multipleChoiceGridRepository;

    @Autowired
    public MultipleChoiceGridManager(MultipleChoiceGridRepository multipleChoiceGridRepository) {
        this.multipleChoiceGridRepository = multipleChoiceGridRepository;
    }

    @Override
    @Async
    public CompletableFuture<Void> create(UUID formId, UUID formResponseId, MultipleChoiceGridResponsePutReqDto response) {
        MultipleChoiceGrid multipleChoiceGrid = new MultipleChoiceGrid();

        var key = new AnyTypeQuestionResponse.PartitionKey(
                formId,
                formResponseId,
                response.getQuestionId()
        );

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
    public CompletableFuture<List<MultipleChoiceGridResponseIndividualDto>> getResponses(UUID formId, UUID formResponseId, Set<Long> questionIds) {
        var responses = multipleChoiceGridRepository.findAllByQuestionIds(formId, formResponseId, questionIds);

        return CompletableFuture.completedFuture(
                responses.stream().map(res -> {
                    var mcg = new MultipleChoiceGridResponseIndividualDto();

                    mcg.setQuestionId(res.getKey().getQuestionId());
                    mcg.setQuestionType(getQuestionType());
                    mcg.setRows(
                            res.getResponses().entrySet()
                                    .stream()
                                    .map(entry ->
                                            new MultipleChoiceGridResponseIndividualDto.Row(
                                                    entry.getKey(), entry.getValue()
                                            )
                                    ).toList()
                    );

                    return mcg;
                }).toList()
        );
    }

    @Override
    @Async
    public CompletableFuture<Void> deleteAllByQuestionIds(UUID formId, UUID formResponseId, Set<Long> questionIds) {
        multipleChoiceGridRepository.deleteAllByQuestionIds(formId, formResponseId, questionIds);

        return CompletableFuture.completedFuture(null);
    }

    @Override
    @Async
    public CompletableFuture<Void> deleteAllByFormId(UUID formId) {
        multipleChoiceGridRepository.deleteAllByFormId(formId);

        return CompletableFuture.completedFuture(null);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.MULTIPLE_CHOICE_GRID;
    }

}
