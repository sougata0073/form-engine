package com.sougata.form_data_service.repository;

import com.sougata.form_data_service.model.AnyTypeQuestionResponse;
import com.sougata.form_data_service.model.LinearScale;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("LINEAR_SCALE_RESPONSE_REPOSITORY")
public interface LinearScaleRepository extends AnyTypeQuestionResponseRepository<LinearScale, AnyTypeQuestionResponse.PartitionKey> {

    @Query("delete from linear_scales where question_id = :questionId")
    void deleteAllByQuestionId(Long questionId);

    @Query("""
            delete
            from linear_scales
            where question_id = :questionId
            and question_response_id = :questionResponseId
            """)
    void deleteAllByQuestionIdAndQuestionResponseId(Long questionId, Long questionResponseId);
}
