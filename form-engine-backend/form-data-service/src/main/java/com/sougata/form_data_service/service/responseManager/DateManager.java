package com.sougata.form_data_service.service.responseManager;

import com.sougata.form_data_service.model.AnyTypeQuestionResponse;
import com.sougata.form_data_service.model.Date;
import com.sougata.form_data_service.repository.DateRepository;
import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.formResponse.individual.DateResponseIndividualDto;
import com.sougata.form_engine.dto.question.responseputrequest.DateResponsePutReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service("DATE_RESPONSE_MANAGER")
@RequiredArgsConstructor
public class DateManager extends ResponseManager<DateResponsePutReqDto, DateResponseIndividualDto> {

    private final DateRepository dateRepository;

    @Override
    @Async
    public CompletableFuture<Void> create(UUID formId, UUID formResponseId, DateResponsePutReqDto response) {
        Date date = new Date();

        var key = new AnyTypeQuestionResponse.PartitionKey(
                formId,
                formResponseId,
                response.getQuestionId()
        );

        date.setKey(key);
        date.setDate(response.getDate());

        dateRepository.save(date);

        return CompletableFuture.completedFuture(null);
    }

    @Override
    @Async
    public CompletableFuture<List<DateResponseIndividualDto>> getResponses(UUID formId, UUID formResponseId, Set<Long> questionIds) {
        var responses = dateRepository.findAllByQuestionIds(formId, formResponseId, questionIds);

        return CompletableFuture.completedFuture(
                responses.stream().map(res -> {
                    var d = new DateResponseIndividualDto();

                    d.setQuestionId(res.getKey().getQuestionId());
                    d.setQuestionType(getQuestionType());
                    d.setDate(res.getDate());

                    return d;
                }).toList()
        );
    }

    @Override
    @Async
    public CompletableFuture<Void> deleteAllByQuestionIds(UUID formId, UUID formResponseId, Set<Long> questionIds) {
        dateRepository.deleteAllByQuestionIds(formId, formResponseId, questionIds);

        return CompletableFuture.completedFuture(null);
    }

    @Override
    @Async
    public CompletableFuture<Void> deleteAllByFormId(UUID formId) {
        dateRepository.deleteAllByFormId(formId);

        return CompletableFuture.completedFuture(null);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.DATE;
    }

}
