package com.sougata.form_data_service.repository;

import com.sougata.form_data_service.model.Duration;
import org.springframework.stereotype.Repository;

@Repository("DURATION_RESPONSE_REPOSITORY")
public interface DurationRepository extends AnyTypeQuestionResponseRepository<Duration, Long> {

}
