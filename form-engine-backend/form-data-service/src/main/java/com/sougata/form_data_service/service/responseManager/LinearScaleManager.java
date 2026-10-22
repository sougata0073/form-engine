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
    public CompletableFuture<Void> create(LinearScaleResponsePutReqDto response, UUID formResponseId) {
        LinearScale linearScale = new LinearScale();

        var key = new AnyTypeQuestionResponse.PartitionKey();
        key.setQuestionId(response.getQuestionId());
        key.setFormResponseId(formResponseId);

        linearScale.setKey(key);
        linearScale.setScale(response.getScale());

        linearScaleRepository.save(linearScale);

        return CompletableFuture.completedFuture(null);
    }

    @Override
    @Async
    public CompletableFuture<List<LinearScaleResponseIndividualDto>> getResponses(UUID formResponseId, List<Long> questionIds) {
        var responses = linearScaleRepository.findAllByQuestionIdsAndFormResponseId(questionIds, formResponseId);

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
    public CompletableFuture<Void> deleteAllByQuestionIdsAndFormResponseId(List<Long> questionIds, UUID formResponseId) {
        linearScaleRepository.deleteAllByQuestionIdsAndFormResponseId(questionIds, formResponseId);

        return CompletableFuture.completedFuture(null);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.LINEAR_SCALE;
    }

}
