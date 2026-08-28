package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.Rating;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository("RATING_RESPONSE_REPOSITORY")
public interface RatingRepository extends AnyTypeQuestionResponseRepository<Rating, Long> {

    @Query("""
            select
            r.questionResponse.questionId questionId,
            r.rating rating,
            sum(r.rating) ratingSum,
            count(r.rating) responseCount
            from Rating r
            where r.questionResponse.formResponse.formId = :formId
            and r.rating is not null
            group by r.rating, r.questionResponse.questionId
            """)
    List<Tuple> getResponseRatingCount(UUID formId);

    @Query(value = """
            select
            r.rating rating,
            count(*) responseCount
            from form_responses fr
            left join question_responses qr
            on fr.id = qr.form_response_id
            and qr.question_id = :questionId
            left join ratings r
            on qr.id = r.question_response_id
            group by r.rating
            order by responseCount desc, r.rating asc
            """, nativeQuery = true)
    List<Tuple> groupedByRating(long questionId, Pageable pageable);

    @Query("""
            select
            r.questionResponse.questionId questionId,
            r.rating rating
            from Rating r
            where r.questionResponse.formResponse.id = :formResponseId
            """)
    List<Tuple> getRatingsByFormResponse(long formResponseId);

    @Query(value = """
            select
            fr.id responseId,
            fr.user_id userId
            from form_responses fr
            left join question_responses qr
            on fr.id = qr.form_response_id
            and qr.question_id = :questionId
            left join ratings r
            on qr.id = r.question_response_id
            where (
                (:response is null and r.rating is null)
                or r.rating = :response
            )
            order by fr.created_at, fr.id
            """, nativeQuery = true)
    List<Tuple> getResponseIdsByGroupedResponse(long questionId, Integer response, Pageable pageable);
}
