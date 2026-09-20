package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.MultipleChoiceResponse;
import org.springframework.stereotype.Repository;

@Repository("MULTIPLE_CHOICE_RESPONSE_REPOSITORY")
public interface MultipleChoiceResponseRepository extends AnyTypeQuestionResponseRepository<MultipleChoiceResponse, Long> {

}
