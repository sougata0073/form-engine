package com.sougata.form_data_service.service.responseManager;

import com.sougata.form_data_service.model.FormResponse;
import com.sougata.form_data_service.model.ShortAnswer;
import com.sougata.form_data_service.repository.QuestionResponseRepository;
import com.sougata.form_data_service.repository.ShortAnswerRepository;
import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.question.responseRequest.ShortAnswerResponsePutReqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service("SHORT_ANSWER_RESPONSE_MANAGER")
public class ShortAnswerManager extends ResponseManager<ShortAnswerResponsePutReqDto> {

    private final ShortAnswerRepository shortAnswerRepository;

    @Autowired
    public ShortAnswerManager(ShortAnswerRepository shortAnswerRepository, QuestionResponseRepository questionResponseRepository) {
        super(questionResponseRepository);
        this.shortAnswerRepository = shortAnswerRepository;
    }

    @Override
    @Transactional
    public void create(ShortAnswerResponsePutReqDto response, FormResponse formResponse) {
        ShortAnswer shortAnswer = new ShortAnswer();

        var qr = createQuestionResponse(response.getQuestionId(), formResponse);

        shortAnswer.setText(response.getText());
        shortAnswer.setQuestionResponse(qr);

        shortAnswerRepository.save(shortAnswer);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.SHORT_ANSWER;
    }

    @Override
    public void deleteResponsesByQuestion(UUID formId, Long questionId) {
        shortAnswerRepository.deleteAllByFormIdAndQuestionId(formId, questionId);
    }

    @Override
    public void deleteResponsesByFormResponse(UUID formId, Long formResponseId) {
        shortAnswerRepository.deleteAllByFormIdAndFormResponseId(formId, formResponseId);
    }
}
