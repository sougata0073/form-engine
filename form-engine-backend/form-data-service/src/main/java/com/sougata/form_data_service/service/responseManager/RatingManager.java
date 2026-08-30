package com.sougata.form_data_service.service.responseManager;

import com.sougata.form_data_service.model.FormResponse;
import com.sougata.form_data_service.model.Rating;
import com.sougata.form_data_service.repository.QuestionResponseRepository;
import com.sougata.form_data_service.repository.RatingRepository;
import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.question.responseRequest.RatingResponsePutReqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service("RATING_RESPONSE_MANAGER")
public class RatingManager extends ResponseManager<RatingResponsePutReqDto> {

    private final RatingRepository ratingRepository;

    @Autowired
    public RatingManager(RatingRepository ratingRepository, QuestionResponseRepository questionResponseRepository) {
        super(questionResponseRepository);
        this.ratingRepository = ratingRepository;
    }

    @Override
    @Transactional
    public void create(RatingResponsePutReqDto response, FormResponse formResponse) {
        Rating rating = new Rating();

        var qr = createQuestionResponse(response.getQuestionId(), formResponse);

        rating.setRating(response.getRating());
        rating.setQuestionResponse(qr);

        ratingRepository.save(rating);
    }


    @Override
    public QuestionType getQuestionType() {
        return QuestionType.RATING;
    }

    @Override
    public void deleteResponsesByQuestion(UUID formId, Long questionId) {
        ratingRepository.deleteAllByFormIdAndQuestionId(formId, questionId);
    }

    @Override
    public void deleteResponsesByFormResponse(UUID formId, Long formResponseId) {
        ratingRepository.deleteAllByFormIdAndFormResponseId(formId, formResponseId);
    }
}
