package com.sougata.form_data_service.service.responseManager;

import com.sougata.form_data_service.model.AnyTypeQuestionResponse;
import com.sougata.form_data_service.model.Duration;
import com.sougata.form_data_service.repository.DurationRepository;
import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.formResponse.individual.DurationResponseIndividualDto;
import com.sougata.form_engine.dto.question.responseputrequest.DurationResponsePutReqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service("DURATION_RESPONSE_MANAGER")
public class DurationManager extends ResponseManager<DurationResponsePutReqDto, DurationResponseIndividualDto> {

    private final DurationRepository durationRepository;

    @Autowired
    public DurationManager(DurationRepository durationRepository) {
        this.durationRepository = durationRepository;
    }

    @Override
    @Async
    public CompletableFuture<Void> create(UUID formId, UUID formResponseId, DurationResponsePutReqDto response) {
        Duration duration = new Duration();

        var key = new AnyTypeQuestionResponse.PartitionKey(
                formId,
                formResponseId,
                response.getQuestionId()
        );

        duration.setKey(key);
        duration.setHours(response.getHours());
        duration.setMinutes(response.getMinutes());
        duration.setSeconds(response.getSeconds());

        durationRepository.save(duration);

        return CompletableFuture.completedFuture(null);
    }

    @Override
    @Async
    public CompletableFuture<List<DurationResponseIndividualDto>> getResponses(UUID formId, UUID formResponseId, Set<Long> questionIds) {
        var responses = durationRepository.findAllByQuestionIds(formId, formResponseId, questionIds);

        return CompletableFuture.completedFuture(
                responses.stream().map(res -> {
                    var dr = new DurationResponseIndividualDto();

                    dr.setQuestionId(res.getKey().getQuestionId());
                    dr.setQuestionType(getQuestionType());
                    dr.setHours(res.getHours());
                    dr.setMinutes(res.getMinutes());
                    dr.setSeconds(res.getSeconds());

                    return dr;
                }).toList()
        );
    }

    @Override
    @Async
    public CompletableFuture<Void> deleteAllByQuestionIds(UUID formId, UUID formResponseId, Set<Long> questionIds) {
        durationRepository.deleteAllByQuestionIds(formId, formResponseId, questionIds);

        return CompletableFuture.completedFuture(null);
    }

    @Override
    @Async
    public CompletableFuture<Void> deleteAllByFormId(UUID formId) {
        durationRepository.deleteAllByFormId(formId);

        return CompletableFuture.completedFuture(null);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.DURATION;
    }

}
