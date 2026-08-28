package com.sougata.form_data_service.repository;

import com.sougata.form_data_service.model.LinearScale;
import org.springframework.stereotype.Repository;

@Repository("LINEAR_SCALE_RESPONSE_REPOSITORY")
public interface LinearScaleRepository extends AnyTypeQuestionResponseRepository<LinearScale, Long> {

}
