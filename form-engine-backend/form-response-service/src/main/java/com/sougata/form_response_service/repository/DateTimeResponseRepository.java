package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.DateTimeResponse;
import org.springframework.stereotype.Repository;

@Repository("DATE_TIME_RESPONSE_REPOSITORY")
public interface DateTimeResponseRepository extends AnyTypeQuestionResponseRepository<DateTimeResponse, Long> {
}
