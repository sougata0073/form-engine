package com.sougata.form_service.service.formSchema.questionManager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sougata.form_service.constant.QuestionType;
import com.sougata.form_service.dto.question.request.CheckboxPutReqDto;
import com.sougata.form_service.dto.question.response.CheckboxDetailsDto;
import com.sougata.form_service.dto.template.questionTemplate.CheckboxTemplateDetails;
import com.sougata.form_service.exception.JsonParsingException;
import com.sougata.form_service.exception.QuestionNotFoundException;
import com.sougata.form_service.model.formSchema.Checkbox;
import com.sougata.form_service.model.formSchema.CheckboxOption;
import com.sougata.form_service.model.formSchema.Form;
import com.sougata.form_service.model.formSchema.Question;
import com.sougata.form_service.repository.formSchema.CheckboxRepository;
import com.sougata.form_service.repository.formSchema.QuestionRepository;
import com.sougata.form_service.service.formSchema.FormService;
import com.sougata.form_service.service.formSchema.QuestionManager;
import com.sougata.form_service.util.JsonUtil;
import com.sougata.form_service.validation.configuration.ValidationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service("CHECKBOX_QUESTION_MANAGER")
public class CheckboxManager extends QuestionManager<Checkbox, CheckboxPutReqDto, CheckboxDetailsDto, CheckboxTemplateDetails> {

    private final CheckboxRepository checkboxRepository;

    @Autowired
    public CheckboxManager(CheckboxRepository checkboxRepository, FormService formService, QuestionRepository questionRepository) {
        super(questionRepository, formService);
        this.checkboxRepository = checkboxRepository;
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
    public CheckboxDetailsDto create(UUID formId, CheckboxPutReqDto crudDto) {
        var newCb = new Checkbox();

        var question = createQuestion(crudDto, formId);

        setPropertiesForNew(crudDto, newCb, question);

        var savedCb = checkboxRepository.save(newCb);

        return toQuestionResDto(savedCb, question);
    }

    @Override
    @Transactional
    public CheckboxDetailsDto create(UUID formId, Long questionId, CheckboxPutReqDto questionAddUpdateReq) {
        var newCb = new Checkbox();

        var question = updateQuestion(questionId, questionAddUpdateReq);

        setPropertiesForNew(questionAddUpdateReq, newCb, question);

        var savedCb = checkboxRepository.save(newCb);

        return toQuestionResDto(savedCb, question);
    }

    @Override
    @Transactional
    public CheckboxDetailsDto update(UUID formId, Long questionId, CheckboxPutReqDto questionAddUpdateReq) {
        Checkbox cb = checkboxRepository.findByQuestionId(questionId)
                .orElseThrow(() -> new QuestionNotFoundException(QuestionType.CHECKBOX, questionId));

        var question = updateQuestion(questionId, questionAddUpdateReq);
        cb.setValidationConfig(JsonUtil.objectToOldJsonNode(questionAddUpdateReq.getValidationConfig()));

        Map<Long, CheckboxOption> existingOptions = cb.getOptions().stream()
                .collect(Collectors.toMap(CheckboxOption::getId, option -> option));
        Set<Long> requestOptionIds = questionAddUpdateReq.getOptions().stream()
                .map(CheckboxPutReqDto.Option::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        cb.getOptions().removeIf(option -> !requestOptionIds.contains(option.getId()));

        for (int i = 0; i < questionAddUpdateReq.getOptions().size(); i++) {
            var dto = questionAddUpdateReq.getOptions().get(i);

            if (dto.getId() == null) {
                CheckboxOption option = new CheckboxOption();
                option.setOption(dto.getOption());
                option.setOrderIndex(i);
                option.setCheckbox(cb);

                cb.getOptions().add(option);
            } else {
                CheckboxOption option = existingOptions.get(dto.getId());
                if (option == null) {
                    throw new IllegalArgumentException("Invalid option id: " + dto.getId());
                }
                option.setOption(dto.getOption());
                option.setOrderIndex(i);
            }
        }

        checkboxRepository.save(cb);

        return toQuestionResDto(cb, question);
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
    public CheckboxPutReqDto toQuestionAddUpdateReq(CheckboxDetailsDto questionRes) {
        var cb = new CheckboxPutReqDto();

        populateCommonFields(questionRes, cb);

        cb.setOptions(
                questionRes.getOptions().stream()
                        .map(op -> new CheckboxPutReqDto.Option(null, op.getOption()))
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
    public void delete(UUID formId, Long questionId) {
        checkboxRepository.deleteQuestion(questionId);
    }

    private void setPropertiesForNew(CheckboxPutReqDto source, Checkbox target, Question question) {
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
