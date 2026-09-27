package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.ParagraphResponse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository("PARAGRAPH_RESPONSE_REPOSITORY")
public interface ParagraphResponseRepository extends AnyTypeQuestionResponseRepository<ParagraphResponse, Long> {

    @Query(value = """
            select paragraph_responses_increment_or_create(
                cast(:data as jsonb), :formResponseId, :incrementBy
            )
            """, nativeQuery = true)
    void createOrIncrement(String data, UUID formResponseId, Long incrementBy);

}
