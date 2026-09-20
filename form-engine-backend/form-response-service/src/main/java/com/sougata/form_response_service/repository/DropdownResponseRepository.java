package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.DropdownResponse;
import org.springframework.stereotype.Repository;

@Repository("DROPDOWN_RESPONSE_REPOSITORY")
public interface DropdownResponseRepository extends AnyTypeQuestionResponseRepository<DropdownResponse, Long> {
}
