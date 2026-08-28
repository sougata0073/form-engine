package com.sougata.form_service.service.formSchema.questionManager;

import com.sougata.form_service.constant.QuestionType;
import com.sougata.form_service.dto.question.request.LinearScalePutReqDto;
import com.sougata.form_service.dto.question.response.LinearScaleDetailsDto;
import com.sougata.form_service.dto.template.questionTemplate.LinearScaleTemplateDetails;
import com.sougata.form_service.exception.QuestionNotFoundException;
import com.sougata.form_service.model.formSchema.Form;
import com.sougata.form_service.model.formSchema.LinearScale;
import com.sougata.form_service.model.formSchema.Question;
import com.sougata.form_service.repository.formSchema.LinearScaleRepository;
import com.sougata.form_service.repository.formSchema.QuestionRepository;
import com.sougata.form_service.service.formSchema.FormService;
import com.sougata.form_service.service.formSchema.QuestionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service("LINEAR_SCALE_QUESTION_MANAGER")
public class LinearScaleManager extends QuestionManager<LinearScale, LinearScalePutReqDto, LinearScaleDetailsDto, LinearScaleTemplateDetails> {

    private final LinearScaleRepository linearScaleRepository;

    public LinearScaleManager(LinearScaleRepository linearScaleRepository, FormService formService, QuestionRepository questionRepository) {
        super(questionRepository, formService);
        this.linearScaleRepository = linearScaleRepository;
    }

    @Override
    public LinearScaleDetailsDto get(UUID formId, Long questionId) {
        return toQuestionResDto(linearScaleRepository.findByQuestionId(questionId).orElseThrow(() -> new QuestionNotFoundException(questionId)));
    }

    @Override
    @Transactional
    public LinearScaleDetailsDto create(UUID formId, LinearScalePutReqDto crudDto) {
        var newLs = new LinearScale();

        var question = createQuestion(crudDto, formId);

        setPropertiesForNew(crudDto, newLs, question);

        var saved = linearScaleRepository.save(newLs);

        return toQuestionResDto(saved, question);
    }

    @Override
    @Transactional
    public LinearScaleDetailsDto create(UUID formId, Long questionId, LinearScalePutReqDto questionAddUpdateReq) {
        var newCb = new LinearScale();

        var question = updateQuestion(questionId, questionAddUpdateReq);

        setPropertiesForNew(questionAddUpdateReq, newCb, question);

        var saved = linearScaleRepository.save(newCb);

        return toQuestionResDto(saved, question);
    }

    @Override
    @Transactional
    public LinearScaleDetailsDto update(UUID formId, Long questionId, LinearScalePutReqDto questionAddUpdateReq) {
        LinearScale ls = linearScaleRepository.findByQuestionId(questionId)
                .orElseThrow(() -> new QuestionNotFoundException(QuestionType.LINEAR_SCALE, questionId));

        var question = updateQuestion(questionId, questionAddUpdateReq);

        ls.setFromNumber(questionAddUpdateReq.getFromNumber());
        ls.setToNumber(questionAddUpdateReq.getToNumber());

        linearScaleRepository.save(ls);

        return toQuestionResDto(ls, question);
    }

    @Override
    public LinearScaleDetailsDto toQuestionResDto(LinearScale childQuestion) {
        return toQuestionResDto(childQuestion, childQuestion.getQuestion());
    }

    @Override
    public LinearScaleDetailsDto toQuestionResDto(LinearScale childQuestion, Question parentQuestion) {
        var ls = new LinearScaleDetailsDto();

        populateCommonFields(parentQuestion, ls);

        ls.setFromNumber(childQuestion.getFromNumber());
        ls.setToNumber(childQuestion.getToNumber());

        return ls;
    }

    @Override
    public LinearScalePutReqDto toQuestionAddUpdateReq(LinearScaleDetailsDto questionRes) {
        var ls = new LinearScalePutReqDto();

        populateCommonFields(questionRes, ls);

        ls.setFromNumber(questionRes.getFromNumber());
        ls.setToNumber(questionRes.getToNumber());

        return ls;
    }

    @Override
    @Transactional
    public LinearScale createFromTemplate(LinearScaleTemplateDetails template, Form form) {
        var ls = new LinearScale();

        ls.setQuestion(createQuestionFromTemplate(template, form));
        ls.setFromNumber(template.getFromNumber());
        ls.setToNumber(template.getToNumber());

        return linearScaleRepository.save(ls);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.LINEAR_SCALE;
    }

    @Override
    public void delete(UUID formId, Long questionId) {
        linearScaleRepository.deleteQuestion(questionId);
    }

    private void setPropertiesForNew(LinearScalePutReqDto source, LinearScale target, Question question) {
        target.setQuestion(question);
        target.setFromNumber(source.getFromNumber());
        target.setToNumber(source.getToNumber());
    }
}
