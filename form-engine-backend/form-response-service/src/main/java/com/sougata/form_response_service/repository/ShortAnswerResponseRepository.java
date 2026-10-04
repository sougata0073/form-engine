package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.ShortAnswerResponse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("SHORT_ANSWER_RESPONSE_REPOSITORY")
public interface ShortAnswerResponseRepository extends AnyTypeQuestionResponseRepository<ShortAnswerResponse, Long> {

    @Query(value = """
            select short_answer_responses_increment_or_create(
                cast(:batchResponses as jsonb)
            )
            """, nativeQuery = true)
    void createOrIncrement(String batchResponses);

}
