package com.sougata.form_data_service.service.responseManager;

import com.sougata.form_data_service.model.AnyTypeQuestionResponse;
import com.sougata.form_data_service.model.Rating;
import com.sougata.form_data_service.repository.RatingRepository;
import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.formResponse.individual.RatingResponseIndividualDto;
import com.sougata.form_engine.dto.question.responseputrequest.RatingResponsePutReqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service("RATING_RESPONSE_MANAGER")
public class RatingManager extends ResponseManager<RatingResponsePutReqDto, RatingResponseIndividualDto> {

    private final RatingRepository ratingRepository;

    @Autowired
    public RatingManager(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    @Override
    @Async
    public CompletableFuture<Void> create(UUID formId, UUID formResponseId, RatingResponsePutReqDto response) {
        Rating rating = new Rating();

        var key = new AnyTypeQuestionResponse.PartitionKey(
                formId,
                formResponseId,
                response.getQuestionId()
        );

        rating.setKey(key);
        rating.setRating(response.getRating());

        ratingRepository.save(rating);

        return CompletableFuture.completedFuture(null);
    }

    @Override
    @Async
    public CompletableFuture<List<RatingResponseIndividualDto>> getResponses(UUID formId, UUID formResponseId, Set<Long> questionIds) {
        var responses = ratingRepository.findAllByQuestionIds(formId, formResponseId, questionIds);

        return CompletableFuture.completedFuture(
                responses.stream().map(res -> {
                    var r = new RatingResponseIndividualDto();

                    r.setQuestionId(res.getKey().getQuestionId());
                    r.setQuestionType(getQuestionType());
                    r.setRating(res.getRating());

                    return r;
                }).toList()
        );
    }

    @Override
    @Async
    public CompletableFuture<Void> deleteAllByQuestionIds(UUID formId, UUID formResponseId, Set<Long> questionIds) {
        ratingRepository.deleteAllByQuestionIds(formId, formResponseId, questionIds);

        return CompletableFuture.completedFuture(null);
    }

    @Override
    @Async
    public CompletableFuture<Void> deleteAllByFormId(UUID formId) {
        ratingRepository.deleteAllByFormId(formId);

        return CompletableFuture.completedFuture(null);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.RATING;
    }

}
