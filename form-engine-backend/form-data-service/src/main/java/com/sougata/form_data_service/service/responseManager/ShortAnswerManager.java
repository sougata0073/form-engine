package com.sougata.form_data_service.service.responseManager;

import com.sougata.form_data_service.model.AnyTypeQuestionResponse;
import com.sougata.form_data_service.model.FormResponse;
import com.sougata.form_data_service.model.ShortAnswer;
import com.sougata.form_data_service.repository.QuestionResponseRepository;
import com.sougata.form_data_service.repository.ShortAnswerRepository;
import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.question.responseputrequest.ShortAnswerResponsePutReqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SHORT_ANSWER_RESPONSE_MANAGER")
public class ShortAnswerManager extends ResponseManager<ShortAnswerResponsePutReqDto> {

    private final ShortAnswerRepository shortAnswerRepository;

    @Autowired
    public ShortAnswerManager(ShortAnswerRepository shortAnswerRepository, QuestionResponseRepository questionResponseRepository) {
        super(questionResponseRepository);
        this.shortAnswerRepository = shortAnswerRepository;
    }

    @Override
    public void create(ShortAnswerResponsePutReqDto response, FormResponse formResponse) {
        ShortAnswer shortAnswer = new ShortAnswer();

        var qr = createQuestionResponse(response.getQuestionId(), formResponse);

        shortAnswer.setKey(new AnyTypeQuestionResponse.PartitionKey(response.getQuestionId(), qr.getKey().getQuestionResponseId()));
        shortAnswer.setText(response.getText());

        shortAnswerRepository.save(shortAnswer);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.SHORT_ANSWER;
    }

    @Override
    public void deleteResponsesByQuestionId(Long questionId) {
        shortAnswerRepository.deleteAllByQuestionId(questionId);
    }

    @Override
    public void deleteResponsesByQuestionIdAndQuestionResponseId(Long questionId, Long questionResponseId) {
        shortAnswerRepository.deleteAllByQuestionIdAndQuestionResponseId(questionId, questionResponseId);
    }

}
