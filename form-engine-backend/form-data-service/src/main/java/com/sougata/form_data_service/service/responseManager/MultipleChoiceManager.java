package com.sougata.form_data_service.service.responseManager;

import com.sougata.form_data_service.model.FormResponse;
import com.sougata.form_data_service.model.MultipleChoice;
import com.sougata.form_data_service.repository.MultipleChoiceRepository;
import com.sougata.form_data_service.repository.QuestionResponseRepository;
import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.question.responseRequest.MultipleChoiceResponsePutReqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service("MULTIPLE_CHOICE_RESPONSE_MANAGER")
public class MultipleChoiceManager extends ResponseManager<MultipleChoiceResponsePutReqDto> {

    private final MultipleChoiceRepository multipleChoiceRepository;

    @Autowired
    public MultipleChoiceManager(MultipleChoiceRepository multipleChoiceRepository, QuestionResponseRepository questionResponseRepository) {
        super(questionResponseRepository);
        this.multipleChoiceRepository = multipleChoiceRepository;
    }

    @Override
    @Transactional
    public void create(MultipleChoiceResponsePutReqDto response, FormResponse formResponse) {
        MultipleChoice multipleChoice = new MultipleChoice();

        var qr = createQuestionResponse(response.getQuestionId(), formResponse);

        multipleChoice.setResponseOptionId(response.getResponseOptionId());
        multipleChoice.setQuestionResponse(qr);

        multipleChoiceRepository.save(multipleChoice);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.MULTIPLE_CHOICE;
    }

    @Override
    public void deleteResponsesByQuestion(UUID formId, Long questionId) {
        multipleChoiceRepository.deleteAllByFormIdAndQuestionId(formId, questionId);
    }

    @Override
    public void deleteResponsesByFormResponse(UUID formId, Long formResponseId) {
        multipleChoiceRepository.deleteAllByFormIdAndFormResponseId(formId, formResponseId);
    }
}
