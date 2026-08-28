package com.sougata.form_data_service.repository;

import com.sougata.form_data_service.model.TickBoxGrid;
import org.springframework.stereotype.Repository;

@Repository("TICK_BOX_GRID_RESPONSE_REPOSITORY")
public interface TickBoxGridRepository extends AnyTypeQuestionResponseRepository<TickBoxGrid, Long> {

}
