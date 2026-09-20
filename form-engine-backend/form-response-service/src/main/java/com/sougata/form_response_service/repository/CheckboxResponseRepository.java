package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.CheckboxResponse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository("CHECKBOX_RESPONSE_REPOSITORY")
public interface CheckboxResponseRepository extends AnyTypeQuestionResponseRepository<CheckboxResponse, Long> {

    @Query("select cr.questionResponseId from CheckboxResponse cr where cr.optionId in :optionIds")
    List<Long> findQuestionResponseIdsByOptionIds(Set<Long> optionIds);

}
