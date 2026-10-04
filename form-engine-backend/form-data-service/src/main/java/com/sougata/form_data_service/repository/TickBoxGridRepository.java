package com.sougata.form_data_service.repository;

import com.sougata.form_data_service.model.AnyTypeQuestionResponse;
import com.sougata.form_data_service.model.TickBoxGrid;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository("TICK_BOX_GRID_RESPONSE_REPOSITORY")
public interface TickBoxGridRepository extends AnyTypeQuestionResponseRepository<TickBoxGrid, AnyTypeQuestionResponse.PartitionKey> {

    @Query("delete from tick_box_grids where question_id in :questionIds and form_response_id = :formResponseId")
    void deleteAllByQuestionIdsAndFormResponseId(List<Long> questionIds, UUID formResponseId);
}
