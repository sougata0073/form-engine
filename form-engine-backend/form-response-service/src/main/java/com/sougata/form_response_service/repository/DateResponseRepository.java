package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.DateResponse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository("DATE_RESPONSE_REPOSITORY")
public interface DateResponseRepository extends AnyTypeQuestionResponseRepository<DateResponse, Long> {

    @Query(value = """
            select date_responses_increment_or_create(
                cast(:data as jsonb), :formResponseId, :incrementBy
            )
            """, nativeQuery = true)
    void createOrIncrement(String data, UUID formResponseId, Long incrementBy);

}
