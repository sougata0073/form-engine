package com.sougata.form_service.service.formSchema;

import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.question.details.QuestionDetailsDto;
import com.sougata.form_engine.dto.question.schemaputrequest.QuestionPutReqDto;
import com.sougata.form_engine.dto.template.questionTemplate.QuestionTemplateDetails;
import com.sougata.form_service.exception.QuestionNotFoundException;
import com.sougata.form_service.model.formSchema.AnyTypeQuestion;
import com.sougata.form_service.model.formSchema.Form;
import com.sougata.form_service.model.formSchema.Question;
import com.sougata.form_service.repository.formSchema.QuestionRepository;

import java.util.UUID;

public abstract class
QuestionManager<
        Q extends AnyTypeQuestion,
        QAUR extends QuestionPutReqDto,
        QR extends QuestionDetailsDto,
        QTD extends QuestionTemplateDetails
        > {

    private final QuestionRepository questionRepository;
    private final FormService formService;

    protected QuestionManager(QuestionRepository questionRepository, FormService formService) {
        this.questionRepository = questionRepository;
        this.formService = formService;
    }

    public abstract QR get(UUID formId, Long questionId);

    public abstract QR create(UUID formId, QAUR crudDto);

    public abstract QR create(UUID formId, Long questionId, QAUR questionAddUpdateReq);

    public abstract QR update(UUID formId, Long questionId, QAUR questionAddUpdateReq);

    public abstract QuestionType getQuestionType();

    public abstract void delete(UUID formId, Long questionId);

    public abstract QR toQuestionResDto(Q childQuestion);

    public abstract QR toQuestionResDto(Q childQuestion, Question parentQuestion);

    public abstract QAUR toQuestionAddUpdateReq(QR questionRes);

    public abstract Q createFromTemplate(QTD template, Form form);

    public void populateCommonFields(Q childQuestion, QR questionRes) {
        questionRes.setId(childQuestion.getQuestionId());
        questionRes.setQuestion(childQuestion.getQuestion().getQuestion());
        questionRes.setQuestionType(getQuestionType());
        questionRes.setDescription(childQuestion.getQuestion().getDescription());
        questionRes.setOrderIndex(childQuestion.getQuestion().getOrderIndex());
        questionRes.setRequired(childQuestion.getQuestion().getRequired());
    }

    public void populateCommonFields(Question parentQuestion, QR questionRes) {
        questionRes.setId(parentQuestion.getId());
        questionRes.setQuestion(parentQuestion.getQuestion());
        questionRes.setQuestionType(getQuestionType());
        questionRes.setDescription(parentQuestion.getDescription());
        questionRes.setOrderIndex(parentQuestion.getOrderIndex());
        questionRes.setRequired(parentQuestion.getRequired());
    }

    public void populateCommonFields(QR questionRes, QAUR questionAddUpdateRequest) {
        questionAddUpdateRequest.setQuestion(questionRes.getQuestion());
        questionAddUpdateRequest.setQuestionType(getQuestionType());
        questionAddUpdateRequest.setDescription(questionRes.getDescription());
        questionAddUpdateRequest.setRequired(questionRes.getRequired());
    }

    public Question createQuestion(QAUR source, UUID formId) {
        var newQ = new Question();

        newQ.setForm(formService.getFormById(formId));

        newQ.setQuestion(source.getQuestion());
        newQ.setDescription(source.getDescription());
        newQ.setRequired(source.getRequired());
        newQ.setOrderIndex(questionRepository.getNextQuestionIndex(formId));
        newQ.setQuestionType(getQuestionType());

        return questionRepository.save(newQ);
    }

    public Question updateQuestion(Long questionId, QAUR source) {
        var q = questionRepository.findById(questionId)
                .orElseThrow(() -> new QuestionNotFoundException(questionId));

        q.setQuestion(source.getQuestion());
        q.setDescription(source.getDescription());
        q.setRequired(source.getRequired());
        q.setQuestionType(getQuestionType());

        return questionRepository.save(q);
    }

    public Question createQuestionFromTemplate(QTD template, Form form) {
        var newQ = new Question();

        newQ.setForm(form);
        newQ.setQuestion(template.getQuestion());
        newQ.setDescription(template.getDescription());
        newQ.setRequired(template.getRequired());
        newQ.setOrderIndex(questionRepository.getNextQuestionIndex(form.getId()));
        newQ.setQuestionType(getQuestionType());

        return questionRepository.save(newQ);
    }
}
