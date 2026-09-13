package com.sougata.form_data_service.repository;

import com.sougata.form_data_service.model.QuestionResponse;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionResponseRepository extends CassandraRepository<QuestionResponse, QuestionResponse.PartitionKey> {

    @Query("delete from question_responses where question_id = :questionId")
    void deleteAllByQuestionId(long questionId);

    @Query("delete from question_responses where question_id = :questionId and form_response_id = :formResponseId")
    void deleteAllByQuestionIdAndFormResponseId(Long questionId, Long formResponseId);

}
