package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.TickBoxGridResponse;
import org.springframework.stereotype.Repository;

@Repository("TICK_BOX_GRID_RESPONSE_REPOSITORY")
public interface TickBoxGridResponseRepository extends AnyTypeQuestionResponseRepository<TickBoxGridResponse, Long> {
}
