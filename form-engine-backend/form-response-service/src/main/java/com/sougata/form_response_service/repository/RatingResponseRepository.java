package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.RatingResponse;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

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
}
