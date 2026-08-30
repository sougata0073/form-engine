package com.sougata.form_service.service.formSchema.questionManager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.question.details.ShortAnswerDetailsDto;
import com.sougata.form_engine.dto.question.schemaRequest.ShortAnswerPutReqDto;
import com.sougata.form_engine.dto.template.questionTemplate.ShortAnswerTemplateDetails;
import com.sougata.form_engine.dto.validation.config.ValidationConfig;
import com.sougata.form_engine.util.JsonUtil;
import com.sougata.form_service.exception.JsonParsingException;
import com.sougata.form_service.exception.QuestionNotFoundException;
import com.sougata.form_service.model.formSchema.Form;
import com.sougata.form_service.model.formSchema.Question;
import com.sougata.form_service.model.formSchema.ShortAnswer;
import com.sougata.form_service.repository.formSchema.QuestionRepository;
import com.sougata.form_service.repository.formSchema.ShortAnswerRepository;
import com.sougata.form_service.service.formSchema.FormService;
import com.sougata.form_service.service.formSchema.QuestionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service("SHORT_ANSWER_QUESTION_MANAGER")
public class ShortAnswerManager extends QuestionManager<ShortAnswer, ShortAnswerPutReqDto, ShortAnswerDetailsDto, ShortAnswerTemplateDetails> {

    private final ShortAnswerRepository shortAnswerRepository;

    public ShortAnswerManager(ShortAnswerRepository shortAnswerRepository, FormService formService, QuestionRepository questionRepository) {
        super(questionRepository, formService);
        this.shortAnswerRepository = shortAnswerRepository;
    }

    @Override
    public ShortAnswerDetailsDto get(UUID formId, Long questionId) {
        return toQuestionResDto(shortAnswerRepository.findByQuestionId(questionId).orElseThrow(() -> new QuestionNotFoundException(questionId)));
    }

    @Override
    @Transactional
    public ShortAnswerDetailsDto create(UUID formId, ShortAnswerPutReqDto crudDto) {
        var newS = new ShortAnswer();

        var question = createQuestion(crudDto, formId);

        setPropertiesForNew(crudDto, newS, question);

        var saved = shortAnswerRepository.save(newS);

        return toQuestionResDto(saved);
    }

    @Override
    @Transactional
    public ShortAnswerDetailsDto create(UUID formId, Long questionId, ShortAnswerPutReqDto questionAddUpdateReq) {
        var newS = new ShortAnswer();

        var question = updateQuestion(questionId, questionAddUpdateReq);

        setPropertiesForNew(questionAddUpdateReq, newS, question);

        var saved = shortAnswerRepository.save(newS);

        return toQuestionResDto(saved);
    }

    @Override
    @Transactional
    public ShortAnswerDetailsDto update(UUID formId, Long questionId, ShortAnswerPutReqDto questionAddUpdateReq) {
        ShortAnswer sa = shortAnswerRepository.findByQuestionId(questionId)
                .orElseThrow(() -> new QuestionNotFoundException(QuestionType.SHORT_ANSWER, questionId));

        updateQuestion(questionId, questionAddUpdateReq);
        sa.setValidationConfig(JsonUtil.objectToOldJsonNode(questionAddUpdateReq.getValidationConfig()));

        shortAnswerRepository.save(sa);

        return toQuestionResDto(sa);
    }

    @Override
    public ShortAnswerDetailsDto toQuestionResDto(ShortAnswer childQuestion) {
        return toQuestionResDto(childQuestion, childQuestion.getQuestion());
    }

    @Override
    public ShortAnswerDetailsDto toQuestionResDto(ShortAnswer childQuestion, Question parentQuestion) {
        var s = new ShortAnswerDetailsDto();

        populateCommonFields(parentQuestion, s);

        try {
            s.setValidationConfig(JsonUtil.oldJsonNodeToObject(childQuestion.getValidationConfig(), ValidationConfig.class));
        } catch (JsonProcessingException e) {
            throw new JsonParsingException(JsonUtil.oldJsonNodeToString(childQuestion.getValidationConfig()));
        }

        return s;
    }

    @Override
    public ShortAnswerPutReqDto toQuestionAddUpdateReq(ShortAnswerDetailsDto questionRes) {
        var sa = new ShortAnswerPutReqDto();

        populateCommonFields(questionRes, sa);

        sa.setValidationConfig(questionRes.getValidationConfig());

        return sa;
    }

    @Override
    @Transactional
    public ShortAnswer createFromTemplate(ShortAnswerTemplateDetails template, Form form) {
        var sa = new ShortAnswer();

        sa.setQuestion(createQuestionFromTemplate(template, form));
        sa.setValidationConfig(JsonUtil.objectToOldJsonNode(template.getValidationConfig()));

        return shortAnswerRepository.save(sa);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.SHORT_ANSWER;
    }

    @Override
    public void delete(UUID formId, Long questionId) {
        shortAnswerRepository.deleteQuestion(questionId);
    }

    private void setPropertiesForNew(ShortAnswerPutReqDto source, ShortAnswer target, Question question) {
        target.setQuestion(question);
        target.setValidationConfig(JsonUtil.objectToOldJsonNode(source.getValidationConfig()));
    }
}
