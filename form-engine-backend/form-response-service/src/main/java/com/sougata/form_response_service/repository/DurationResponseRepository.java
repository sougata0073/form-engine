package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.DurationResponse;
import org.springframework.stereotype.Repository;

@Repository("DURATION_RESPONSE_REPOSITORY")
public interface DurationResponseRepository extends AnyTypeQuestionResponseRepository<DurationResponse, Long> {
}
