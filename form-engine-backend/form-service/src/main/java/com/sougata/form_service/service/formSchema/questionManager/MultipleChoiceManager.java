package com.sougata.form_service.service.formSchema.questionManager;

import com.sougata.form_service.constant.QuestionType;
import com.sougata.form_service.dto.question.request.MultipleChoicePutReqDto;
import com.sougata.form_service.dto.question.response.MultipleChoiceDetailsDto;
import com.sougata.form_service.dto.template.questionTemplate.MultipleChoiceTemplateDetails;
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

import java.util.*;
import java.util.stream.Collectors;

@Service("MULTIPLE_CHOICE_QUESTION_MANAGER")
public class MultipleChoiceManager extends QuestionManager<MultipleChoice, MultipleChoicePutReqDto, MultipleChoiceDetailsDto, MultipleChoiceTemplateDetails> {

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
    public MultipleChoiceDetailsDto create(UUID formId, MultipleChoicePutReqDto crudDto) {
        var newMc = new MultipleChoice();

        var question = createQuestion(crudDto, formId);

        setPropertiesForNew(crudDto, newMc, question);

        var saved = multipleChoiceRepository.save(newMc);

        return toQuestionResDto(saved, question);
    }

    @Override
    @Transactional
    public MultipleChoiceDetailsDto create(UUID formId, Long questionId, MultipleChoicePutReqDto questionAddUpdateReq) {
        var newMc = new MultipleChoice();

        var question = updateQuestion(questionId, questionAddUpdateReq);

        setPropertiesForNew(questionAddUpdateReq, newMc, question);

        var saved = multipleChoiceRepository.save(newMc);

        return toQuestionResDto(saved, question);
    }

    @Override
    @Transactional
    public MultipleChoiceDetailsDto update(UUID formId, Long questionId, MultipleChoicePutReqDto questionAddUpdateReq) {
        MultipleChoice mc = multipleChoiceRepository.findByQuestionId(questionId)
                .orElseThrow(() -> new QuestionNotFoundException(QuestionType.MULTIPLE_CHOICE, questionId));

        var question = updateQuestion(questionId, questionAddUpdateReq);

        Map<Long, MultipleChoiceOption> existingOptions = mc.getOptions().stream()
                .collect(Collectors.toMap(MultipleChoiceOption::getId, option -> option));

        Set<Long> requestOptionIds = questionAddUpdateReq.getOptions().stream()
                .map(MultipleChoicePutReqDto.Option::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        mc.getOptions().removeIf(option -> !requestOptionIds.contains(option.getId()));

        for (int i = 0; i < questionAddUpdateReq.getOptions().size(); i++) {
            var dto = questionAddUpdateReq.getOptions().get(i);

            if (dto.getId() == null) {
                MultipleChoiceOption option = new MultipleChoiceOption();
                option.setOption(dto.getOption());
                option.setOrderIndex(i);
                option.setMultipleChoice(mc);

                mc.getOptions().add(option);
            } else {
                MultipleChoiceOption option = existingOptions.get(dto.getId());

                if (option == null) {
                    throw new IllegalArgumentException("Invalid multiple choice option id: " + dto.getId());
                }

                option.setOption(dto.getOption());
                option.setOrderIndex(i);
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
    public MultipleChoicePutReqDto toQuestionAddUpdateReq(MultipleChoiceDetailsDto questionRes) {
        var mc = new MultipleChoicePutReqDto();

        populateCommonFields(questionRes, mc);

        mc.setOptions(
                questionRes.getOptions().stream()
                        .map(op -> new MultipleChoicePutReqDto.Option(null, op.getOption()))
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
    public void delete(UUID formId, Long questionId) {
        multipleChoiceRepository.deleteQuestion(questionId);
    }

    private void setPropertiesForNew(MultipleChoicePutReqDto source, MultipleChoice target, Question question) {
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
