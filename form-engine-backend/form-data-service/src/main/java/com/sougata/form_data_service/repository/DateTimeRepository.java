package com.sougata.form_data_service.repository;

import com.sougata.form_data_service.model.AnyTypeQuestionResponse;
import com.sougata.form_data_service.model.DateTime;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository("DATE_TIME_RESPONSE_REPOSITORY")
public interface DateTimeRepository extends AnyTypeQuestionResponseRepository<DateTime, AnyTypeQuestionResponse.PartitionKey> {

    @Query("delete from date_times where question_id in :questionIds and form_response_id = :formResponseId")
    void deleteAllByQuestionIdsAndFormResponseId(List<Long> questionIds, UUID formResponseId);
}
