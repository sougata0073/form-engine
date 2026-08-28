package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.MultipleChoice;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository("MULTIPLE_CHOICE_RESPONSE_REPOSITORY")
public interface MultipleChoiceRepository extends AnyTypeQuestionResponseRepository<MultipleChoice, Long> {

    @Query("""
            select
            mc.questionResponse.questionId questionId,
            mc.responseOptionId responseOptionId,
            count(mc.responseOptionId) responseCount
            from MultipleChoice mc
            where mc.questionResponse.formResponse.formId = :formId
            group by mc.responseOptionId, mc.questionResponse.questionId
            """)
    List<Tuple> getResponseOptionCount(UUID formId);

    @Query(value = """
            select
            mc.response_option_id optionId,
            count(*) responseCount
            from form_responses fr
            left join question_responses qr
            on fr.id = qr.form_response_id
            and qr.question_id = :questionId
            left join multiple_choices mc
            on qr.id = mc.question_response_id
            group by mc.response_option_id
            order by responseCount desc, mc.response_option_id asc
            """, nativeQuery = true)
    List<Tuple> groupedByResponseOption(long questionId, Pageable pageable);

    @Query("""
            select
            mc.questionResponse.questionId questionId,
            mc.responseOptionId optionId
            from MultipleChoice mc
            where mc.questionResponse.formResponse.id = :formResponseId
            """)
    List<Tuple> getOptionIdsByFormResponse(long formResponseId);

    @Query(value = """
            select
            fr.id responseId,
            fr.user_id userId
            from form_responses fr
            left join question_responses qr
            on fr.id = qr.form_response_id
            and qr.question_id = :questionId
            left join multiple_choices mc
            on qr.id = mc.question_response_id
            where (
                (:response is null and mc.response_option_id is null)
                or mc.response_option_id = :response
            )
            order by fr.created_at, fr.id
            """, nativeQuery = true)
    List<Tuple> getResponseIdsByGroupedResponse(long questionId, Long response, Pageable pageable);
}
