package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.MultipleChoiceGrid;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository("MULTIPLE_CHOICE_GRID_RESPONSE_REPOSITORY")
public interface MultipleChoiceGridRepository extends AnyTypeQuestionResponseRepository<MultipleChoiceGrid, Long> {

    @Query("""
            select
            mcg.questionResponse.questionId questionId,
            mcgr.rowId rowId,
            mcgr.responseColumnId responseColumnId,
            count(mcgr.responseColumnId) responseCount
            from MultipleChoiceGridRow mcgr
            join mcgr.multipleChoiceGrid mcg
            where mcg.questionResponse.formResponse.formId = :formId
            group by mcg.questionResponse.questionId, mcgr.rowId, mcgr.responseColumnId
            """)
    List<Tuple> getResponseOptionCount(UUID formId);

    @Query(value = """
            select
            mcgr.response_column_id columnId,
            count(*) responseCount
            from form_responses fr
            left join question_responses qr
            on qr.form_response_id = fr.id
            and qr.question_id = :questionId
            left join multiple_choice_grids mcg
            on mcg.question_response_id = qr.id
            left join multiple_choice_grid_rows mcgr
            on mcg.question_response_id = mcgr.multiple_choice_grid_id
            and mcgr.row_id = :rowId
            group by mcgr.response_column_id
            order by responseCount desc, mcgr.response_column_id asc
            """, nativeQuery = true)
    List<Tuple> groupedByResponseRowColumn(long questionId, long rowId, Pageable pageable);

    @Query(value = """
            select
            qr.question_id questionId,
            array_agg(mcgr.row_id order by mcgr.row_id) rowIds,
            array_agg(mcgr.response_column_id order by mcgr.row_id) columnIds
            from multiple_choice_grids mcg
            join multiple_choice_grid_rows mcgr
            on mcg.question_response_id = mcgr.multiple_choice_grid_id
            join question_responses qr
            on mcg.question_response_id = qr.id
            join form_responses fr
            on fr.id = qr.form_response_id
            where fr.id = :formResponseId
            group by qr.question_id
            """, nativeQuery = true)
    List<Tuple> getRowColumnIdsByFormResponse(long formResponseId);

    @Query(value = """
            select
              fr.id responseId,
              fr.user_id userId
            from
              form_responses fr
              left join question_responses qr on qr.form_response_id = fr.id
              and qr.question_id = :questionId
              left join multiple_choice_grids mcg on mcg.question_response_id = qr.id
              left join multiple_choice_grid_rows mcgr on mcg.question_response_id = mcgr.multiple_choice_grid_id
              and mcgr.row_id = :rowId
            where (:response is null and mcgr.response_column_id is null) or mcgr.response_column_id = :response
            order by fr.created_at, fr.id
            """, nativeQuery = true)
    List<Tuple> getResponseIdsByGroupedResponse(long questionId, Long rowId, Long response, Pageable pageable);
}
