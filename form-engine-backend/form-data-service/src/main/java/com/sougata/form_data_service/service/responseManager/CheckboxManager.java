package com.sougata.form_data_service.service.responseManager;

import com.sougata.form_data_service.model.AnyTypeQuestionResponse;
import com.sougata.form_data_service.model.Checkbox;
import com.sougata.form_data_service.repository.CheckboxRepository;
import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.question.responseputrequest.CheckboxResponsePutReqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service("CHECKBOX_RESPONSE_MANAGER")
public class CheckboxManager extends ResponseManager<CheckboxResponsePutReqDto> {

    private final CheckboxRepository checkboxRepository;

    @Autowired
    public CheckboxManager(CheckboxRepository checkboxRepository) {
        this.checkboxRepository = checkboxRepository;
    }

    @Override
    @Async
    public CompletableFuture<Void> create(CheckboxResponsePutReqDto response, UUID formResponseId) {
        Checkbox cb = new Checkbox();

        var key = new AnyTypeQuestionResponse.PartitionKey();
        key.setQuestionId(response.getQuestionId());
        key.setFormResponseId(formResponseId);

        cb.setKey(key);
        cb.setResponseOptionIds(response.getResponseOptionIds());

        checkboxRepository.save(cb);

        return CompletableFuture.completedFuture(null);
    }

    @Override
    @Async
    public CompletableFuture<Void> deleteAllByQuestionIdsAndFormResponseId(List<Long> questionIds, UUID formResponseId) {
        checkboxRepository.deleteAllByQuestionIdsAndFormResponseId(questionIds, formResponseId);

        return CompletableFuture.completedFuture(null);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.CHECKBOX;
    }

}
