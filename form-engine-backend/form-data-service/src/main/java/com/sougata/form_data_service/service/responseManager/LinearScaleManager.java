package com.sougata.form_data_service.service.responseManager;

import com.sougata.form_data_service.model.AnyTypeQuestionResponse;
import com.sougata.form_data_service.model.LinearScale;
import com.sougata.form_data_service.repository.LinearScaleRepository;
import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.formResponse.individual.LinearScaleResponseIndividualDto;
import com.sougata.form_engine.dto.question.responseputrequest.LinearScaleResponsePutReqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service("LINEAR_SCALE_RESPONSE_MANAGER")
public class LinearScaleManager extends ResponseManager<LinearScaleResponsePutReqDto, LinearScaleResponseIndividualDto> {

    private final LinearScaleRepository linearScaleRepository;

    @Autowired
    public LinearScaleManager(LinearScaleRepository linearScaleRepository) {
        this.linearScaleRepository = linearScaleRepository;
    }

    @Override
    @Async
    public CompletableFuture<Void> create(UUID formId, UUID formResponseId, LinearScaleResponsePutReqDto response) {
        LinearScale linearScale = new LinearScale();

        var key = new AnyTypeQuestionResponse.PartitionKey(
                formId,
                formResponseId,
                response.getQuestionId()
        );

        linearScale.setKey(key);
        linearScale.setScale(response.getScale());

        linearScaleRepository.save(linearScale);

        return CompletableFuture.completedFuture(null);
    }

    @Override
    @Async
    public CompletableFuture<List<LinearScaleResponseIndividualDto>> getResponses(UUID formId, UUID formResponseId, Set<Long> questionIds) {
        var responses = linearScaleRepository.findAllByQuestionIds(formId, formResponseId, questionIds);

        return CompletableFuture.completedFuture(
                responses.stream().map(res -> {
                    var ls = new LinearScaleResponseIndividualDto();

                    ls.setQuestionId(res.getKey().getQuestionId());
                    ls.setQuestionType(getQuestionType());
                    ls.setScale(res.getScale());

                    return ls;
                }).toList()
        );
    }

    @Override
    @Async
    public CompletableFuture<Void> deleteAllByQuestionIds(UUID formId, UUID formResponseId, Set<Long> questionIds) {
        linearScaleRepository.deleteAllByQuestionIds(formId, formResponseId, questionIds);

        return CompletableFuture.completedFuture(null);
    }

    @Override
    @Async
    public CompletableFuture<Void> deleteAllByFormId(UUID formId) {
        linearScaleRepository.deleteAllByFormId(formId);

        return CompletableFuture.completedFuture(null);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.LINEAR_SCALE;
    }

}
