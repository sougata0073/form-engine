package com.sougata.form_service.service.formSchema.questionManager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.question.details.ParagraphDetailsDto;
import com.sougata.form_engine.dto.question.schemaaddrequest.ParagraphAddReqDto;
import com.sougata.form_engine.dto.question.schemaupdatereq.ParagraphUpdateReqDto;
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
public class ParagraphManager extends QuestionManager<Paragraph, ParagraphAddReqDto, ParagraphUpdateReqDto, ParagraphDetailsDto, ParagraphTemplateDetails> {

    private final ParagraphRepository paragraphRepository;

    public ParagraphManager(ParagraphRepository paragraphRepository, FormService formService, QuestionRepository questionRepository) {
        super(questionRepository, formService);
        this.paragraphRepository = paragraphRepository;
    }

    @Override
    public ParagraphDetailsDto get(UUID formId, Long questionId) {
        return toQuestionDetailsDto(paragraphRepository.findByQuestionId(questionId).orElseThrow(() -> new QuestionNotFoundException(questionId)));
    }

    @Override
    @Transactional
    public ParagraphDetailsDto create(UUID formId, ParagraphAddReqDto crudDto) {
        var newP = new Paragraph();

        var question = createQuestion(crudDto, formId);

        setPropertiesForNew(crudDto, newP, question);

        var saved = paragraphRepository.save(newP);

        return toQuestionDetailsDto(saved, question);
    }

    @Override
    @Transactional
    public ParagraphDetailsDto create(UUID formId, Long questionId, ParagraphAddReqDto questionAddReq) {
        var newP = new Paragraph();

        var question = updateQuestion(questionId, questionAddReq);

        setPropertiesForNew(questionAddReq, newP, question);

        var saved = paragraphRepository.save(newP);

        return toQuestionDetailsDto(saved, question);
    }

    @Override
    @Transactional
    public ParagraphDetailsDto update(UUID formId, Long questionId, ParagraphUpdateReqDto questionUpdateReq) {
        Paragraph p = paragraphRepository.findByQuestionId(questionId)
                .orElseThrow(() -> new QuestionNotFoundException(getQuestionType(), questionId));

        var question = updateQuestion(questionId, questionUpdateReq);

        if (questionUpdateReq.getUpdateFields().contains(ParagraphUpdateReqDto.Fields.validationConfig)) {
            p.setValidationConfig(JsonUtil.objectToOldJsonNode(questionUpdateReq.getValidationConfig()));
        }

        return toQuestionDetailsDto(paragraphRepository.save(p), question);
    }

    @Override
    public ParagraphDetailsDto toQuestionDetailsDto(Paragraph childQuestion) {
        return toQuestionDetailsDto(childQuestion, childQuestion.getQuestion());
    }

    @Override
    public ParagraphDetailsDto toQuestionDetailsDto(Paragraph childQuestion, Question parentQuestion) {
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
    public ParagraphAddReqDto toQuestionAddUpdateReq(ParagraphDetailsDto questionRes) {
        var p = new ParagraphAddReqDto();

        populateCommonFields(questionRes, p);

        p.setValidationConfig(questionRes.getValidationConfig());

        return p;
    }

    @Override
    @Transactional
    public ParagraphDetailsDto createFromTemplate(ParagraphTemplateDetails template, Form form) {
        var p = new Paragraph();

        var question = createQuestionFromTemplate(template, form);

        p.setQuestion(question);
        p.setValidationConfig(JsonUtil.objectToOldJsonNode(template.getValidationConfig()));

        return toQuestionDetailsDto(paragraphRepository.save(p), question);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.PARAGRAPH;
    }

    @Override
    public void delete(Long questionId) {
        paragraphRepository.deleteQuestion(questionId);
    }

    private void setPropertiesForNew(ParagraphAddReqDto source, Paragraph target, Question question) {
        target.setQuestion(question);
        target.setValidationConfig(JsonUtil.objectToOldJsonNode(source.getValidationConfig()));
    }
}
