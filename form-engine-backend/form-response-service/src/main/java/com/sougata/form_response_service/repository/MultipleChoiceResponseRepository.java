package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.MultipleChoiceResponse;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("MULTIPLE_CHOICE_RESPONSE_REPOSITORY")
public interface MultipleChoiceResponseRepository extends AnyTypeQuestionResponseRepository<MultipleChoiceResponse, Long> {

    @Query(value = """
            select multiple_choice_responses_increment_or_create(
                cast(:batchResponses as jsonb)
            )
            """, nativeQuery = true)
    void createOrIncrement(String batchResponses);

    @Query("""
            select
            mc.optionId optionId,
            mc.responseCount responseCount
            from MultipleChoiceResponse mc
            where mc.questionId = :questionId
            order by mc.responseCount desc, mc.optionId
            """)
    List<Tuple> groupedByOptionIds(Long questionId, Pageable pageable);

    @Query(value = """
            select
            fri.form_response_id responseId,
            fri.user_id userId
            from multiple_choice_responses mcr
            join question_response_form_response_individual qrfri
            on mcr.question_response_id = qrfri.question_response_id
            and mcr.question_id = :questionId and mcr.option_id = :groupedResponse
            join form_response_individuals fri
            on fri.form_response_id = qrfri.form_response_individual_id
            order by fri.form_response_id
            """, nativeQuery = true)
    List<Tuple> getResponseIdsByGroupedResponse(Long questionId, Long groupedResponse, Pageable pageable);
}
