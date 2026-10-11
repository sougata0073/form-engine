package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.ParagraphResponse;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("PARAGRAPH_RESPONSE_REPOSITORY")
public interface ParagraphResponseRepository extends AnyTypeQuestionResponseRepository<ParagraphResponse, Long> {

    @Query(value = """
            select paragraph_responses_increment_or_create(
                cast(:batchResponses as jsonb)
            )
            """, nativeQuery = true)
    void createOrIncrement(String batchResponses);

    @Query("""
            select pr.text
            from ParagraphResponse pr
            where pr.questionId = :questionId
            order by pr.responseCount desc, pr.text
            """)
    List<String> getResponseTexts(Long questionId, Pageable pageable);

    @Query("""
            select
            p.text text,
            p.responseCount responseCount
            from ParagraphResponse p
            where p.questionId = :questionId
            order by p.responseCount desc, p.text
            """)
    List<Tuple> groupedByText(Long questionId, Pageable pageable);

    @Query(value = """
            select
            fri.form_response_id responseId,
            fri.user_id userId
            from paragraph_responses pr
            join question_response_form_response_individual qrfri
            on pr.question_response_id = qrfri.question_response_id
            and pr.question_id = :questionId and pr.text = :groupedResponse
            join form_response_individuals fri
            on fri.form_response_id = qrfri.form_response_individual_id
            order by fri.form_response_id
            """, nativeQuery = true)
    List<Tuple> getResponseIdsByGroupedResponse(Long questionId, String groupedResponse, Pageable pageable);
}
