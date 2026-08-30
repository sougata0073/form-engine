package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.TickBoxGrid;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository("TICK_BOX_GRID_RESPONSE_REPOSITORY")
public interface TickBoxGridRepository extends AnyTypeQuestionResponseRepository<TickBoxGrid, Long> {

    @Query("""
            select
                tbg.questionResponse.questionId questionId,
                tbgr.rowId rowId,
                tbgc.responseOptionId responseOptionId,
                count(tbgc.responseOptionId) responseCount
            from TickBoxGridColumn tbgc
            join tbgc.tickBoxGridRow tbgr
            join tbgr.tickBoxGrid tbg
            where tbg.questionResponse.formResponse.formId = :formId
            group by
                tbg.questionResponse.questionId,
                tbgr.rowId,
                tbgc.responseOptionId
            order by
                tbgr.rowId,
                tbgc.responseOptionId
            """)
    List<Tuple> getResponseOptionCount(UUID formId);

    @Query(value = """
            select
            case
                when array_position(columnIds, null) = 1 and array_length(columnIds, 1) = 1 then null
                else columnIds
            end columnIds,
            count(*) as responseCount
            from (
                select
                array_agg(tbgc.response_option_id order by tbgc.response_option_id) as columnIds
                from form_responses fr
                left join question_responses qr
                on qr.form_response_id = fr.id
                and qr.question_id = :questionId
                left join tick_box_grids tbg
                on qr.id = tbg.question_response_id
                left join tick_box_grid_rows tbgr
                on tbg.question_response_id = tbgr.tick_box_grid_Id
                and tbgr.row_id = :rowId
                left join tick_box_grid_columns tbgc
                on tbgc.tick_box_grid_row_id = tbgr.id
                where fr.form_id = :formId
                group by fr.id
            ) x
            group by columnIds
            order by responseCount desc, columnIds asc
            """, nativeQuery = true)
    List<Tuple> groupedByResponseRowColumn(UUID formId, long questionId, long rowId, Pageable pageable);

    @Query(value = """
                select
            	qr.question_id questionId,
            	tbgr.row_id rowId,
            	array_agg(tbgc.response_option_id) columnIds
            	from tick_box_grids tbg
            	join tick_box_grid_rows tbgr
            	on tbg.question_response_id = tbgr.tick_box_grid_id
            	join tick_box_grid_columns tbgc
            	on tbgc.tick_box_grid_row_id = tbgr.id
            	join question_responses qr
            	on tbg.question_response_id = qr.id
            	join form_responses fr
            	on fr.id = qr.form_response_id
            	where fr.id = :formResponseId
            	group by qr.question_id, tbgr.row_id
            """, nativeQuery = true)
    List<Tuple> getRowColumnIdsByFormResponse(long formResponseId);

    @Query(value = """
        select
            fr.id responseId,
            fr.user_id userId
        from
            form_responses fr
            left join question_responses qr
                on qr.form_response_id = fr.id
                and qr.question_id = :questionId
            left join tick_box_grids tbg
                on qr.id = tbg.question_response_id
            left join tick_box_grid_rows tbgr
                on tbg.question_response_id = tbgr.tick_box_grid_id
                and tbgr.row_id = :rowId
            left join tick_box_grid_columns tbgc
                on tbgc.tick_box_grid_row_id = tbgr.id
        where fr.form_id = :formId
        group by
            fr.id
        having array_agg(
                tbgc.response_option_id
                order by tbgc.response_option_id
            ) = case
                    when cardinality(:response) = 1 and (:response)[1] is null then array[null::bigint]
                    else :response
            end
        order by
            fr.created_at,
            fr.id
    """, nativeQuery = true)
    List<Tuple> getResponseIdsByGroupedResponse(UUID formId, long questionId, Long rowId, Long[] response, Pageable pageable);
}
