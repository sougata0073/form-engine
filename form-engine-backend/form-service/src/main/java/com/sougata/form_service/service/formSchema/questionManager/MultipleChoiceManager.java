package com.sougata.form_service.service.formSchema.questionManager;

import com.sougata.form_engine.constant.ComplexQuestionUpdateAction;
import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.question.details.MultipleChoiceDetailsDto;
import com.sougata.form_engine.dto.question.schemaaddrequest.MultipleChoiceAddReqDto;
import com.sougata.form_engine.dto.question.schemaupdatereq.CheckboxUpdateReqDto;
import com.sougata.form_engine.dto.question.schemaupdatereq.MultipleChoiceUpdateReqDto;
import com.sougata.form_engine.dto.template.questionTemplate.MultipleChoiceTemplateDetails;
import com.sougata.form_engine.util.JsonUtil;
import com.sougata.form_service.exception.QuestionNotFoundException;
import com.sougata.form_service.model.formSchema.*;
import com.sougata.form_service.repository.formSchema.MultipleChoiceOptionRepository;
import com.sougata.form_service.repository.formSchema.MultipleChoiceRepository;
import com.sougata.form_service.repository.formSchema.QuestionRepository;
import com.sougata.form_service.service.formSchema.FormService;
import com.sougata.form_service.service.formSchema.QuestionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service("MULTIPLE_CHOICE_QUESTION_MANAGER")
public class MultipleChoiceManager extends QuestionManager<MultipleChoice, MultipleChoiceAddReqDto, MultipleChoiceUpdateReqDto, MultipleChoiceDetailsDto, MultipleChoiceTemplateDetails> {

    private final MultipleChoiceRepository multipleChoiceRepository;
    private final MultipleChoiceOptionRepository multipleChoiceOptionRepository;

    public MultipleChoiceManager(MultipleChoiceRepository multipleChoiceRepository, FormService formService, QuestionRepository questionRepository, MultipleChoiceOptionRepository multipleChoiceOptionRepository) {
        super(questionRepository, formService);
        this.multipleChoiceRepository = multipleChoiceRepository;
        this.multipleChoiceOptionRepository = multipleChoiceOptionRepository;
    }

    @Override
    public MultipleChoiceDetailsDto get(UUID formId, Long questionId) {
        return toQuestionResDto(multipleChoiceRepository.findByQuestionId(questionId).orElseThrow(() -> new QuestionNotFoundException(questionId)));
    }

    @Override
    @Transactional
    public MultipleChoiceDetailsDto create(UUID formId, MultipleChoiceAddReqDto crudDto) {
        var newMc = new MultipleChoice();

        var question = createQuestion(crudDto, formId);

        setPropertiesForNew(crudDto, newMc, question);

        var saved = multipleChoiceRepository.save(newMc);

        return toQuestionResDto(saved, question);
    }

    @Override
    @Transactional
    public MultipleChoiceDetailsDto create(UUID formId, Long questionId, MultipleChoiceAddReqDto questionAddReq) {
        var newMc = new MultipleChoice();

        var question = updateQuestion(questionId, questionAddReq);

        setPropertiesForNew(questionAddReq, newMc, question);

        var saved = multipleChoiceRepository.save(newMc);

        return toQuestionResDto(saved, question);
    }

    @Override
    @Transactional
    public MultipleChoiceDetailsDto update(UUID formId, Long questionId, MultipleChoiceUpdateReqDto questionUpdateReq) {
        MultipleChoice mc = multipleChoiceRepository.findByQuestionId(questionId)
                .orElseThrow(() -> new QuestionNotFoundException(QuestionType.MULTIPLE_CHOICE, questionId));

        var question = updateQuestion(questionId, questionUpdateReq);

        if (questionUpdateReq.getUpdateFields().contains(MultipleChoiceUpdateReqDto.Fields.option)) {

            var option = questionUpdateReq.getOption();
            var action = option.getAction();

            if (action == ComplexQuestionUpdateAction.ADD) {

                var mcOption = new MultipleChoiceOption();

                mcOption.setMultipleChoice(mc);
                mcOption.setOption(option.getOption());
                mcOption.setOrderIndex(multipleChoiceRepository.getOptionCount(questionId).intValue());

                multipleChoiceOptionRepository.save(mcOption);

            } else if (action == ComplexQuestionUpdateAction.UPDATE) {

                var mcOption = multipleChoiceOptionRepository.findById(option.getId())
                        .orElseThrow(() -> new IllegalArgumentException("Multiple choice option not found for Id: " + option.getId()));

                mcOption.setOption(option.getOption());

                multipleChoiceOptionRepository.save(mcOption);

            } else if (action == ComplexQuestionUpdateAction.DELETE) {
                multipleChoiceOptionRepository.deleteById(option.getId());
            }
        }

        multipleChoiceRepository.save(mc);

        return toQuestionResDto(mc, question);
    }

    @Override
    public MultipleChoiceDetailsDto toQuestionResDto(MultipleChoice childQuestion) {
        return toQuestionResDto(childQuestion, childQuestion.getQuestion());
    }

    @Override
    public MultipleChoiceDetailsDto toQuestionResDto(MultipleChoice childQuestion, Question parentQuestion) {
        var m = new MultipleChoiceDetailsDto();

        populateCommonFields(parentQuestion, m);

        var options = childQuestion.getOptions().stream()
                .map(op ->
                        new MultipleChoiceDetailsDto.Option(op.getId(), op.getOption(), op.getOrderIndex())
                )
                .sorted(Comparator.comparingInt(MultipleChoiceDetailsDto.Option::getOrderIndex))
                .toList();

        m.setOptions(options);

        return m;
    }

    @Override
    public MultipleChoiceAddReqDto toQuestionAddUpdateReq(MultipleChoiceDetailsDto questionRes) {
        var mc = new MultipleChoiceAddReqDto();

        populateCommonFields(questionRes, mc);

        mc.setOptions(
                questionRes.getOptions().stream()
                        .map(op -> new MultipleChoiceAddReqDto.Option(null, op.getOption()))
                        .toList()
        );

        return mc;
    }

    @Override
    @Transactional
    public MultipleChoice createFromTemplate(MultipleChoiceTemplateDetails template, Form form) {
        var mc = new MultipleChoice();

        mc.setQuestion(createQuestionFromTemplate(template, form));
        mc.setOptions(
                template.getOptions().stream().map(op -> {
                            var res = new MultipleChoiceOption();

                            res.setMultipleChoice(mc);
                            res.setOption(op.getOption());
                            res.setOrderIndex(op.getOrderIndex());

                            return res;
                        })
                        .toList()
        );

        return multipleChoiceRepository.save(mc);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.MULTIPLE_CHOICE;
    }

    @Override
    @Transactional
    public void delete(Long questionId) {
        multipleChoiceRepository.deleteQuestion(questionId);
    }

    private void setPropertiesForNew(MultipleChoiceAddReqDto source, MultipleChoice target, Question question) {
        var options = new ArrayList<MultipleChoiceOption>();

        for (int i = 0; i < source.getOptions().size(); i++) {
            var op = source.getOptions().get(i);
            var mcOp = new MultipleChoiceOption();

            mcOp.setOption(op.getOption());
            mcOp.setMultipleChoice(target);
            mcOp.setOrderIndex(i);

            options.add(mcOp);
        }

        target.setQuestion(question);
        target.setOptions(options);
    }
}
