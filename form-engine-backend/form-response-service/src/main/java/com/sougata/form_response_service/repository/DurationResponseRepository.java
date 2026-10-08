package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.DurationResponse;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("DURATION_RESPONSE_REPOSITORY")
public interface DurationResponseRepository extends AnyTypeQuestionResponseRepository<DurationResponse, Long> {

    @Query(value = """
            select duration_responses_increment_or_create(
                cast(:batchResponses as jsonb)
            )
            """, nativeQuery = true)
    void createOrIncrement(String batchResponses);

    @Query(value = """
            select
            dr.hours as hours,
            array_agg(dr.minutes order by dr.minutes, dr.seconds) as minutes,
            array_agg(dr.seconds order by dr.minutes, dr.seconds) as seconds,
            array_agg(dr.response_count order by dr.minutes, dr.seconds) as responseCounts
            from duration_responses dr
            where dr.question_id = :questionId
            group by dr.hours
            order by dr.hours
            """, nativeQuery = true)
    List<Tuple> groupedByHour(Long questionId, Pageable pageable);

    @Query("""
            select
            d.hours hours,
            d.minutes minutes,
            d.seconds seconds,
            d.responseCount responseCount
            from DurationResponse d
            where d.questionId = :questionId
            order by d.responseCount desc, d.hours, d.minutes, d.seconds
            """)
    List<Tuple> groupedByDuration(Long questionId, Pageable pageable);
}
