package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.TimeResponse;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository("TIME_RESPONSE_REPOSITORY")
public interface TimeResponseRepository extends AnyTypeQuestionResponseRepository<TimeResponse, Long> {

    @Query(value = """
            select time_responses_increment_or_create(
                cast(:batchResponses as jsonb)
            )
            """, nativeQuery = true)
    void createOrIncrement(String batchResponses);

    @Query(value = """
            select
            extract(hour from tr.time)::integer as hour,
            array_agg(to_char(tr.time at time zone 'UTC', 'YYYY-MM-DD"T"HH24:MI:SS.MS') || '+00:00' order by tr.time) as times,
            array_agg(tr.response_count order by tr.time) as timeCounts
            from time_responses tr
            where tr.question_id = :questionId
            group by hour
            order by hour
            """, nativeQuery = true)
    List<Tuple> groupedByHour(Long questionId, Pageable pageable);

    @Query("""
            select
            t.time time,
            t.responseCount responseCount
            from TimeResponse t
            where t.questionId = :questionId
            order by t.responseCount desc, t.time
            """)
    List<Tuple> groupedByTime(UUID formId, Long questionId, Pageable pageable);

    @Query(value = """
            select
            fri.form_response_id responseId,
            fri.user_id userId
            from time_responses tr
            join question_response_form_response_individual qrfri
            on tr.question_response_id = qrfri.question_response_id
            and tr.question_id = :questionId and tr.time = :groupedResponse
            join form_response_individuals fri
            on fri.form_response_id = qrfri.form_response_individual_id
            order by fri.form_response_id
            """, nativeQuery = true)
    List<Tuple> getResponseIdsByGroupedResponse(Long questionId, Instant groupedResponse, Pageable pageable);
}
