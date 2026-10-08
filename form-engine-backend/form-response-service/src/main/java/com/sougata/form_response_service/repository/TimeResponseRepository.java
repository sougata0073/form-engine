package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.TimeResponse;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

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
            array_agg(to_char(tr.time, 'YYYY-MM-DD"T"HH24:MI:SS.MSOF') order by tr.time) as times,
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
}
