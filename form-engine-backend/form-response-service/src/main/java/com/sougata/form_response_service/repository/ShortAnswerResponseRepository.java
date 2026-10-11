package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.ShortAnswerResponse;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    @Query(value = """
            select
            fri.form_response_id responseId,
            fri.user_id userId
            from short_answer_responses sa
            join question_response_form_response_individual qrfri
            on sa.question_response_id = qrfri.question_response_id
            and sa.question_id = :questionId and sa.text = :groupedResponse
            join form_response_individuals fri
            on fri.form_response_id = qrfri.form_response_individual_id
            order by fri.form_response_id
            """, nativeQuery = true)
    List<Tuple> getResponseIdsByGroupedResponse(Long questionId, String groupedResponse, Pageable pageable);
}
