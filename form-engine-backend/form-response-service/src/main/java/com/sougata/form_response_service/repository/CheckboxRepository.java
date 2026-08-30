package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.Checkbox;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository("CHECKBOX_RESPONSE_REPOSITORY")
public interface CheckboxRepository extends AnyTypeQuestionResponseRepository<Checkbox, Long> {

    @Query("""
            select
            cb.questionResponse.questionId questionId,
            cbo.responseOptionId responseOptionId,
            count(cbo.responseOptionId) responseCount
            from CheckboxOption cbo
            join cbo.checkbox cb
            where cb.questionResponse.formResponse.formId = :formId
            group by cbo.responseOptionId, cb.questionResponse.questionId
            """)
    List<Tuple> getResponseOptionCount(UUID formId);

    @Query(value = """
            select
            case
                when array_position(optionIds, null) = 1 and array_length(optionIds, 1) = 1 then null
                else optionIds
            end optionIds,
            count(*) as responsecount
            from (
                select
                array_agg(co.response_option_id order by co.response_option_id) as optionIds
                from form_responses fr
                left join question_responses qr
                on qr.form_response_id = fr.id
                and qr.question_id = :questionId
                left join checkboxes c
                on qr.id = c.question_response_id
                left join checkbox_options co
                on co.checkbox_id = c.question_response_id
                where fr.form_id = :formId
                group by fr.id
                order by optionIds
            ) responses
            group by optionIds
            order by responsecount desc, optionIds asc
            """, nativeQuery = true)
    List<Tuple> groupedByResponseOptions(UUID formId, long questionId, Pageable pageable);

    @Query(value = """
            select
            qr.question_id questionId,
            array_agg(cbo.response_option_id) optionIds
            from checkboxes cb
            join checkbox_options cbo
            on cb.question_response_id = cbo.checkbox_id
            join question_responses qr
            on cb.question_response_id = qr.id
            join form_responses fr
            on fr.id = qr.form_response_id
            where fr.id = :formResponseId
            group by qr.question_id
            """, nativeQuery = true)
    List<Tuple> getOptionIdsByFormResponse(long formResponseId);

    @Query(value = """
            select
              fr.id responseId,
              fr.user_id userId
            from
              form_responses fr
              left join question_responses qr on qr.form_response_id = fr.id
              and qr.question_id = :questionId
              left join checkboxes c on qr.id = c.question_response_id
              left join checkbox_options co on co.checkbox_id = c.question_response_id
            where fr.form_id = :formId
            group by
              fr.id
            having
              array_agg(
                co.response_option_id
                order by
                  co.response_option_id
              ) = case
                when (cardinality(:response) = 1 and :response[1] is null) then array[null::bigint]
                else :response
            end
            order by
              fr.created_at,
              fr.id
            """, nativeQuery = true)
    List<Tuple> getResponseIdsByGroupedResponse(UUID formId, long questionId, Long[] response, Pageable pageable);
}
