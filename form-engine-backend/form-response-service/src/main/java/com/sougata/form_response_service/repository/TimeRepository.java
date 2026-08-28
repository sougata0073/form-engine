package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.Time;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository("TIME_RESPONSE_REPOSITORY")
public interface TimeRepository extends AnyTypeQuestionResponseRepository<Time, Long> {

    @Query(value = """
            select
            x.hour as hour,
            array_agg(to_char(x.time, 'YYYY-MM-DD"T"HH24:MI:SSOF') order by x.time) times,
            array_agg(x.timeCount order by x.time) timeCounts
            from (
                select
                extract(hour from t.time)::int as hour,
                t.time time,
                count(t.time) timeCount
                from times t
                join question_responses qr
                on t.question_response_id = qr.id
                join form_responses fr
                on fr.id = qr.form_response_id
                where qr.question_id = :questionId
                group by hour, time
            ) x
            group by x.hour
            order by x.hour
            """, nativeQuery = true)
    List<Tuple> getResponseTimes(long questionId, Pageable pageable);

    @Query(value = """
            select
            t.time time,
            count(*) responseCount
            from form_responses fr
            left join question_responses qr
            on fr.id = qr.form_response_id
            and qr.question_id = :questionId
            left join times t
            on qr.id = t.question_response_id
            group by t.time
            order by responseCount desc, t.time asc
            """, nativeQuery = true)
    List<Tuple> groupedByTime(long questionId, Pageable pageable);

    @Query("""
            select
            t.questionResponse.questionId questionId,
            t.time time
            from Time t
            where t.questionResponse.formResponse.id = :formResponseId
            """)
    List<Tuple> getTimesByFormResponse(long formResponseId);

    @Query(value = """
            select
            fr.id responseId,
            fr.user_id userId
            from form_responses fr
            left join question_responses qr
            on fr.id = qr.form_response_id
            and qr.question_id = :questionId
            left join times t
            on qr.id = t.question_response_id
            where (
                (cast(:response as timestamp with time zone) is null and t.time is null)
                or t.time = :response
            )
            order by fr.created_at, fr.id
            """, nativeQuery = true)
    List<Tuple> getResponseIdsByGroupedResponse(long questionId, Instant response, Pageable pageable);

}
