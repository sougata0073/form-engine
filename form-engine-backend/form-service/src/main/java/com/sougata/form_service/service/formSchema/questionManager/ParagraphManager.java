package com.sougata.form_service.service.formSchema.questionManager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.question.details.ParagraphDetailsDto;
import com.sougata.form_engine.dto.question.schemaputrequest.ParagraphPutReqDto;
import com.sougata.form_engine.dto.template.questionTemplate.ParagraphTemplateDetails;
import com.sougata.form_engine.dto.validation.config.ValidationConfig;
import com.sougata.form_engine.util.JsonUtil;
import com.sougata.form_service.exception.JsonParsingException;
import com.sougata.form_service.exception.QuestionNotFoundException;
import com.sougata.form_service.model.formSchema.Form;
import com.sougata.form_service.model.formSchema.Paragraph;
import com.sougata.form_service.model.formSchema.Question;
import com.sougata.form_service.repository.formSchema.ParagraphRepository;
import com.sougata.form_service.repository.formSchema.QuestionRepository;
import com.sougata.form_service.service.formSchema.FormService;
import com.sougata.form_service.service.formSchema.QuestionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service("PARAGRAPH_QUESTION_MANAGER")
public class ParagraphManager extends QuestionManager<Paragraph, ParagraphPutReqDto, ParagraphDetailsDto, ParagraphTemplateDetails> {

    private final ParagraphRepository paragraphRepository;

    public ParagraphManager(ParagraphRepository paragraphRepository, FormService formService, QuestionRepository questionRepository) {
        super(questionRepository, formService);
        this.paragraphRepository = paragraphRepository;
    }

    @Override
    public ParagraphDetailsDto get(UUID formId, Long questionId) {
        return toQuestionResDto(paragraphRepository.findByQuestionId(questionId).orElseThrow(() -> new QuestionNotFoundException(questionId)));
    }

    @Override
    @Transactional
    public ParagraphDetailsDto create(UUID formId, ParagraphPutReqDto crudDto) {
        var newP = new Paragraph();

        var question = createQuestion(crudDto, formId);

        setPropertiesForNew(crudDto, newP, question);

        var saved = paragraphRepository.save(newP);

        return toQuestionResDto(saved, question);
    }

    @Override
    @Transactional
    public ParagraphDetailsDto create(UUID formId, Long questionId, ParagraphPutReqDto questionAddUpdateReq) {
        var newP = new Paragraph();

        var question = updateQuestion(questionId, questionAddUpdateReq);

        setPropertiesForNew(questionAddUpdateReq, newP, question);

        var saved = paragraphRepository.save(newP);

        return toQuestionResDto(saved, question);
    }

    @Override
    @Transactional
    public ParagraphDetailsDto update(UUID formId, Long questionId, ParagraphPutReqDto questionAddUpdateReq) {
        Paragraph p = paragraphRepository.findByQuestionId(questionId)
                .orElseThrow(() -> new QuestionNotFoundException(QuestionType.PARAGRAPH, questionId));

        var question = updateQuestion(questionId, questionAddUpdateReq);
        p.setValidationConfig(JsonUtil.objectToOldJsonNode(questionAddUpdateReq.getValidationConfig()));

        paragraphRepository.save(p);

        return toQuestionResDto(p, question);
    }

    @Override
    public ParagraphDetailsDto toQuestionResDto(Paragraph childQuestion) {
        return toQuestionResDto(childQuestion, childQuestion.getQuestion());
    }

    @Override
    public ParagraphDetailsDto toQuestionResDto(Paragraph childQuestion, Question parentQuestion) {
        var p = new ParagraphDetailsDto();

        populateCommonFields(parentQuestion, p);

        try {
            p.setValidationConfig(JsonUtil.oldJsonNodeToObject(childQuestion.getValidationConfig(), ValidationConfig.class));
        } catch (JsonProcessingException e) {
            throw new JsonParsingException(JsonUtil.oldJsonNodeToString(childQuestion.getValidationConfig()));
        }

        return p;
    }

    @Override
    public ParagraphPutReqDto toQuestionAddUpdateReq(ParagraphDetailsDto questionRes) {
        var p = new ParagraphPutReqDto();

        populateCommonFields(questionRes, p);

        p.setValidationConfig(questionRes.getValidationConfig());

        return p;
    }

    @Override
    @Transactional
    public Paragraph createFromTemplate(ParagraphTemplateDetails template, Form form) {
        var p = new Paragraph();

        p.setQuestion(createQuestionFromTemplate(template, form));
        p.setValidationConfig(JsonUtil.objectToOldJsonNode(template.getValidationConfig()));

        return paragraphRepository.save(p);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.PARAGRAPH;
    }

    @Override
    public void delete(UUID formId, Long questionId) {
        paragraphRepository.deleteQuestion(questionId);
    }

    private void setPropertiesForNew(ParagraphPutReqDto source, Paragraph target, Question question) {
        target.setQuestion(question);
        target.setValidationConfig(JsonUtil.objectToOldJsonNode(source.getValidationConfig()));
    }
}
