package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.DateResponse;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("DATE_RESPONSE_REPOSITORY")
public interface DateResponseRepository extends AnyTypeQuestionResponseRepository<DateResponse, Long> {

    @Query(value = """
            select date_responses_increment_or_create(
                cast(:batchResponses as jsonb)
            )
            """, nativeQuery = true)
    void createOrIncrement(String batchResponses);

    @Query("""
            select
            dr.date date,
            dr.responseCount responseCount
            from DateResponse dr
            where dr.questionId = :questionId
            order by dr.responseCount desc, dr.date
            """)
    List<Tuple> groupedByDates(Long questionId, Pageable pageable);

    @Query(value = """
            select
            extract(year from dr.date)::integer as year,
            extract(month from dr.date)::integer as month,
            array_agg(to_char(dr.date, 'YYYY-MM-DD"T"HH24:MI:SS.MSOF') order by dr.date) as dates,
            array_agg(dr.response_count order by dr.date) as dateCounts
            from date_responses dr
            where dr.question_id = :questionId
            group by year, month
            order by year, month
            """, nativeQuery = true)
    List<Tuple> groupedByYearMonth(Long questionId, Pageable pageable);

}
