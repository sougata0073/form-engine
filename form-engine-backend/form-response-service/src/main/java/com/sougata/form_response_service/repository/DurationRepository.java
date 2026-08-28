package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.Duration;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("DURATION_RESPONSE_REPOSITORY")
public interface DurationRepository extends AnyTypeQuestionResponseRepository<Duration, Long> {

    @Query(value = """
            select
            x.hours as hours,
            array_agg(x.minutes order by x.minutes, x.seconds) minutes,
            array_agg(x.seconds order by x.minutes, x.seconds) seconds,
            array_agg(x.minSecCount order by x.minutes, x.seconds) minSecCounts
            from (
                select
                d.hours hours,
                d.minutes minutes,
                d.seconds seconds,
                count((d.minutes, d.seconds)) minSecCount
                from durations d
                join question_responses qr
                on d.question_response_id = qr.id
                join form_responses fr
                on fr.id = qr.form_response_id
                where qr.question_id = :questionId
                group by d.hours, d.minutes, d.seconds
            ) x
            group by x.hours
            order by x.hours
            """, nativeQuery = true)
    List<Tuple> getResponseDurations(long questionId, Pageable pageable);

    @Query(value = """
            select
            d.hours hours,
            d.minutes minutes,
            d.seconds seconds,
            count(*) responseCount
            from form_responses fr
            left join question_responses  qr
            on fr.id = qr.form_response_id
            and qr.question_id = :questionId
            left join durations d
            on qr.id = d.question_response_id
            group by d.hours, d.minutes, d.seconds
            order by responseCount desc, d.hours asc, d.minutes asc, d.seconds asc
            """, nativeQuery = true)
    List<Tuple> groupedByDuration(long questionId, Pageable pageable);

    @Query("""
            select
            d.questionResponse.questionId questionId,
            d.hours hours,
            d.minutes minutes,
            d.seconds seconds
            from Duration d
            where d.questionResponse.formResponse.id = :formResponseId
            """)
    List<Tuple> getDurationsByFormResponse(long formResponseId);

    @Query(value = """
            select
            fr.id responseId,
            fr.user_id userId
            from form_responses fr
            left join question_responses qr
            on fr.id = qr.form_response_id
            and qr.question_id = :questionId
            left join durations d
            on qr.id = d.question_response_id
            where (
                (:hours is null and d.hours is null)
                or d.hours = :hours
            ) and (
                (:minutes is null and d.minutes is null)
                or d.minutes = :minutes
            ) and (
                (:seconds is null and d.seconds is null)
                or d.seconds = :seconds
            )
            order by fr.created_at, fr.id
            """, nativeQuery = true)
    List<Tuple> getResponseIdsByGroupedResponse(long questionId, Integer hours, Integer minutes, Integer seconds, Pageable pageable);
}
