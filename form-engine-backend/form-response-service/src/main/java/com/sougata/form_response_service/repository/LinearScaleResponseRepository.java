package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.LinearScaleResponse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository("LINEAR_SCALE_RESPONSE_REPOSITORY")
public interface LinearScaleResponseRepository extends AnyTypeQuestionResponseRepository<LinearScaleResponse, Long> {

    @Query(value = """
            select linear_scale_responses_increment_or_create(
                cast(:data as jsonb), :formResponseId, :incrementBy
            )
            """, nativeQuery = true)
    void createOrIncrement(String data, UUID formResponseId, Long incrementBy);

}
