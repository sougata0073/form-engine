package com.sougata.form_data_service.repository;

import com.sougata.form_data_service.model.AnyTypeQuestionResponse;
import com.sougata.form_data_service.model.TickBoxGrid;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository("TICK_BOX_GRID_RESPONSE_REPOSITORY")
public interface TickBoxGridRepository extends AnyTypeQuestionResponseRepository<TickBoxGrid, AnyTypeQuestionResponse.PartitionKey> {

    @Query("delete from tick_box_grids where form_id = :formId and form_response_id = :formResponseId and question_id in :questionIds")
    void deleteAllByQuestionIds(UUID formId, UUID formResponseId, Set<Long> questionIds);

    @Query("delete from tick_box_grids where form_id = :formId")
    void deleteAllByFormId(UUID formId);

    @Query("select * from tick_box_grids where form_id = :formId and form_response_id = :formResponseId and question_id in :questionIds")
    List<TickBoxGrid> findAllByQuestionIds(UUID formId, UUID formResponseId, Set<Long> questionIds);
}
