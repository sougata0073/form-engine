package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.ParagraphResponse;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

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
}
