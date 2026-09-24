package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.CheckboxResponse;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository("CHECKBOX_RESPONSE_REPOSITORY")
public interface CheckboxResponseRepository extends AnyTypeQuestionResponseRepository<CheckboxResponse, Long> {

    @Modifying
    @Transactional
    @Query("update CheckboxResponse cr set cr.responseCount = cr.responseCount + :incrementBy where cr.optionId in :optionIds")
    void incrementResponseCountByOptionIds(Set<Long> optionIds, Long incrementBy);

    @Query(value = """
            insert into question_response_form_response_individual
            (question_response_id, form_response_individual_id)
            select
            cr.question_response_id,
            :formResponseId
            from checkbox_responses cr
            where cr.option_id in :optionIds
            """, nativeQuery = true)
    @Modifying
    @Transactional
    void insertFormResponseIndividualByOptionIds(UUID formResponseId, Set<Long> optionIds);

}
