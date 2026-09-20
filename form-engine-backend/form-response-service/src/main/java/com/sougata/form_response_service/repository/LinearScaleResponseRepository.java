package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.LinearScaleResponse;
import org.springframework.stereotype.Repository;

@Repository("LINEAR_SCALE_RESPONSE_REPOSITORY")
public interface LinearScaleResponseRepository extends AnyTypeQuestionResponseRepository<LinearScaleResponse, Long> {
}
