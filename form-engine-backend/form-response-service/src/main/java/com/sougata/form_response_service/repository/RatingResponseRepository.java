package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.RatingResponse;
import org.springframework.stereotype.Repository;

@Repository("RATING_RESPONSE_REPOSITORY")
public interface RatingResponseRepository extends AnyTypeQuestionResponseRepository<RatingResponse, Long> {

}
