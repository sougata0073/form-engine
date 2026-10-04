package com.sougata.form_data_service.service.responseManager;

import com.sougata.form_data_service.model.AnyTypeQuestionResponse;
import com.sougata.form_data_service.model.Date;
import com.sougata.form_data_service.repository.DateRepository;
import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.question.responseputrequest.DateResponsePutReqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service("DATE_RESPONSE_MANAGER")
public class DateManager extends ResponseManager<DateResponsePutReqDto> {

    private final DateRepository dateRepository;

    @Autowired
    public DateManager(DateRepository dateRepository) {
        this.dateRepository = dateRepository;
    }

    @Override
    @Async
    public CompletableFuture<Void> create(DateResponsePutReqDto response, UUID formResponseId) {
        Date date = new Date();

        var key = new AnyTypeQuestionResponse.PartitionKey();
        key.setQuestionId(response.getQuestionId());
        key.setFormResponseId(formResponseId);

        date.setKey(key);
        date.setDate(response.getDate());

        dateRepository.save(date);

        return CompletableFuture.completedFuture(null);
    }

    @Override
    @Async
    public CompletableFuture<Void> deleteAllByQuestionIdsAndFormResponseId(List<Long> questionIds, UUID formResponseId) {
        dateRepository.deleteAllByQuestionIdsAndFormResponseId(questionIds, formResponseId);

        return CompletableFuture.completedFuture(null);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.DATE;
    }

}
