package com.sougata.form_service.service.formSchema.questionManager;

import com.sougata.form_engine.constant.ComplexQuestionUpdateAction;
import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.question.details.DropdownDetailsDto;
import com.sougata.form_engine.dto.question.schemaaddrequest.DropdownAddReqDto;
import com.sougata.form_engine.dto.question.schemaupdatereq.DropdownUpdateReqDto;
import com.sougata.form_engine.dto.template.questionTemplate.DropdownTemplateDetails;
import com.sougata.form_service.exception.QuestionNotFoundException;
import com.sougata.form_service.model.formSchema.Dropdown;
import com.sougata.form_service.model.formSchema.DropdownOption;
import com.sougata.form_service.model.formSchema.Form;
import com.sougata.form_service.model.formSchema.Question;
import com.sougata.form_service.repository.formSchema.DropdownOptionRepository;
import com.sougata.form_service.repository.formSchema.DropdownRepository;
import com.sougata.form_service.repository.formSchema.QuestionRepository;
import com.sougata.form_service.service.formSchema.FormService;
import com.sougata.form_service.service.formSchema.QuestionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.UUID;

@Service("DROPDOWN_QUESTION_MANAGER")
public class DropdownManager extends QuestionManager<Dropdown, DropdownAddReqDto, DropdownUpdateReqDto, DropdownDetailsDto, DropdownTemplateDetails> {

    private final DropdownRepository dropdownRepository;
    private final DropdownOptionRepository dropdownOptionRepository;

    public DropdownManager(DropdownRepository dropdownRepository, FormService formService, QuestionRepository questionRepository, DropdownOptionRepository dropdownOptionRepository) {
        super(questionRepository, formService);
        this.dropdownRepository = dropdownRepository;
        this.dropdownOptionRepository = dropdownOptionRepository;
    }

    @Override
    public DropdownDetailsDto get(UUID formId, Long questionId) {
        return toQuestionResDto(dropdownRepository.findByQuestionId(questionId).orElseThrow(() -> new QuestionNotFoundException(questionId)));
    }

    @Override
    @Transactional
    public DropdownDetailsDto create(UUID formId, DropdownAddReqDto crudDto) {
        var newDd = new Dropdown();

        var question = createQuestion(crudDto, formId);

        setPropertiesForNew(crudDto, newDd, question);

        var saved = dropdownRepository.save(newDd);

        return toQuestionResDto(saved, question);
    }

    @Override
    @Transactional
    public DropdownDetailsDto create(UUID formId, Long questionId, DropdownAddReqDto questionAddReq) {
        var newDd = new Dropdown();

        var question = updateQuestion(questionId, questionAddReq);

        setPropertiesForNew(questionAddReq, newDd, question);

        var saved = dropdownRepository.save(newDd);

        return toQuestionResDto(saved, question);
    }

    @Override
    @Transactional
    public DropdownDetailsDto update(UUID formId, Long questionId, DropdownUpdateReqDto questionUpdateReq) {
        Dropdown dd = dropdownRepository.findByQuestionId(questionId)
                .orElseThrow(() -> new QuestionNotFoundException(QuestionType.DROPDOWN, questionId));

        var question = updateQuestion(questionId, questionUpdateReq);

        if(questionUpdateReq.getUpdateFields().contains(DropdownUpdateReqDto.Fields.option)) {
            var option = questionUpdateReq.getOption();
            var action = option.getAction();

            if (action == ComplexQuestionUpdateAction.ADD) {

                var dOption = new DropdownOption();

                dOption.setDropdown(dd);
                dOption.setOption(option.getOption());
                dOption.setOrderIndex(dropdownRepository.getOptionCount(questionId).intValue());

                dropdownOptionRepository.save(dOption);

            } else if (action == ComplexQuestionUpdateAction.UPDATE) {

                var dOption = dropdownOptionRepository.findById(option.getId())
                        .orElseThrow(() -> new IllegalArgumentException("Dropdown option not found for Id: " + option.getId()));

                dOption.setOption(option.getOption());

                dropdownOptionRepository.save(dOption);

            } else if (action == ComplexQuestionUpdateAction.DELETE) {
                dropdownOptionRepository.deleteById(option.getId());
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
    public void delete(Long questionId) {
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
    public DropdownAddReqDto toQuestionAddUpdateReq(DropdownDetailsDto questionRes) {
        var dd = new DropdownAddReqDto();

        populateCommonFields(questionRes, dd);

        dd.setOptions(
                questionRes.getOptions().stream()
                        .map(op -> new DropdownAddReqDto.Option(null, op.getOption()))
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

    private void setPropertiesForNew(DropdownAddReqDto source, Dropdown target, Question question) {
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
