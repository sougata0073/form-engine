package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.Date;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository("DATE_RESPONSE_REPOSITORY")
public interface DateRepository extends AnyTypeQuestionResponseRepository<Date, Long> {

    @Query(value = """
            select
            x.year as year,
            x.month as month,
            array_agg(to_char(x.date, 'YYYY-MM-DD"T"HH24:MI:SSTZH:TZM') order by x.date) dates,
            array_agg(x.dateCount order by x.date) dateCounts
            from (
                select
                extract(year from d.date)::int as year,
                extract(month from d.date)::int as month,
                d.date date,
                count(d.date) dateCount
                from dates d
                join question_responses qr
                on d.question_response_id = qr.id
                join form_responses fr
                on fr.id = qr.form_response_id
                where qr.question_id = :questionId
                group by year, month, d.date
            ) x
            group by x.year, x.month
            order by x.year, x.month
            """, nativeQuery = true)
    List<Tuple> getResponseDates(long questionId, Pageable pageable);

    @Query(value = """
            select
            d.date date,
            count(*) responseCount
            from form_responses fr
            left join question_responses  qr
            on fr.id = qr.form_response_id
            and qr.question_id = :questionId
            left join dates d
            on qr.id = d.question_response_id
            where fr.form_id = :formId
            group by d.date
            order by responseCount desc, d.date asc
            """, nativeQuery = true)
    List<Tuple> groupedByDate(UUID formId, long questionId, Pageable pageable);

    @Query("""
            select
            d.questionResponse.questionId questionId,
            d.date date
            from Date d
            where d.questionResponse.formResponse.id = :formResponseId
            """)
    List<Tuple> getDatesByFormResponse(long formResponseId);

    @Query(value = """
            select
            fr.id responseId,
            fr.user_id userId
            from form_responses fr
            left join question_responses qr
            on fr.id = qr.form_response_id
            and qr.question_id = :questionId
            left join dates d
            on qr.id = d.question_response_id
            where fr.form_id = :formId and (
                (cast(:response as timestamp with time zone) is null and d.date is null)
                or d.date = :response
            )
            order by fr.created_at, fr.id
            """, nativeQuery = true)
    List<Tuple> getResponseIdsByGroupedResponse(UUID formId, long questionId, Instant response, Pageable pageable);
}
