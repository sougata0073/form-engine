package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.MultipleChoiceGridResponse;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("MULTIPLE_CHOICE_GRID_RESPONSE_REPOSITORY")
public interface MultipleChoiceGridResponseRepository extends AnyTypeQuestionResponseRepository<MultipleChoiceGridResponse, Long> {

    @Query(value = """
            select multiple_choice_grid_responses_increment_or_create(
                cast(:batchResponses as jsonb)
            )
            """, nativeQuery = true)
    void createOrIncrement(String batchResponses);

    @Query(value = """
            select
            fri.form_response_id responseId,
            fri.user_id userId
            from multiple_choice_grid_responses mcgr
            join question_response_form_response_individual qrfri
            on mcgr.question_response_id = qrfri.question_response_id
            and mcgr.question_id = :questionId
            and mcgr.row_id = :rowIdResponse
            and mcgr.column_id = :columnIdResponse
            join form_response_individuals fri
            on fri.form_response_id = qrfri.form_response_individual_id
            order by fri.form_response_id
            """, nativeQuery = true)
    List<Tuple> getResponseIdsByGroupedResponse(Long questionId, Long rowIdResponse, Long columnIdResponse, Pageable pageable);

    @Query("""
            select
            mcgr.columnId columnId,
            mcgr.responseCount responseCount
            from MultipleChoiceGridResponse mcgr
            where mcgr.questionId = :questionId
            and mcgr.rowId = :rowId
            order by mcgr.responseCount desc, mcgr.columnId
            """)
    List<Tuple> groupedByRowColumn(Long questionId, Long rowId, Pageable pageable);
}
