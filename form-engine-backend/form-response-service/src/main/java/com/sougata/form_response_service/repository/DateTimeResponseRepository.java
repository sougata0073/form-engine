package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.DateTimeResponse;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository("DATE_TIME_RESPONSE_REPOSITORY")
public interface DateTimeResponseRepository extends AnyTypeQuestionResponseRepository<DateTimeResponse, Long> {

    @Query(value = """
            select date_time_responses_increment_or_create(
                cast(:batchResponses as jsonb)
            )
            """, nativeQuery = true)
    void createOrIncrement(String batchResponses);

    @Query("""
            select
            dtr.dateTime dateTime,
            dtr.responseCount responseCount
            from DateTimeResponse dtr
            where dtr.questionId = :questionId
            order by dtr.responseCount desc, dtr.dateTime
            """)
    List<Tuple> groupedByDateTimes(Long questionId, Pageable pageable);

    @Query(value = """
            select
            date(dt.date_time) as date,
            dt.date_time as time,
            dt.response_count as timeCount
            from date_time_responses dt
            where dt.question_id = :questionId
            order by date, dt.date_time
            """, nativeQuery = true)
    List<Tuple> getResponseDateTimes(Long questionId, Pageable pageable);

    @Query(value = """
            select
            fri.form_response_id responseId,
            fri.user_id userId
            from date_time_responses dtr
            join question_response_form_response_individual qrfri
            on dtr.question_response_id = qrfri.question_response_id
            and dtr.question_id = :questionId and dtr.date_time = :groupedResponse
            join form_response_individuals fri
            on fri.form_response_id = qrfri.form_response_individual_id
            order by fri.form_response_id
            """, nativeQuery = true)
    List<Tuple> getResponseIdsByGroupedResponse(Long questionId, Instant groupedResponse, Pageable pageable);
}
