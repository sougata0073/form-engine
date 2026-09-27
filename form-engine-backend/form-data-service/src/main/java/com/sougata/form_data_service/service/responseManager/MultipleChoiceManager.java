package com.sougata.form_data_service.service.responseManager;

import com.sougata.form_data_service.model.AnyTypeQuestionResponse;
import com.sougata.form_data_service.model.FormResponse;
import com.sougata.form_data_service.model.MultipleChoice;
import com.sougata.form_data_service.repository.MultipleChoiceRepository;
import com.sougata.form_data_service.repository.QuestionResponseRepository;
import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.question.responseputrequest.MultipleChoiceResponsePutReqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service("MULTIPLE_CHOICE_RESPONSE_MANAGER")
public class MultipleChoiceManager extends ResponseManager<MultipleChoiceResponsePutReqDto> {

    private final MultipleChoiceRepository multipleChoiceRepository;

    @Autowired
    public MultipleChoiceManager(MultipleChoiceRepository multipleChoiceRepository, QuestionResponseRepository questionResponseRepository) {
        super(questionResponseRepository);
        this.multipleChoiceRepository = multipleChoiceRepository;
    }

    @Override
    @Async
    public CompletableFuture<Void> create(MultipleChoiceResponsePutReqDto response, FormResponse formResponse) {
        MultipleChoice multipleChoice = new MultipleChoice();

        var qr = createQuestionResponse(response.getQuestionId(), formResponse);

        multipleChoice.setKey(new AnyTypeQuestionResponse.PartitionKey(response.getQuestionId(), qr.getKey().getQuestionResponseId()));
        multipleChoice.setResponseOptionId(response.getResponseOptionId());

        multipleChoiceRepository.save(multipleChoice);

        return CompletableFuture.completedFuture(null);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.MULTIPLE_CHOICE;
    }

    @Override
    public void deleteResponsesByQuestionId(Long questionId) {
        multipleChoiceRepository.deleteAllByQuestionId(questionId);
    }

    @Override
    public void deleteResponsesByQuestionIdAndQuestionResponseId(Long questionId, Long questionResponseId) {
        multipleChoiceRepository.deleteAllByQuestionIdAndQuestionResponseId(questionId, questionResponseId);
    }

}
