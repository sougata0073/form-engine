package com.sougata.form_service.service.formSchema.questionManager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sougata.form_engine.constant.ComplexQuestionUpdateAction;
import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.question.details.CheckboxDetailsDto;
import com.sougata.form_engine.dto.question.schemaaddrequest.CheckboxAddReqDto;
import com.sougata.form_engine.dto.question.schemaupdatereq.CheckboxUpdateReqDto;
import com.sougata.form_engine.dto.template.questionTemplate.CheckboxTemplateDetails;
import com.sougata.form_engine.dto.validation.config.ValidationConfig;
import com.sougata.form_engine.util.JsonUtil;
import com.sougata.form_service.exception.JsonParsingException;
import com.sougata.form_service.exception.QuestionNotFoundException;
import com.sougata.form_service.model.formSchema.*;
import com.sougata.form_service.repository.formSchema.CheckboxOptionRepository;
import com.sougata.form_service.repository.formSchema.CheckboxRepository;
import com.sougata.form_service.repository.formSchema.QuestionRepository;
import com.sougata.form_service.service.formSchema.FormService;
import com.sougata.form_service.service.formSchema.QuestionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.UUID;

@Service("CHECKBOX_QUESTION_MANAGER")
public class CheckboxManager extends QuestionManager<Checkbox, CheckboxAddReqDto, CheckboxUpdateReqDto, CheckboxDetailsDto, CheckboxTemplateDetails> {

    private final CheckboxRepository checkboxRepository;
    private final CheckboxOptionRepository checkboxOptionRepository;

    @Autowired
    public CheckboxManager(CheckboxRepository checkboxRepository, FormService formService, QuestionRepository questionRepository, CheckboxOptionRepository checkboxOptionRepository) {
        super(questionRepository, formService);
        this.checkboxRepository = checkboxRepository;
        this.checkboxOptionRepository = checkboxOptionRepository;
    }

    @Override
    public CheckboxDetailsDto get(UUID formId, Long questionId) {
        return toQuestionResDto(
                checkboxRepository.findByQuestionId(questionId)
                        .orElseThrow(() -> new QuestionNotFoundException(questionId))
        );
    }

    @Override
    @Transactional
    public CheckboxDetailsDto create(UUID formId, CheckboxAddReqDto crudDto) {
        var newCb = new Checkbox();

        var question = createQuestion(crudDto, formId);

        setPropertiesForNew(crudDto, newCb, question);

        var savedCb = checkboxRepository.save(newCb);

        return toQuestionResDto(savedCb, question);
    }

    @Override
    @Transactional
    public CheckboxDetailsDto create(UUID formId, Long questionId, CheckboxAddReqDto questionAddReq) {
        var newCb = new Checkbox();

        var question = updateQuestion(questionId, questionAddReq);

        setPropertiesForNew(questionAddReq, newCb, question);

        var savedCb = checkboxRepository.save(newCb);

        return toQuestionResDto(savedCb, question);
    }

    @Override
    @Transactional
    public CheckboxDetailsDto update(UUID formId, Long questionId, CheckboxUpdateReqDto questionUpdateReq) {
        Checkbox cb = checkboxRepository.findByQuestionId(questionId)
                .orElseThrow(() -> new QuestionNotFoundException(QuestionType.CHECKBOX, questionId));

        var question = updateQuestion(questionId, questionUpdateReq);

        questionUpdateReq.getUpdateFields().forEach(field -> {
            if (CheckboxUpdateReqDto.Fields.validationConfig.equals(field)) {

                cb.setValidationConfig(JsonUtil.objectToOldJsonNode(questionUpdateReq.getValidationConfig()));

            } else if (CheckboxUpdateReqDto.Fields.options.equals(field)) {

                var prevOptions = cb.getOptions();

                questionUpdateReq.getOptions().forEach(option -> {

                    var action = option.getAction();

                    if (action == ComplexQuestionUpdateAction.ADD) {

                        var cOption = new CheckboxOption();

                        cOption.setCheckbox(cb);
                        cOption.setOption(option.getOption());
                        cOption.setOrderIndex(prevOptions.size());

                        prevOptions.add(cOption);

                    } else if (action == ComplexQuestionUpdateAction.UPDATE) {

                        var cOption = prevOptions
                                .stream()
                                .filter(op -> op.getId().equals(option.getId()))
                                .findFirst()
                                .orElseThrow(() -> new IllegalArgumentException("Checkbox option not found for Id: " + option.getId()));

                        cOption.setOption(option.getOption());

                    } else if (action == ComplexQuestionUpdateAction.DELETE) {

                        var optionToDelete = prevOptions
                                .stream()
                                .filter(op -> op.getId().equals(option.getId()))
                                .findFirst()
                                .orElseThrow(() -> new IllegalArgumentException("Checkbox option not found for Id: " + option.getId()));

                        prevOptions.remove(optionToDelete);

                        prevOptions
                                .stream()
                                .sorted(Comparator.comparingInt(CheckboxOption::getOrderIndex))
                                .forEach(op -> {

                                    if (op.getOrderIndex() > optionToDelete.getOrderIndex()) {
                                        op.setOrderIndex(op.getOrderIndex() - 1);
                                    }

                                });
                    }
                });
            }
        });

        return toQuestionResDto(checkboxRepository.save(cb), question);
    }

