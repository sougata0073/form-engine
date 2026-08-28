package com.sougata.form_data_service.repository;

import com.sougata.form_data_service.model.Time;
import org.springframework.stereotype.Repository;

@Repository("TIME_RESPONSE_REPOSITORY")
public interface TimeRepository extends AnyTypeQuestionResponseRepository<Time, Long> {
}
