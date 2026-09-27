package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.CheckboxResponse;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Repository("CHECKBOX_RESPONSE_REPOSITORY")
public interface CheckboxResponseRepository extends AnyTypeQuestionResponseRepository<CheckboxResponse, Long> {

    @Modifying
    @Transactional
    @Query("""
            update
            CheckboxResponse cr
            set cr.responseCount = cr.responseCount + :incrementBy
            where cr.questionResponse.questionResponseSummary.questionId = :questionId
            and cr.optionId in :optionIds
            """)
    void incrementResponseCountByOptionIds(Long questionId, Set<Long> optionIds, Long incrementBy);

    @Modifying
    @Transactional
    @Query(value = """
            insert into question_response_form_response_individual
            (question_response_id, form_response_individual_id)
            select
            cr.question_response_id,
            :formResponseId
            from checkbox_responses cr
            join question_responses qr
            on cr.question_response_id = qr.id
            where qr.question_response_summary_question_id = :questionId
            and cr.option_id in :optionIds
            """, nativeQuery = true)
    void insertFormResponseIndividualByOptionIds(Long questionId, UUID formResponseId, Set<Long> optionIds);

    @Query(value = """
            select checkbox_responses_increment_or_create(
                cast(:data as jsonb), :formResponseId, :incrementBy
            )
            """, nativeQuery = true)
    void createOrIncrement(String data, UUID formResponseId, Long incrementBy);
}