    @Override
    public CheckboxDetailsDto toQuestionResDto(Checkbox childQuestion) {
        return toQuestionResDto(childQuestion, childQuestion.getQuestion());
    }

    @Override
    public CheckboxDetailsDto toQuestionResDto(Checkbox childQuestion, Question parentQuestion) {
        var cb = new CheckboxDetailsDto();

        populateCommonFields(parentQuestion, cb);

        var options = childQuestion.getOptions().stream()
                .map(o ->
                        new CheckboxDetailsDto.Option(o.getId(), o.getOption(), o.getOrderIndex())
                )
                .sorted(Comparator.comparingInt(CheckboxDetailsDto.Option::getOrderIndex))
                .toList();

        cb.setOptions(options);

        try {
            cb.setValidationConfig(
                    JsonUtil.oldJsonNodeToObject(childQuestion.getValidationConfig(), ValidationConfig.class)
            );
        } catch (JsonProcessingException e) {
            throw new JsonParsingException(JsonUtil.oldJsonNodeToString(childQuestion.getValidationConfig()));
        }

        return cb;
    }

    @Override
    public CheckboxAddReqDto toQuestionAddUpdateReq(CheckboxDetailsDto questionRes) {
        var cb = new CheckboxAddReqDto();

        populateCommonFields(questionRes, cb);

        cb.setOptions(
                questionRes.getOptions().stream()
                        .map(op -> new CheckboxAddReqDto.Option(null, op.getOption()))
                        .toList()
        );
        cb.setValidationConfig(questionRes.getValidationConfig());

        return cb;
    }

    @Override
    public Checkbox createFromTemplate(CheckboxTemplateDetails template, Form form) {
        var cb = new Checkbox();

        cb.setQuestion(createQuestionFromTemplate(template, form));
        cb.setValidationConfig(JsonUtil.objectToOldJsonNode(template.getValidationConfig()));
        cb.setOptions(
                template.getOptions().stream().map(op -> {
                            var res = new CheckboxOption();

                            res.setCheckbox(cb);
                            res.setOption(op.getOption());
                            res.setOrderIndex(op.getOrderIndex());

                            return res;
                        })
                        .toList()
        );

        return checkboxRepository.save(cb);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.CHECKBOX;
    }

    @Override
    @Transactional
    public void delete(Long questionId) {
        checkboxRepository.deleteQuestion(questionId);
    }

    private void setPropertiesForNew(CheckboxAddReqDto source, Checkbox target, Question question) {
        var options = new ArrayList<CheckboxOption>();

        for (int i = 0; i < source.getOptions().size(); i++) {
            var op = source.getOptions().get(i);
            var cbOp = new CheckboxOption();

            cbOp.setOption(op.getOption());
            cbOp.setCheckbox(target);
            cbOp.setOrderIndex(i);

            options.add(cbOp);
        }

        target.setQuestion(question);
        target.setValidationConfig(JsonUtil.objectToOldJsonNode(source.getValidationConfig()));
        target.setOptions(options);
    }

}
