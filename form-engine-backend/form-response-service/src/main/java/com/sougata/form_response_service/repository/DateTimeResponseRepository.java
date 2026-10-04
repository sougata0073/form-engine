package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.DateTimeResponse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("DATE_TIME_RESPONSE_REPOSITORY")
public interface DateTimeResponseRepository extends AnyTypeQuestionResponseRepository<DateTimeResponse, Long> {

    @Query(value = """
            select date_time_responses_increment_or_create(
                cast(:batchResponses as jsonb)
            )
            """, nativeQuery = true)
    void createOrIncrement(String batchResponses);

}
