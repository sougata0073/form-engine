package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.LinearScaleResponse;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("LINEAR_SCALE_RESPONSE_REPOSITORY")
public interface LinearScaleResponseRepository extends AnyTypeQuestionResponseRepository<LinearScaleResponse, Long> {

    @Query(value = """
            select linear_scale_responses_increment_or_create(
                cast(:batchResponses as jsonb)
            )
            """, nativeQuery = true)
    void createOrIncrement(String batchResponses);

    @Query("""
            select
            l.scale scale,
            l.responseCount responseCount
            from LinearScaleResponse l
            where l.questionId = :questionId
            order by l.responseCount desc, l.scale
            """)
    List<Tuple> groupedByScale(Long questionId, Pageable pageable);

    @Query(value = """
            select
            fri.form_response_id responseId,
            fri.user_id userId
            from linear_scale_responses ls
            join question_response_form_response_individual qrfri
            on ls.question_response_id = qrfri.question_response_id
            and ls.question_id = :questionId and ls.scale = :groupedResponse
            join form_response_individuals fri
            on fri.form_response_id = qrfri.form_response_individual_id
            order by fri.form_response_id
            """, nativeQuery = true)
    List<Tuple> getResponseIdsByGroupedResponse(Long questionId, Integer groupedResponse, Pageable pageable);
}
