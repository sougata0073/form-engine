package com.sougata.form_data_service.service.responseManager;

import com.sougata.form_data_service.model.AnyTypeQuestionResponse;
import com.sougata.form_data_service.model.DateTime;
import com.sougata.form_data_service.repository.DateTimeRepository;
import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.question.responseputrequest.DateTimeResponsePutReqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service("DATE_TIME_RESPONSE_MANAGER")
public class DateTimeManager extends ResponseManager<DateTimeResponsePutReqDto> {

    private final DateTimeRepository dateTimeRepository;

    @Autowired
    public DateTimeManager(DateTimeRepository dateTimeRepository) {
        this.dateTimeRepository = dateTimeRepository;
    }

    @Override
    @Async
    public CompletableFuture<Void> create(DateTimeResponsePutReqDto response, UUID formResponseId) {
        DateTime dateTime = new DateTime();

        var key = new AnyTypeQuestionResponse.PartitionKey();
        key.setQuestionId(response.getQuestionId());
        key.setFormResponseId(formResponseId);

        dateTime.setKey(key);
        dateTime.setDateTime(response.getDateTime());

        dateTimeRepository.save(dateTime);

        return CompletableFuture.completedFuture(null);
    }

    @Override
    @Async
    public CompletableFuture<Void> deleteAllByQuestionIdsAndFormResponseId(List<Long> questionIds, UUID formResponseId) {
        dateTimeRepository.deleteAllByQuestionIdsAndFormResponseId(questionIds, formResponseId);

        return CompletableFuture.completedFuture(null);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.DATE_TIME;
    }

}
