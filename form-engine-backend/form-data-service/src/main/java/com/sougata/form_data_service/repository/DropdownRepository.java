package com.sougata.form_data_service.repository;

import com.sougata.form_data_service.model.Dropdown;
import org.springframework.stereotype.Repository;

@Repository("DROPDOWN_RESPONSE_REPOSITORY")
public interface DropdownRepository extends AnyTypeQuestionResponseRepository<Dropdown, Long> {

}
