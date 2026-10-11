package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.TickBoxGridResponse;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("TICK_BOX_GRID_RESPONSE_REPOSITORY")
public interface TickBoxGridResponseRepository extends AnyTypeQuestionResponseRepository<TickBoxGridResponse, Long> {

    @Query(value = """
            select tick_box_grid_responses_increment_or_create(
                cast(:batchResponses as jsonb)
            )
            """, nativeQuery = true)
    void createOrIncrement(String batchResponses);

//    select
//    x.column_ids columnIds,
//    count(x.column_ids) responseCount
//    from (
//            select
//                    array_agg(tbgr.column_id order by tbgr.column_id) column_ids
//    from tick_box_grid_responses tbgr
//    join question_response_form_response_individual qrfri
//    on tbgr.question_response_id = qrfri.question_response_id
//    and tbgr.question_id = 876457279894633527
//    and tbgr.row_id = 876457310940870756
//    group by qrfri.form_response_individual_id
//  ) x
//    group by x.column_ids
//    order by responseCount desc, columnIds;

    @Query(value = """
            select
            fri.form_response_id responseId,
            fri.user_id userId
            from
            tick_box_grid_responses tbgr
            join question_response_form_response_individual qrfri
            on tbgr.question_response_id = qrfri.question_response_id
            and tbgr.question_id = :questionId
            and tbgr.row_id = :rowIdResponse
            join form_response_individuals fri
            on qrfri.form_response_individual_id = fri.form_response_id
            group by fri.form_response_id
            having array_agg(tbgr.column_id order by tbgr.column_id) = :columnIdsResponse
            order by fri.form_response_id
            """, nativeQuery = true)
    List<Tuple> getResponseIdsByGroupedResponse(Long questionId, Long rowIdResponse, Long[] columnIdsResponse, Pageable pageable);

    @Query(value = """
            select
            x.column_ids columnIds,
            count(x.column_ids) responseCount
            from (
              select
              array_agg(tbgr.column_id order by tbgr.column_id) column_ids
              from tick_box_grid_responses tbgr
              join question_response_form_response_individual qrfri
              on tbgr.question_response_id = qrfri.question_response_id
              and tbgr.question_id = :questionId
              and tbgr.row_id = :rowId
              group by qrfri.form_response_individual_id
              ) x
            group by x.column_ids
            order by responseCount desc, columnIds;
            """, nativeQuery = true)
    List<Tuple> groupedByRowColumn(Long questionId, Long rowId, Pageable pageable);
}
