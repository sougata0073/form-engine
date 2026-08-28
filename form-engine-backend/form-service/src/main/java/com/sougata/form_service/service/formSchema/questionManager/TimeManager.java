package com.sougata.form_service.service.formSchema.questionManager;

import com.sougata.form_service.constant.QuestionType;
import com.sougata.form_service.dto.question.request.TimePutReqDto;
import com.sougata.form_service.dto.question.response.TimeDetailsDto;
import com.sougata.form_service.dto.template.questionTemplate.TimeTemplateDetails;
import com.sougata.form_service.exception.QuestionNotFoundException;
import com.sougata.form_service.model.formSchema.Form;
import com.sougata.form_service.model.formSchema.Question;
import com.sougata.form_service.model.formSchema.Time;
import com.sougata.form_service.repository.formSchema.QuestionRepository;
import com.sougata.form_service.repository.formSchema.TimeRepository;
import com.sougata.form_service.service.formSchema.FormService;
import com.sougata.form_service.service.formSchema.QuestionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service("TIME_QUESTION_MANAGER")
public class TimeManager extends QuestionManager<Time, TimePutReqDto, TimeDetailsDto, TimeTemplateDetails> {

    private final TimeRepository timeRepository;

    public TimeManager(TimeRepository timeRepository, FormService formService, QuestionRepository questionRepository) {
        super(questionRepository, formService);
        this.timeRepository = timeRepository;
    }

    @Override
    public TimeDetailsDto get(UUID formId, Long questionId) {
        return toQuestionResDto(timeRepository.findByQuestionId(questionId).orElseThrow(() -> new QuestionNotFoundException(questionId)));
    }

    @Override
    @Transactional
    public TimeDetailsDto create(UUID formId, TimePutReqDto crudDto) {
        var newTime = new Time();

        var question = createQuestion(crudDto, formId);

        newTime.setQuestion(question);

        var saved = timeRepository.save(newTime);

        return toQuestionResDto(saved, question);
    }

    @Override
    @Transactional
    public TimeDetailsDto create(UUID formId, Long questionId, TimePutReqDto questionAddUpdateReq) {
        var newTime = new Time();

        var question = updateQuestion(questionId, questionAddUpdateReq);

        newTime.setQuestion(question);

        var saved = timeRepository.save(newTime);

        return toQuestionResDto(saved, question);
    }

    @Override
    @Transactional
    public TimeDetailsDto update(UUID formId, Long questionId, TimePutReqDto questionAddUpdateReq) {
        Time t = timeRepository.findByQuestionId(questionId)
                .orElseThrow(() -> new QuestionNotFoundException(QuestionType.TIME, questionId));

        var question = updateQuestion(questionId, questionAddUpdateReq);

        timeRepository.save(t);

        return toQuestionResDto(t, question);
    }

    @Override
    public TimeDetailsDto toQuestionResDto(Time childQuestion) {
        return toQuestionResDto(childQuestion, childQuestion.getQuestion());
    }

    @Override
    public TimeDetailsDto toQuestionResDto(Time childQuestion, Question parentQuestion) {
        var t = new TimeDetailsDto();

        populateCommonFields(parentQuestion, t);

        return t;
    }

    @Override
    public TimePutReqDto toQuestionAddUpdateReq(TimeDetailsDto questionRes) {
        var t = new TimePutReqDto();

        populateCommonFields(questionRes, t);

        return t;
    }

    @Override
    @Transactional
    public Time createFromTemplate(TimeTemplateDetails template, Form form) {
        var t = new Time();

        t.setQuestion(createQuestionFromTemplate(template, form));

        return timeRepository.save(t);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.TIME;
    }

    @Override
    public void delete(UUID formId, Long questionId) {
        timeRepository.deleteQuestion(questionId);
    }
}
