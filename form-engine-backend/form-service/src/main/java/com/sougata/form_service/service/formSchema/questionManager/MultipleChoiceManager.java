package com.sougata.form_service.service.formSchema.questionManager;

import com.sougata.form_engine.constant.ComplexQuestionUpdateAction;
import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.question.details.MultipleChoiceDetailsDto;
import com.sougata.form_engine.dto.question.schemaaddrequest.MultipleChoiceAddReqDto;
import com.sougata.form_engine.dto.question.schemaupdatereq.MultipleChoiceUpdateReqDto;
import com.sougata.form_engine.dto.template.questionTemplate.MultipleChoiceTemplateDetails;
import com.sougata.form_service.exception.QuestionNotFoundException;
import com.sougata.form_service.model.formSchema.Form;
import com.sougata.form_service.model.formSchema.MultipleChoice;
import com.sougata.form_service.model.formSchema.MultipleChoiceOption;
import com.sougata.form_service.model.formSchema.Question;
import com.sougata.form_service.repository.formSchema.MultipleChoiceRepository;
import com.sougata.form_service.repository.formSchema.QuestionRepository;
import com.sougata.form_service.service.formSchema.FormService;
import com.sougata.form_service.service.formSchema.QuestionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.UUID;

@Service("MULTIPLE_CHOICE_QUESTION_MANAGER")
public class MultipleChoiceManager extends QuestionManager<MultipleChoice, MultipleChoiceAddReqDto, MultipleChoiceUpdateReqDto, MultipleChoiceDetailsDto, MultipleChoiceTemplateDetails> {

    private final MultipleChoiceRepository multipleChoiceRepository;

    public MultipleChoiceManager(MultipleChoiceRepository multipleChoiceRepository, FormService formService, QuestionRepository questionRepository) {
        super(questionRepository, formService);
        this.multipleChoiceRepository = multipleChoiceRepository;
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

        if (questionUpdateReq.getUpdateFields().contains(MultipleChoiceUpdateReqDto.Fields.options)) {

            var prevOptions = mc.getOptions();

            questionUpdateReq.getOptions().forEach(option -> {

                var action = option.getAction();

                if (action == ComplexQuestionUpdateAction.ADD) {

                    var mcOption = new MultipleChoiceOption();

                    mcOption.setMultipleChoice(mc);
                    mcOption.setOption(option.getOption());
                    mcOption.setOrderIndex(prevOptions.size());

                    prevOptions.add(mcOption);

                } else if (action == ComplexQuestionUpdateAction.UPDATE) {

                    var mcOption = prevOptions
                            .stream()
                            .filter(op -> op.getId().equals(option.getId()))
                            .findFirst()
                            .orElseThrow(() -> new IllegalArgumentException("Multiple choice option not found for Id: " + option.getId()));

                    mcOption.setOption(option.getOption());

                } else if (action == ComplexQuestionUpdateAction.DELETE) {

                    var optionToDelete = prevOptions
                            .stream()
                            .filter(op -> op.getId().equals(option.getId()))
                            .findFirst()
                            .orElseThrow(() -> new IllegalArgumentException("Multiple choice option not found for Id: " + option.getId()));

                    prevOptions.remove(optionToDelete);

                    prevOptions
                            .stream()
                            .sorted(Comparator.comparingInt(MultipleChoiceOption::getOrderIndex))
                            .forEach(op -> {

                                if (op.getOrderIndex() > optionToDelete.getOrderIndex()) {
                                    op.setOrderIndex(op.getOrderIndex() - 1);
                                }

                            });
                }
            });
        }

        return toQuestionResDto(multipleChoiceRepository.save(mc), question);
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
