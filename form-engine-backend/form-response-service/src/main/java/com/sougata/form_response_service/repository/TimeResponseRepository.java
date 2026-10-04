package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.TimeResponse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("TIME_RESPONSE_REPOSITORY")
public interface TimeResponseRepository extends AnyTypeQuestionResponseRepository<TimeResponse, Long> {

    @Query(value = """
            select time_responses_increment_or_create(
                cast(:batchResponses as jsonb)
            )
            """, nativeQuery = true)
    void createOrIncrement(String batchResponses);

}
