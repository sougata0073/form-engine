package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.DropdownResponse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository("DROPDOWN_RESPONSE_REPOSITORY")
public interface DropdownResponseRepository extends AnyTypeQuestionResponseRepository<DropdownResponse, Long> {

    @Query(value = """
            select dropdown_responses_increment_or_create(
                cast(:data as jsonb), :formResponseId, :incrementBy
            )
            """, nativeQuery = true)
    void createOrIncrement(String data, UUID formResponseId, Long incrementBy);

}
