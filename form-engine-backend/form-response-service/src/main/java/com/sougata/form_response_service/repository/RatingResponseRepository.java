package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.RatingResponse;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("RATING_RESPONSE_REPOSITORY")
public interface RatingResponseRepository extends AnyTypeQuestionResponseRepository<RatingResponse, Long> {

    @Query(value = """
            select rating_responses_increment_or_create(
                cast(:batchResponses as jsonb)
            )
            """, nativeQuery = true)
    void createOrIncrement(String batchResponses);

    @Query("""
            select
            r.rating rating,
            r.responseCount responseCount
            from RatingResponse r
            where r.questionId = :questionId
            order by r.responseCount desc, r.rating
            """)
    List<Tuple> groupedByRating(Long questionId, Pageable pageable);

    @Query(value = """
            select
            fri.form_response_id responseId,
            fri.user_id userId
            from rating_responses rr
            join question_response_form_response_individual qrfri
            on rr.question_response_id = qrfri.question_response_id
            and rr.question_id = :questionId and rr.rating = :groupedResponse
            join form_response_individuals fri
            on fri.form_response_id = qrfri.form_response_individual_id
            order by fri.form_response_id
            """, nativeQuery = true)
    List<Tuple> getResponseIdsByGroupedResponse(Long questionId, Integer groupedResponse, Pageable pageable);
}
