package com.sougata.form_data_service.repository;

import com.sougata.form_data_service.model.Rating;
import org.springframework.stereotype.Repository;

@Repository("RATING_RESPONSE_REPOSITORY")
public interface RatingRepository extends AnyTypeQuestionResponseRepository<Rating, Long> {

}
