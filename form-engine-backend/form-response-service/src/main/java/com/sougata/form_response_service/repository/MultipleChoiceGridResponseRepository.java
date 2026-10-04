package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.MultipleChoiceGridResponse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("MULTIPLE_CHOICE_GRID_RESPONSE_REPOSITORY")
public interface MultipleChoiceGridResponseRepository extends AnyTypeQuestionResponseRepository<MultipleChoiceGridResponse, Long> {

    @Query(value = """
            select multiple_choice_grid_responses_increment_or_create(
                cast(:batchResponses as jsonb)
            )
            """, nativeQuery = true)
    void createOrIncrement(String batchResponses);

}
