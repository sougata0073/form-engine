package com.sougata.form_data_service.service.responseManager;

import com.sougata.form_data_service.constant.QuestionType;
import com.sougata.form_data_service.dto.question.request.ParagraphResponsePutReqDto;
import com.sougata.form_data_service.model.FormResponse;
import com.sougata.form_data_service.model.Paragraph;
import com.sougata.form_data_service.repository.ParagraphRepository;
import com.sougata.form_data_service.repository.QuestionResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service("PARAGRAPH_RESPONSE_MANAGER")
public class ParagraphManager extends ResponseManager<
        ParagraphResponsePutReqDto
        > {

    private final ParagraphRepository paragraphRepository;

    @Autowired
    public ParagraphManager(ParagraphRepository paragraphRepository, QuestionResponseRepository questionResponseRepositor) {
        super(questionResponseRepositor);
        this.paragraphRepository = paragraphRepository;
    }

    @Override
    @Transactional
    public void create(ParagraphResponsePutReqDto response, FormResponse formResponse) {
        Paragraph paragraph = new Paragraph();

        var qr = createQuestionResponse(response.getQuestionId(), formResponse);

        paragraph.setText(response.getText());
        paragraph.setQuestionResponse(qr);

        paragraphRepository.save(paragraph);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.PARAGRAPH;
    }

    @Override
    public void deleteResponsesByQuestion(UUID formId, Long questionId) {
        paragraphRepository.deleteAllByFormIdAndQuestionId(formId, questionId);
    }

    @Override
    public void deleteResponsesByFormResponse(UUID formId, Long formResponseId) {
        paragraphRepository.deleteAllByFormIdAndFormResponseId(formId, formResponseId);
    }
}
