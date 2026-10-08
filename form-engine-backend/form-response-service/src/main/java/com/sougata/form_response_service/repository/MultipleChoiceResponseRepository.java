package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.MultipleChoiceResponse;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

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
}
