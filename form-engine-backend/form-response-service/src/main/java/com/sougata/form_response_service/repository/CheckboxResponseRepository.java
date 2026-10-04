package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.CheckboxResponse;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("CHECKBOX_RESPONSE_REPOSITORY")
public interface CheckboxResponseRepository extends AnyTypeQuestionResponseRepository<CheckboxResponse, Long> {

    @Query(value = """
            select checkbox_responses_increment_or_create(
                cast(:batchResponses as jsonb)
            )
            """, nativeQuery = true)
    void createOrIncrement(String batchResponses);

    @Query(value = """
            select
            x.option_ids optionIds,
            count(x.option_ids) responseCount
            from (
                select
                array_agg(cr.option_id order by cr.option_id) option_ids
                from checkbox_responses cr
                join question_response_form_response_individual qrfri
                on cr.question_response_id = qrfri.question_response_id
                and cr.question_id = :questionId
                group by qrfri.form_response_individual_id
            ) x
            group by x.option_ids
            order by responseCount desc
            """, nativeQuery = true
    )
    List<Tuple> groupedByOptionIds(Long questionId, Pageable pageable);

    @Query(value = """
            select
            fri.form_response_id responseId,
            fri.user_id userId
            from
            checkbox_responses cr
            join question_response_form_response_individual qrfri\s
            on cr.question_response_id = qrfri.question_response_id
            and cr.question_id = :questionId
            join form_response_individuals fri
            on qrfri.form_response_individual_id = fri.form_response_id
            group by
            fri.form_response_id
            having array_agg(cr.option_id order by cr.option_id) = :groupedResponse
            order by fri.form_response_id
            """, nativeQuery = true)
    List<Tuple> getResponseIdsByGroupedResponse(Long questionId, Long[] groupedResponse, Pageable pageable);
}
