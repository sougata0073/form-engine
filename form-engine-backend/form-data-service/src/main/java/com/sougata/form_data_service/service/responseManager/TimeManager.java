package com.sougata.form_data_service.service.responseManager;

import com.sougata.form_data_service.model.AnyTypeQuestionResponse;
import com.sougata.form_data_service.model.Time;
import com.sougata.form_data_service.repository.TimeRepository;
import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.formResponse.individual.TimeResponseIndividualDto;
import com.sougata.form_engine.dto.question.responseputrequest.TimeResponsePutReqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service("TIME_RESPONSE_MANAGER")
public class TimeManager extends ResponseManager<TimeResponsePutReqDto, TimeResponseIndividualDto> {

    private final TimeRepository timeRepository;

    @Autowired
    public TimeManager(TimeRepository timeRepository) {
        this.timeRepository = timeRepository;
    }

    @Override
    @Async
    public CompletableFuture<Void> create(TimeResponsePutReqDto response, UUID formResponseId) {
        Time time = new Time();

        var key = new AnyTypeQuestionResponse.PartitionKey();
        key.setQuestionId(response.getQuestionId());
        key.setFormResponseId(formResponseId);

        time.setKey(key);
        time.setTime(response.getTime());

        timeRepository.save(time);

        return CompletableFuture.completedFuture(null);
    }

    @Override
    @Async
    public CompletableFuture<List<TimeResponseIndividualDto>> getResponses(UUID formResponseId, List<Long> questionIds) {
        var responses = timeRepository.findAllByQuestionIdsAndFormResponseId(questionIds, formResponseId);

        return CompletableFuture.completedFuture(
                responses.stream().map(res -> {
                    var t = new TimeResponseIndividualDto();

                    t.setQuestionId(res.getKey().getQuestionId());
                    t.setQuestionType(getQuestionType());
                    t.setTime(res.getTime());

                    return t;
                }).toList()
        );
    }

    @Override
    @Async
    public CompletableFuture<Void> deleteAllByQuestionIdsAndFormResponseId(List<Long> questionIds, UUID formResponseId) {
        timeRepository.deleteAllByQuestionIdsAndFormResponseId(questionIds, formResponseId);

        return CompletableFuture.completedFuture(null);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.TIME;
    }

}
