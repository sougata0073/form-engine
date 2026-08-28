package com.sougata.form_service.service.formSchema.questionManager;

import com.sougata.form_service.constant.QuestionType;
import com.sougata.form_service.dto.question.request.DropdownPutReqDto;
import com.sougata.form_service.dto.question.response.DropdownDetailsDto;
import com.sougata.form_service.dto.template.questionTemplate.DropdownTemplateDetails;
import com.sougata.form_service.exception.QuestionNotFoundException;
import com.sougata.form_service.model.formSchema.Dropdown;
import com.sougata.form_service.model.formSchema.DropdownOption;
import com.sougata.form_service.model.formSchema.Form;
import com.sougata.form_service.model.formSchema.Question;
import com.sougata.form_service.repository.formSchema.DropdownRepository;
import com.sougata.form_service.repository.formSchema.QuestionRepository;
import com.sougata.form_service.service.formSchema.FormService;
import com.sougata.form_service.service.formSchema.QuestionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service("DROPDOWN_QUESTION_MANAGER")
public class DropdownManager extends QuestionManager<Dropdown, DropdownPutReqDto, DropdownDetailsDto, DropdownTemplateDetails> {

    private final DropdownRepository dropdownRepository;

    public DropdownManager(DropdownRepository dropdownRepository, FormService formService, QuestionRepository questionRepository) {
        super(questionRepository, formService);
        this.dropdownRepository = dropdownRepository;
    }

    @Override
    public DropdownDetailsDto get(UUID formId, Long questionId) {
        return toQuestionResDto(dropdownRepository.findByQuestionId(questionId).orElseThrow(() -> new QuestionNotFoundException(questionId)));
    }

    @Override
    @Transactional
    public DropdownDetailsDto create(UUID formId, DropdownPutReqDto crudDto) {
        var newDd = new Dropdown();

        var question = createQuestion(crudDto, formId);

        setPropertiesForNew(crudDto, newDd, question);

        var saved = dropdownRepository.save(newDd);

        return toQuestionResDto(saved, question);
    }

    @Override
    @Transactional
    public DropdownDetailsDto create(UUID formId, Long questionId, DropdownPutReqDto questionAddUpdateReq) {
        var newDd = new Dropdown();

        var question = updateQuestion(questionId, questionAddUpdateReq);

        setPropertiesForNew(questionAddUpdateReq, newDd, question);

        var saved = dropdownRepository.save(newDd);

        return toQuestionResDto(saved, question);
    }

    @Override
    @Transactional
    public DropdownDetailsDto update(UUID formId, Long questionId, DropdownPutReqDto questionAddUpdateReq) {
        Dropdown dd = dropdownRepository.findByQuestionId(questionId)
                .orElseThrow(() -> new QuestionNotFoundException(QuestionType.DROPDOWN, questionId));

        var question = updateQuestion(questionId, questionAddUpdateReq);

        Map<Long, DropdownOption> existingOptions = dd.getOptions().stream()
                .collect(Collectors.toMap(DropdownOption::getId, option -> option));
        Set<Long> requestOptionIds = questionAddUpdateReq.getOptions().stream()
                .map(DropdownPutReqDto.Option::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        dd.getOptions().removeIf(option -> !requestOptionIds.contains(option.getId()));

        for (int i = 0; i < questionAddUpdateReq.getOptions().size(); i++) {
            var dto = questionAddUpdateReq.getOptions().get(i);

            if (dto.getId() == null) {
                DropdownOption option = new DropdownOption();
                option.setOption(dto.getOption());
                option.setOrderIndex(i);
                option.setDropdown(dd);

                dd.getOptions().add(option);
            } else {
                DropdownOption option = existingOptions.get(dto.getId());
                if (option == null) {
                    throw new IllegalArgumentException("Invalid dropdown option id: " + dto.getId());
                }
                option.setOption(dto.getOption());
                option.setOrderIndex(i);
            }
        }

        dropdownRepository.save(dd);

        return toQuestionResDto(dd, question);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.DROPDOWN;
    }

    @Override
    @Transactional
    public void delete(UUID formId, Long questionId) {
        dropdownRepository.deleteQuestion(questionId);
    }

    @Override
    public DropdownDetailsDto toQuestionResDto(Dropdown childQuestion) {
        return toQuestionResDto(childQuestion, childQuestion.getQuestion());
    }

    @Override
    public DropdownDetailsDto toQuestionResDto(Dropdown childQuestion, Question parentQuestion) {
        var dd = new DropdownDetailsDto();

        populateCommonFields(parentQuestion, dd);

        var options = childQuestion.getOptions().stream()
                .map(o -> new DropdownDetailsDto.Option(o.getId(), o.getOption(), o.getOrderIndex()))
                .sorted(Comparator.comparingInt(DropdownDetailsDto.Option::getOrderIndex))
                .toList();

        dd.setOptions(options);

        return dd;
    }

    @Override
    public DropdownPutReqDto toQuestionAddUpdateReq(DropdownDetailsDto questionRes) {
        var dd = new DropdownPutReqDto();

        populateCommonFields(questionRes, dd);

        dd.setOptions(
                questionRes.getOptions().stream()
                        .map(op -> new DropdownPutReqDto.Option(null, op.getOption()))
                        .toList()
        );

        return dd;
    }

    @Override
    @Transactional
    public Dropdown createFromTemplate(DropdownTemplateDetails template, Form form) {
        var d = new Dropdown();

        d.setQuestion(createQuestionFromTemplate(template, form));
        d.setOptions(
                template.getOptions().stream().map(op -> {
                            var res = new DropdownOption();

                            res.setDropdown(d);
                            res.setOption(op.getOption());
                            res.setOrderIndex(op.getOrderIndex());

                            return res;
                        })
                        .toList()
        );

        return dropdownRepository.save(d);
    }

    private void setPropertiesForNew(DropdownPutReqDto source, Dropdown target, Question question) {
        var options = new ArrayList<DropdownOption>();

        for (int i = 0; i < source.getOptions().size(); i++) {
            var op = source.getOptions().get(i);
            var ddOp = new DropdownOption();

            ddOp.setOption(op.getOption());
            ddOp.setDropdown(target);
            ddOp.setOrderIndex(i);

            options.add(ddOp);
        }

        target.setQuestion(question);
        target.setOptions(options);
    }
}
