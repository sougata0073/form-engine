package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.DateResponse;
import org.springframework.stereotype.Repository;

@Repository("DATE_RESPONSE_REPOSITORY")
public interface DateResponseRepository extends AnyTypeQuestionResponseRepository<DateResponse, Long> {
}
