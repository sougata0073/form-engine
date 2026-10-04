package com.sougata.form_data_service.service.responseManager;

import com.sougata.form_data_service.model.AnyTypeQuestionResponse;
import com.sougata.form_data_service.model.MultipleChoice;
import com.sougata.form_data_service.repository.MultipleChoiceRepository;
import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.question.responseputrequest.MultipleChoiceResponsePutReqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service("MULTIPLE_CHOICE_RESPONSE_MANAGER")
public class MultipleChoiceManager extends ResponseManager<MultipleChoiceResponsePutReqDto> {

    private final MultipleChoiceRepository multipleChoiceRepository;

    @Autowired
    public MultipleChoiceManager(MultipleChoiceRepository multipleChoiceRepository) {
        this.multipleChoiceRepository = multipleChoiceRepository;
    }

    @Override
    @Async
    public CompletableFuture<Void> create(MultipleChoiceResponsePutReqDto response, UUID formResponseId) {
        MultipleChoice multipleChoice = new MultipleChoice();

        var key = new AnyTypeQuestionResponse.PartitionKey();
        key.setQuestionId(response.getQuestionId());
        key.setFormResponseId(formResponseId);

        multipleChoice.setKey(key);
        multipleChoice.setResponseOptionId(response.getResponseOptionId());

        multipleChoiceRepository.save(multipleChoice);

        return CompletableFuture.completedFuture(null);
    }

    @Override
    @Async
    public CompletableFuture<Void> deleteAllByQuestionIdsAndFormResponseId(List<Long> questionIds, UUID formResponseId) {
        multipleChoiceRepository.deleteAllByQuestionIdsAndFormResponseId(questionIds, formResponseId);

        return CompletableFuture.completedFuture(null);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.MULTIPLE_CHOICE;
    }

}
