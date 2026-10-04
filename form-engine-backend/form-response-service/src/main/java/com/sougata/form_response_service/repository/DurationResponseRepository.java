package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.DurationResponse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("DURATION_RESPONSE_REPOSITORY")
public interface DurationResponseRepository extends AnyTypeQuestionResponseRepository<DurationResponse, Long> {

    @Query(value = """
            select duration_responses_increment_or_create(
                cast(:batchResponses as jsonb)
            )
            """, nativeQuery = true)
    void createOrIncrement(String batchResponses);

}
