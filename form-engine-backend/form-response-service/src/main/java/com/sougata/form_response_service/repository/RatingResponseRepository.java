package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.RatingResponse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("RATING_RESPONSE_REPOSITORY")
public interface RatingResponseRepository extends AnyTypeQuestionResponseRepository<RatingResponse, Long> {

    @Query(value = """
            select rating_responses_increment_or_create(
                cast(:batchResponses as jsonb)
            )
            """, nativeQuery = true)
    void createOrIncrement(String batchResponses);

}
