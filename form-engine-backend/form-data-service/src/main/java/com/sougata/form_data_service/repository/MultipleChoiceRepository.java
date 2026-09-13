package com.sougata.form_data_service.repository;

import com.sougata.form_data_service.model.AnyTypeQuestionResponse;
import com.sougata.form_data_service.model.MultipleChoice;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("MULTIPLE_CHOICE_RESPONSE_REPOSITORY")
public interface MultipleChoiceRepository extends AnyTypeQuestionResponseRepository<MultipleChoice, AnyTypeQuestionResponse.PartitionKey> {

    @Query("delete from multiple_choices where question_id = :questionId")
    void deleteAllByQuestionId(Long questionId);

    @Query("""
            delete
            from multiple_choices
            where question_id = :questionId
            and question_response_id = :questionResponseId
            """)
    void deleteAllByQuestionIdAndQuestionResponseId(Long questionId, Long questionResponseId);
}
