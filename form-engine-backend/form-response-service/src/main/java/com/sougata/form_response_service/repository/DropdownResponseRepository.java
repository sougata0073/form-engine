package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.DropdownResponse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("DROPDOWN_RESPONSE_REPOSITORY")
public interface DropdownResponseRepository extends AnyTypeQuestionResponseRepository<DropdownResponse, Long> {

    @Query(value = """
            select dropdown_responses_increment_or_create(
                cast(:batchResponses as jsonb)
            )
            """, nativeQuery = true)
    void createOrIncrement(String batchResponses);

}
