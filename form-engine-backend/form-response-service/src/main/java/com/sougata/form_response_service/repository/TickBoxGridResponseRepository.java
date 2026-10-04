package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.TickBoxGridResponse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("TICK_BOX_GRID_RESPONSE_REPOSITORY")
public interface TickBoxGridResponseRepository extends AnyTypeQuestionResponseRepository<TickBoxGridResponse, Long> {

    @Query(value = """
            select tick_box_grid_responses_increment_or_create(
                cast(:batchResponses as jsonb)
            )
            """, nativeQuery = true)
    void createOrIncrement(String batchResponses);

}
