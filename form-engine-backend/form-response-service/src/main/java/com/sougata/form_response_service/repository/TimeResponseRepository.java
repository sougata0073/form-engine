package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.TimeResponse;
import org.springframework.stereotype.Repository;

@Repository("TIME_RESPONSE_REPOSITORY")
public interface TimeResponseRepository extends AnyTypeQuestionResponseRepository<TimeResponse, Long> {
}
