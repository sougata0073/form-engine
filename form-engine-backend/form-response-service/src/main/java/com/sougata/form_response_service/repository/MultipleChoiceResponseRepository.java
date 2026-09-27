package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.MultipleChoiceResponse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository("MULTIPLE_CHOICE_RESPONSE_REPOSITORY")
public interface MultipleChoiceResponseRepository extends AnyTypeQuestionResponseRepository<MultipleChoiceResponse, Long> {

    @Query(value = """
            select multiple_choice_responses_increment_or_create(
                cast(:data as jsonb), :formResponseId, :incrementBy
            )
            """, nativeQuery = true)
    void createOrIncrement(String data, UUID formResponseId, Long incrementBy);

}
