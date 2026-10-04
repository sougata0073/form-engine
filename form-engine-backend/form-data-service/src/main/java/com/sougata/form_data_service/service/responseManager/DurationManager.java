package com.sougata.form_data_service.service.responseManager;

import com.sougata.form_data_service.model.AnyTypeQuestionResponse;
import com.sougata.form_data_service.model.Duration;
import com.sougata.form_data_service.repository.DurationRepository;
import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.question.responseputrequest.DurationResponsePutReqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service("DURATION_RESPONSE_MANAGER")
public class DurationManager extends ResponseManager<DurationResponsePutReqDto> {

    private final DurationRepository durationRepository;

    @Autowired
    public DurationManager(DurationRepository durationRepository) {
        this.durationRepository = durationRepository;
    }

    @Override
    @Async
    public CompletableFuture<Void> create(DurationResponsePutReqDto response, UUID formResponseId) {
        Duration duration = new Duration();

        var key = new AnyTypeQuestionResponse.PartitionKey();
        key.setQuestionId(response.getQuestionId());
        key.setFormResponseId(formResponseId);

        duration.setKey(key);
        duration.setHours(response.getHours());
        duration.setMinutes(response.getMinutes());
        duration.setSeconds(response.getSeconds());

        durationRepository.save(duration);

        return CompletableFuture.completedFuture(null);
    }

    @Override
    @Async
    public CompletableFuture<Void> deleteAllByQuestionIdsAndFormResponseId(List<Long> questionIds, UUID formResponseId) {
        durationRepository.deleteAllByQuestionIdsAndFormResponseId(questionIds, formResponseId);

        return CompletableFuture.completedFuture(null);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.DURATION;
    }

}
