package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.ShortAnswerResponse;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository("SHORT_ANSWER_RESPONSE_REPOSITORY")
public interface ShortAnswerResponseRepository extends AnyTypeQuestionResponseRepository<ShortAnswerResponse, Long> {

    @Query(value = """
            select short_answer_responses_increment_or_create(
                cast(:batchResponses as jsonb)
            )
            """, nativeQuery = true)
    void createOrIncrement(String batchResponses);

    @Query("""
            select sa.text
            from ShortAnswerResponse sa
            where sa.questionId = :questionId
            order by sa.responseCount desc, sa.text
            """)
    List<String> getResponseTexts(Long questionId, Pageable pageable);

    @Query("""
            select
            sa.text text,
            sa.responseCount responseCount
            from ShortAnswerResponse sa
            where sa.questionId = :questionId
            order by sa.responseCount desc, sa.text
            """)
    List<Tuple> groupedByText(Long questionId, Pageable pageable);
}
