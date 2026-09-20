package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.MultipleChoiceGridResponse;
import org.springframework.stereotype.Repository;

@Repository("MULTIPLE_CHOICE_GRID_RESPONSE_REPOSITORY")
public interface MultipleChoiceGridResponseRepository extends AnyTypeQuestionResponseRepository<MultipleChoiceGridResponse, Long> {
}
