package com.sougata.form_data_service.service.responseManager;

import com.sougata.form_data_service.model.AnyTypeQuestionResponse;
import com.sougata.form_data_service.model.ShortAnswer;
import com.sougata.form_data_service.repository.ShortAnswerRepository;
import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.question.responseputrequest.ShortAnswerResponsePutReqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service("SHORT_ANSWER_RESPONSE_MANAGER")
public class ShortAnswerManager extends ResponseManager<ShortAnswerResponsePutReqDto> {

    private final ShortAnswerRepository shortAnswerRepository;

    @Autowired
    public ShortAnswerManager(ShortAnswerRepository shortAnswerRepository) {
        this.shortAnswerRepository = shortAnswerRepository;
    }

    @Override
    @Async
    public CompletableFuture<Void> create(ShortAnswerResponsePutReqDto response, UUID formResponseId) {
        ShortAnswer shortAnswer = new ShortAnswer();

        var key = new AnyTypeQuestionResponse.PartitionKey();
        key.setQuestionId(response.getQuestionId());
        key.setFormResponseId(formResponseId);

        shortAnswer.setKey(key);
        shortAnswer.setText(response.getText());

        shortAnswerRepository.save(shortAnswer);

        return CompletableFuture.completedFuture(null);
    }

    @Override
    @Async
    public CompletableFuture<Void> deleteAllByQuestionIdsAndFormResponseId(List<Long> questionIds, UUID formResponseId) {
        shortAnswerRepository.deleteAllByQuestionIdsAndFormResponseId(questionIds, formResponseId);

        return CompletableFuture.completedFuture(null);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.SHORT_ANSWER;
    }

}
