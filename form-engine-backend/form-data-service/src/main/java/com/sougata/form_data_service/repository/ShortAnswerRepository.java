package com.sougata.form_data_service.repository;

import com.sougata.form_data_service.model.AnyTypeQuestionResponse;
import com.sougata.form_data_service.model.ShortAnswer;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("SHORT_ANSWER_RESPONSE_REPOSITORY")
public interface ShortAnswerRepository extends AnyTypeQuestionResponseRepository<ShortAnswer, AnyTypeQuestionResponse.PartitionKey> {

    @Query("delete from short_answers where question_id = :questionId")
    void deleteAllByQuestionId(Long questionId);

    @Query("""
            delete
            from short_answers
            where question_id = :questionId
            and question_response_id = :questionResponseId
            """)
    void deleteAllByQuestionIdAndQuestionResponseId(Long questionId, Long questionResponseId);
}
