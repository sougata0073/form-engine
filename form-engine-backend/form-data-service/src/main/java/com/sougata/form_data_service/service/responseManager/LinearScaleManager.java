package com.sougata.form_data_service.service.responseManager;

import com.sougata.form_data_service.model.AnyTypeQuestionResponse;
import com.sougata.form_data_service.model.FormResponse;
import com.sougata.form_data_service.model.LinearScale;
import com.sougata.form_data_service.repository.LinearScaleRepository;
import com.sougata.form_data_service.repository.QuestionResponseRepository;
import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.question.responseputrequest.LinearScaleResponsePutReqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("LINEAR_SCALE_RESPONSE_MANAGER")
public class LinearScaleManager extends ResponseManager<LinearScaleResponsePutReqDto> {

    private final LinearScaleRepository linearScaleRepository;

    @Autowired
    public LinearScaleManager(LinearScaleRepository linearScaleRepository, QuestionResponseRepository questionResponseRepository) {
        super(questionResponseRepository);
        this.linearScaleRepository = linearScaleRepository;
    }

    @Override
    @Transactional
    public void create(LinearScaleResponsePutReqDto response, FormResponse formResponse) {
        LinearScale linearScale = new LinearScale();

        var qr = createQuestionResponse(response.getQuestionId(), formResponse);

        linearScale.setKey(new AnyTypeQuestionResponse.PartitionKey(response.getQuestionId(), qr.getKey().getQuestionResponseId()));
        linearScale.setScale(response.getScale());

        linearScaleRepository.save(linearScale);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.LINEAR_SCALE;
    }

    @Override
    public void deleteResponsesByQuestionId(Long questionId) {
        linearScaleRepository.deleteAllByQuestionId(questionId);
    }

    @Override
    public void deleteResponsesByQuestionIdAndQuestionResponseId(Long questionId, Long questionResponseId) {
        linearScaleRepository.deleteAllByQuestionIdAndQuestionResponseId(questionId, questionResponseId);
    }

}
