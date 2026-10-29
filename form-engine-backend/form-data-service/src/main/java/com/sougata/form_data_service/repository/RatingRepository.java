package com.sougata.form_data_service.repository;

import com.sougata.form_data_service.model.AnyTypeQuestionResponse;
import com.sougata.form_data_service.model.Rating;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository("RATING_RESPONSE_REPOSITORY")
public interface RatingRepository extends AnyTypeQuestionResponseRepository<Rating, AnyTypeQuestionResponse.PartitionKey> {

    @Query("delete from ratings where form_id = :formId and form_response_id = :formResponseId and question_id in :questionIds")
    void deleteAllByQuestionIds(UUID formId, UUID formResponseId, Set<Long> questionIds);

    @Query("delete from ratings where form_id = :formId")
    void deleteAllByFormId(UUID formId);

    @Query("select * from ratings where form_id = :formId and form_response_id = :formResponseId and question_id in :questionIds")
    List<Rating> findAllByQuestionIds(UUID formId, UUID formResponseId, Set<Long> questionIds);
}
