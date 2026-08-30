package com.sougata.form_service.service.formSchema.questionManager;

import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.question.details.RatingDetailsDto;
import com.sougata.form_engine.dto.question.schemaputrequest.RatingPutReqDto;
import com.sougata.form_engine.dto.template.questionTemplate.RatingTemplateDetails;
import com.sougata.form_service.exception.QuestionNotFoundException;
import com.sougata.form_service.model.formSchema.Form;
import com.sougata.form_service.model.formSchema.Question;
import com.sougata.form_service.model.formSchema.Rating;
import com.sougata.form_service.repository.formSchema.QuestionRepository;
import com.sougata.form_service.repository.formSchema.RatingRepository;
import com.sougata.form_service.service.formSchema.FormService;
import com.sougata.form_service.service.formSchema.QuestionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service("RATING_QUESTION_MANAGER")
public class RatingManager extends QuestionManager<Rating, RatingPutReqDto, RatingDetailsDto, RatingTemplateDetails> {

    private final RatingRepository ratingRepository;

    public RatingManager(RatingRepository ratingRepository, FormService formService, QuestionRepository questionRepository) {
        super(questionRepository, formService);
        this.ratingRepository = ratingRepository;
    }

    @Override
    public RatingDetailsDto get(UUID formId, Long questionId) {
        return toQuestionResDto(ratingRepository.findByQuestionId(questionId).orElseThrow(() -> new QuestionNotFoundException(questionId)));
    }

    @Override
    @Transactional
    public RatingDetailsDto create(UUID formId, RatingPutReqDto crudDto) {
        var newR = new Rating();

        var question = createQuestion(crudDto, formId);

        setPropertiesForNew(crudDto, newR, question);

        var saved = ratingRepository.save(newR);

        return toQuestionResDto(saved, question);
    }

    @Override
    @Transactional
    public RatingDetailsDto create(UUID formId, Long questionId, RatingPutReqDto questionAddUpdateReq) {
        var newR = new Rating();

        var question = updateQuestion(questionId, questionAddUpdateReq);

        setPropertiesForNew(questionAddUpdateReq, newR, question);

        var saved = ratingRepository.save(newR);

        return toQuestionResDto(saved, question);
    }

    @Override
    @Transactional
    public RatingDetailsDto update(UUID formId, Long questionId, RatingPutReqDto questionAddUpdateReq) {
        Rating r = ratingRepository.findByQuestionId(questionId)
                .orElseThrow(() -> new QuestionNotFoundException(getQuestionType(), questionId));

        var question = updateQuestion(questionId, questionAddUpdateReq);

        r.setMaxRatingNumber(questionAddUpdateReq.getMaxRatingNumber());
        r.setRatingIcon(questionAddUpdateReq.getRatingIcon());

        ratingRepository.save(r);

        return toQuestionResDto(r, question);
    }

    @Override
    public RatingDetailsDto toQuestionResDto(Rating childQuestion) {
        return toQuestionResDto(childQuestion, childQuestion.getQuestion());
    }

    @Override
    public RatingDetailsDto toQuestionResDto(Rating childQuestion, Question parentQuestion) {
        var r = new RatingDetailsDto();

        populateCommonFields(parentQuestion, r);

        r.setRatingIcon(childQuestion.getRatingIcon());
        r.setMaxRatingNumber(childQuestion.getMaxRatingNumber());

        return r;
    }

    @Override
    public RatingPutReqDto toQuestionAddUpdateReq(RatingDetailsDto questionRes) {
        var r = new RatingPutReqDto();

        populateCommonFields(questionRes, r);

        r.setMaxRatingNumber(questionRes.getMaxRatingNumber());
        r.setRatingIcon(questionRes.getRatingIcon());

        return r;
    }

    @Override
    @Transactional
    public Rating createFromTemplate(RatingTemplateDetails template, Form form) {
        var r = new Rating();

        r.setQuestion(createQuestionFromTemplate(template, form));
        r.setMaxRatingNumber(template.getMaxRatingNumber());
        r.setRatingIcon(template.getRatingIcon());

        return ratingRepository.save(r);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.RATING;
    }

    @Override
    public void delete(UUID formId, Long questionId) {
        ratingRepository.deleteQuestion(questionId);
    }

    private void setPropertiesForNew(RatingPutReqDto source, Rating target, Question question) {
        target.setQuestion(question);
        target.setMaxRatingNumber(source.getMaxRatingNumber());
        target.setRatingIcon(source.getRatingIcon());
    }
}
