package com.sougata.form_data_service.service.responseManager;

import com.sougata.form_data_service.model.AnyTypeQuestionResponse;
import com.sougata.form_data_service.model.TickBoxGrid;
import com.sougata.form_data_service.repository.TickBoxGridRepository;
import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.formResponse.individual.TickBoxGridResponseIndividualDto;
import com.sougata.form_engine.dto.question.responseputrequest.TickBoxGridResponsePutReqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service("TICK_BOX_GRID_RESPONSE_MANAGER")
public class TickBoxGridManager extends ResponseManager<TickBoxGridResponsePutReqDto, TickBoxGridResponseIndividualDto> {

    private final TickBoxGridRepository tickBoxGridRepository;

    @Autowired
    public TickBoxGridManager(TickBoxGridRepository tickBoxGridRepository) {
        this.tickBoxGridRepository = tickBoxGridRepository;
    }

    @Override
    @Async
    public CompletableFuture<Void> create(TickBoxGridResponsePutReqDto response, UUID formResponseId) {
        TickBoxGrid tickBoxGrid = new TickBoxGrid();

        var key = new AnyTypeQuestionResponse.PartitionKey();
        key.setQuestionId(response.getQuestionId());
        key.setFormResponseId(formResponseId);

        tickBoxGrid.setKey(key);
        tickBoxGrid.setResponses(
                response.getRows().stream().collect(
                        Collectors.toMap(
                                TickBoxGridResponsePutReqDto.Row::getRowId, TickBoxGridResponsePutReqDto.Row::getResponseColumnIds
                        )
                )
        );

        tickBoxGridRepository.save(tickBoxGrid);

        return CompletableFuture.completedFuture(null);
    }

    @Override
    @Async
    public CompletableFuture<List<TickBoxGridResponseIndividualDto>> getResponses(UUID formResponseId, List<Long> questionIds) {
        var responses = tickBoxGridRepository.findAllByQuestionIdsAndFormResponseId(questionIds, formResponseId);

        return CompletableFuture.completedFuture(
                responses.stream().map(res -> {
                    var tbg = new TickBoxGridResponseIndividualDto();

                    tbg.setQuestionId(res.getKey().getQuestionId());
                    tbg.setQuestionType(getQuestionType());
                    tbg.setRows(
                            res.getResponses().entrySet()
                                    .stream()
                                    .map(entry ->
                                            new TickBoxGridResponseIndividualDto.Row(
                                                    entry.getKey(), entry.getValue().stream().map(Objects::toString).collect(Collectors.toSet())
                                            )
                                    ).toList()
                    );

                    return tbg;
                }).toList()
        );
    }

    @Override
    @Async
    public CompletableFuture<Void> deleteAllByQuestionIdsAndFormResponseId(List<Long> questionIds, UUID formResponseId) {
        tickBoxGridRepository.deleteAllByQuestionIdsAndFormResponseId(questionIds, formResponseId);

        return CompletableFuture.completedFuture(null);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.TICK_BOX_GRID;
    }

}
