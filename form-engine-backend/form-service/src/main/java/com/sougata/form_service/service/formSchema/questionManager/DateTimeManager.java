package com.sougata.form_service.service.formSchema.questionManager;

import com.sougata.form_service.constant.QuestionType;
import com.sougata.form_service.dto.question.request.DateTimePutReqDto;
import com.sougata.form_service.dto.question.response.DateTimeDetailsDto;
import com.sougata.form_service.dto.template.questionTemplate.DateTimeTemplateDetails;
import com.sougata.form_service.exception.QuestionNotFoundException;
import com.sougata.form_service.model.formSchema.DateTime;
import com.sougata.form_service.model.formSchema.Form;
import com.sougata.form_service.model.formSchema.Question;
import com.sougata.form_service.repository.formSchema.DateTimeRepository;
import com.sougata.form_service.repository.formSchema.QuestionRepository;
import com.sougata.form_service.service.formSchema.FormService;
import com.sougata.form_service.service.formSchema.QuestionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service("DATE_TIME_QUESTION_MANAGER")
public class DateTimeManager extends QuestionManager<DateTime, DateTimePutReqDto, DateTimeDetailsDto, DateTimeTemplateDetails> {

    private final DateTimeRepository dateTimeRepository;

    public DateTimeManager(DateTimeRepository dateTimeRepository, FormService formService, QuestionRepository questionRepository) {
        super(questionRepository, formService);
        this.dateTimeRepository = dateTimeRepository;
    }

    @Override
    public DateTimeDetailsDto get(UUID formId, Long questionId) {
        return toQuestionResDto(dateTimeRepository.findByQuestionId(questionId).orElseThrow(() -> new QuestionNotFoundException(questionId)));
    }

    @Override
    @Transactional
    public DateTimeDetailsDto create(UUID formId, DateTimePutReqDto crudDto) {
        var newDt = new DateTime();

        var question = createQuestion(crudDto, formId);

        newDt.setQuestion(question);

        var saved = dateTimeRepository.save(newDt);

        return toQuestionResDto(saved, question);
    }

    @Override
    @Transactional
    public DateTimeDetailsDto create(UUID formId, Long questionId, DateTimePutReqDto questionAddUpdateReq) {
        var newDt = new DateTime();

        var question = updateQuestion(questionId, questionAddUpdateReq);

        newDt.setQuestion(question);

        var saved = dateTimeRepository.save(newDt);

        return toQuestionResDto(saved, question);
    }

    @Override
    @Transactional
    public DateTimeDetailsDto update(UUID formId, Long questionId, DateTimePutReqDto questionAddUpdateReq) {
        var dt = dateTimeRepository.findByQuestionId(questionId)
                .orElseThrow(() -> new QuestionNotFoundException(QuestionType.DATE_TIME, questionId));

        var question = updateQuestion(questionId, questionAddUpdateReq);

        dateTimeRepository.save(dt);

        return toQuestionResDto(dt, question);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.DATE_TIME;
    }

    @Override
    public void delete(UUID formId, Long questionId) {
        dateTimeRepository.deleteQuestion(questionId);
    }

    @Override
    public DateTimeDetailsDto toQuestionResDto(DateTime childQuestion) {
        return toQuestionResDto(childQuestion, childQuestion.getQuestion());
    }

    @Override
    public DateTimeDetailsDto toQuestionResDto(DateTime childQuestion, Question parentQuestion) {
        var dt = new DateTimeDetailsDto();

        populateCommonFields(parentQuestion, dt);

        return dt;
    }

    @Override
    public DateTimePutReqDto toQuestionAddUpdateReq(DateTimeDetailsDto questionRes) {
        var dt = new DateTimePutReqDto();

        populateCommonFields(questionRes, dt);

        return dt;
    }

    @Override
    @Transactional
    public DateTime createFromTemplate(DateTimeTemplateDetails template, Form form) {
        var dt = new DateTime();

        dt.setQuestion(createQuestionFromTemplate(template, form));

        return dateTimeRepository.save(dt);
    }
}
