package com.sougata.form_data_service.repository;

import com.sougata.form_data_service.model.AnyTypeQuestionResponse;
import com.sougata.form_data_service.model.Dropdown;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("DROPDOWN_RESPONSE_REPOSITORY")
public interface DropdownRepository extends AnyTypeQuestionResponseRepository<Dropdown, AnyTypeQuestionResponse.PartitionKey> {

    @Query("delete from dropdowns where question_id = :questionId")
    void deleteAllByQuestionId(Long questionId);

    @Query("""
            delete
            from dropdowns
            where question_id = :questionId
            and question_response_id = :questionResponseId
            """)
    void deleteAllByQuestionIdAndQuestionResponseId(Long questionId, Long questionResponseId);
}
