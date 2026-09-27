package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.DateTimeResponse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository("DATE_TIME_RESPONSE_REPOSITORY")
public interface DateTimeResponseRepository extends AnyTypeQuestionResponseRepository<DateTimeResponse, Long> {

    @Query(value = """
            select date_time_responses_increment_or_create(
                cast(:data as jsonb), :formResponseId, :incrementBy
            )
            """, nativeQuery = true)
    void createOrIncrement(String data, UUID formResponseId, Long incrementBy);

}
