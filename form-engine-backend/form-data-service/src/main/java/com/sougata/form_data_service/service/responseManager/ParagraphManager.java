package com.sougata.form_data_service.service.responseManager;

import com.sougata.form_data_service.model.AnyTypeQuestionResponse;
import com.sougata.form_data_service.model.FormResponse;
import com.sougata.form_data_service.model.Paragraph;
import com.sougata.form_data_service.repository.ParagraphRepository;
import com.sougata.form_data_service.repository.QuestionResponseRepository;
import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.question.responseputrequest.ParagraphResponsePutReqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("PARAGRAPH_RESPONSE_MANAGER")
public class ParagraphManager extends ResponseManager<ParagraphResponsePutReqDto> {

    private final ParagraphRepository paragraphRepository;

    @Autowired
    public ParagraphManager(ParagraphRepository paragraphRepository, QuestionResponseRepository questionResponseRepositor) {
        super(questionResponseRepositor);
        this.paragraphRepository = paragraphRepository;
    }

    @Override
    public void create(ParagraphResponsePutReqDto response, FormResponse formResponse) {
        Paragraph paragraph = new Paragraph();

        var qr = createQuestionResponse(response.getQuestionId(), formResponse);

        paragraph.setKey(new AnyTypeQuestionResponse.PartitionKey(response.getQuestionId(), qr.getKey().getQuestionResponseId()));
        paragraph.setText(response.getText());

        paragraphRepository.save(paragraph);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.PARAGRAPH;
    }

    @Override
    public void deleteResponsesByQuestionId(Long questionId) {
        paragraphRepository.deleteAllByQuestionId(questionId);
    }

    @Override
    public void deleteResponsesByQuestionIdAndQuestionResponseId(Long questionId, Long questionResponseId) {
        paragraphRepository.deleteAllByQuestionIdAndQuestionResponseId(questionId, questionResponseId);
    }

}
