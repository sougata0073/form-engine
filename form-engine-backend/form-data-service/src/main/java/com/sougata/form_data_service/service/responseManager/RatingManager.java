package com.sougata.form_data_service.service.responseManager;

import com.sougata.form_data_service.model.AnyTypeQuestionResponse;
import com.sougata.form_data_service.model.FormResponse;
import com.sougata.form_data_service.model.Rating;
import com.sougata.form_data_service.repository.QuestionResponseRepository;
import com.sougata.form_data_service.repository.RatingRepository;
import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.question.responseputrequest.RatingResponsePutReqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service("RATING_RESPONSE_MANAGER")
public class RatingManager extends ResponseManager<RatingResponsePutReqDto> {

    private final RatingRepository ratingRepository;

    @Autowired
    public RatingManager(RatingRepository ratingRepository, QuestionResponseRepository questionResponseRepository) {
        super(questionResponseRepository);
        this.ratingRepository = ratingRepository;
    }

    @Override
    @Async
    public CompletableFuture<Void> create(RatingResponsePutReqDto response, FormResponse formResponse) {
        Rating rating = new Rating();

        var qr = createQuestionResponse(response.getQuestionId(), formResponse);

        rating.setKey(new AnyTypeQuestionResponse.PartitionKey(response.getQuestionId(), qr.getKey().getQuestionResponseId()));
        rating.setRating(response.getRating());

        ratingRepository.save(rating);

        return CompletableFuture.completedFuture(null);
    }


    @Override
    public QuestionType getQuestionType() {
        return QuestionType.RATING;
    }

    @Override
    public void deleteResponsesByQuestionId(Long questionId) {
        ratingRepository.deleteAllByQuestionId(questionId);
    }

    @Override
    public void deleteResponsesByQuestionIdAndQuestionResponseId(Long questionId, Long questionResponseId) {
        ratingRepository.deleteAllByQuestionIdAndQuestionResponseId(questionId, questionResponseId);
    }

}
