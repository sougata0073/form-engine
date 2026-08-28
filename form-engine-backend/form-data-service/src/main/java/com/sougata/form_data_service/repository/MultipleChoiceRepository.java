package com.sougata.form_data_service.repository;

import com.sougata.form_data_service.model.MultipleChoice;
import org.springframework.stereotype.Repository;

@Repository("MULTIPLE_CHOICE_RESPONSE_REPOSITORY")
public interface MultipleChoiceRepository extends AnyTypeQuestionResponseRepository<MultipleChoice, Long> {
}
