package com.sougata.form_service.service.formSchema;

import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.question.details.QuestionDetailsDto;
import com.sougata.form_engine.dto.question.schemaaddrequest.QuestionAddReqDto;
import com.sougata.form_engine.dto.question.schemaupdatereq.QuestionUpdateReqDto;
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
        QAR extends QuestionAddReqDto,
        QUR extends QuestionUpdateReqDto,
        QDetails extends QuestionDetailsDto,
        QTD extends QuestionTemplateDetails
        > {

    private final QuestionRepository questionRepository;
    private final FormService formService;
    private final FormServiceCached formServiceCached;

    protected QuestionManager(QuestionRepository questionRepository, FormService formService, FormServiceCached formServiceCached) {
        this.questionRepository = questionRepository;
        this.formService = formService;
        this.formServiceCached = formServiceCached;
    }

    public abstract QDetails get(UUID formId, Long questionId);

    public abstract QDetails create(UUID formId, UUID userId, QAR questionAddReq);

    public abstract QDetails create(UUID formId, Long questionId, QAR questionAddReq);

    public abstract QDetails update(UUID formId, Long questionId, QUR questionUpdateReq);

    public abstract QuestionType getQuestionType();

    public abstract void delete(Long questionId);

    public abstract QDetails toQuestionDetailsDto(Q childQuestion);

    public abstract QDetails toQuestionDetailsDto(Q childQuestion, Question parentQuestion);

    public abstract QAR toQuestionAddReq(QDetails questionRes);

    public abstract QDetails createFromTemplate(QTD template, Form form);

    public void populateCommonFields(Q childQuestion, QDetails questionRes) {
        questionRes.setId(childQuestion.getQuestionId());
        questionRes.setQuestion(childQuestion.getQuestion().getQuestion());
        questionRes.setQuestionType(getQuestionType());
        questionRes.setDescription(childQuestion.getQuestion().getDescription());
        questionRes.setOrderIndex(childQuestion.getQuestion().getOrderIndex());
        questionRes.setRequired(childQuestion.getQuestion().getRequired());
    }

    public void populateCommonFields(Question parentQuestion, QDetails questionRes) {
        questionRes.setId(parentQuestion.getId());
        questionRes.setQuestion(parentQuestion.getQuestion());
        questionRes.setQuestionType(getQuestionType());
        questionRes.setDescription(parentQuestion.getDescription());
        questionRes.setOrderIndex(parentQuestion.getOrderIndex());
        questionRes.setRequired(parentQuestion.getRequired());
    }

    public void populateCommonFields(QDetails questionRes, QAR questionAddUpdateRequest) {
        questionAddUpdateRequest.setQuestion(questionRes.getQuestion());
        questionAddUpdateRequest.setQuestionType(getQuestionType());
        questionAddUpdateRequest.setDescription(questionRes.getDescription());
    }

    public Question createQuestion(QAR source, UUID formId, UUID userId) {
        var newQ = new Question();
        var formDefaultSettings = formServiceCached.getDefaultFormSettings(userId);

        newQ.setForm(formService.getFormById(formId));

        newQ.setQuestion(source.getQuestion());
        newQ.setDescription(source.getDescription());
        newQ.setRequired(formDefaultSettings.getMakeQuestionsRequiredByDefault());
        newQ.setOrderIndex(questionRepository.getNextQuestionIndex(formId));
        newQ.setQuestionType(getQuestionType());

        return questionRepository.save(newQ);
    }

    public Question updateQuestion(Long questionId, QAR source) {
        var q = questionRepository.findById(questionId)
                .orElseThrow(() -> new QuestionNotFoundException(questionId));

        q.setQuestion(source.getQuestion());
        q.setDescription(source.getDescription());
        q.setQuestionType(getQuestionType());

        return questionRepository.save(q);
    }

    public Question updateQuestion(Long questionId, QUR source) {
        var q = questionRepository.findById(questionId)
                .orElseThrow(() -> new QuestionNotFoundException(questionId));

        source.getUpdateFields().forEach(field -> {
            switch (field) {
                case QUR.Fields.question -> q.setQuestion(source.getQuestion());
                case QUR.Fields.description -> q.setDescription(source.getDescription());
                case QUR.Fields.required -> q.setRequired(source.getRequired());
                case QUR.Fields.questionType -> q.setQuestionType(getQuestionType());
            }
        });

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
