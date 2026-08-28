package com.sougata.form_data_service.repository;

import com.sougata.form_data_service.model.MultipleChoiceGrid;
import org.springframework.stereotype.Repository;

@Repository("MULTIPLE_CHOICE_GRID_RESPONSE_REPOSITORY")
public interface MultipleChoiceGridRepository extends AnyTypeQuestionResponseRepository<MultipleChoiceGrid, Long> {

}
