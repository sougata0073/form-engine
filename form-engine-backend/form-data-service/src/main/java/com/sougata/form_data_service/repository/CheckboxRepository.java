package com.sougata.form_data_service.repository;

import com.sougata.form_data_service.model.Checkbox;
import org.springframework.stereotype.Repository;

@Repository("CHECKBOX_RESPONSE_REPOSITORY")
public interface CheckboxRepository extends AnyTypeQuestionResponseRepository<Checkbox, Long> {

}
