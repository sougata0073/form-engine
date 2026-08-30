package com.sougata.form_service.service.formSchema.questionManager;

import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.question.details.DateDetailsDto;
import com.sougata.form_engine.dto.question.schemaRequest.DatePutReqDto;
import com.sougata.form_engine.dto.template.questionTemplate.DateTemplateDetails;
import com.sougata.form_service.exception.QuestionNotFoundException;
import com.sougata.form_service.model.formSchema.Date;
import com.sougata.form_service.model.formSchema.Form;
import com.sougata.form_service.model.formSchema.Question;
import com.sougata.form_service.repository.formSchema.DateRepository;
import com.sougata.form_service.repository.formSchema.QuestionRepository;
import com.sougata.form_service.service.formSchema.FormService;
import com.sougata.form_service.service.formSchema.QuestionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service("DATE_QUESTION_MANAGER")
public class DateManager extends QuestionManager<Date, DatePutReqDto, DateDetailsDto, DateTemplateDetails> {

    private final DateRepository dateRepository;

    public DateManager(DateRepository dateRepository, FormService formService, QuestionRepository questionRepository, QuestionRepository questionRepository1) {
        super(questionRepository, formService);
        this.dateRepository = dateRepository;
    }

    @Override
    public DateDetailsDto get(UUID formId, Long questionId) {
        return toQuestionResDto(dateRepository.findByQuestionId(questionId)
                .orElseThrow(() -> new QuestionNotFoundException(questionId)));
    }

    @Override
    @Transactional
    public DateDetailsDto create(UUID formId, DatePutReqDto crudDto) {
        var newDate = new Date();

        var question = createQuestion(crudDto, formId);

        newDate.setQuestion(question);

        var saved = dateRepository.save(newDate);

        return toQuestionResDto(saved, question);
    }

    @Override
    @Transactional
    public DateDetailsDto create(UUID formId, Long questionId, DatePutReqDto questionAddUpdateReq) {
        var newDate = new Date();

        var question = updateQuestion(questionId, questionAddUpdateReq);

        newDate.setQuestion(question);

        var saved = dateRepository.save(newDate);

        return toQuestionResDto(saved, question);
    }

    @Override
    @Transactional
    public DateDetailsDto update(UUID formId, Long questionId, DatePutReqDto questionAddUpdateReq) {
        Date date = dateRepository.findByQuestionId(questionId)
                .orElseThrow(() -> new QuestionNotFoundException(QuestionType.DATE, questionId));

        var question = updateQuestion(questionId, questionAddUpdateReq);

        dateRepository.save(date);

        return toQuestionResDto(date, question);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.DATE;
    }

    @Override
    public void delete(UUID formId, Long questionId) {
        dateRepository.deleteQuestion(questionId);
    }

    @Override
    public DateDetailsDto toQuestionResDto(Date childQuestion) {
        return toQuestionResDto(childQuestion, childQuestion.getQuestion());
    }

    @Override
    public DateDetailsDto toQuestionResDto(Date childQuestion, Question parentQuestion) {
        var d = new DateDetailsDto();

        populateCommonFields(parentQuestion, d);

        return d;
    }

    @Override
    public DatePutReqDto toQuestionAddUpdateReq(DateDetailsDto questionRes) {
        var d = new DatePutReqDto();

        populateCommonFields(questionRes, d);

        return d;
    }

    @Override
    @Transactional
    public Date createFromTemplate(DateTemplateDetails template, Form form) {
        var d = new Date();

        d.setQuestion(createQuestionFromTemplate(template, form));

        return dateRepository.save(d);
    }

}
