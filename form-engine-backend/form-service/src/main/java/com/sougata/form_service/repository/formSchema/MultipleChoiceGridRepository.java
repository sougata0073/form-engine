package com.sougata.form_service.repository.formSchema;

import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_service.model.formSchema.MultipleChoiceGrid;
import org.springframework.stereotype.Repository;

@Repository("MULTIPLE_CHOICE_GRID_REPOSITORY")
public interface MultipleChoiceGridRepository extends AnyTypeQuestionRepository<MultipleChoiceGrid, Long> {

    @Override
    default QuestionType getQuestionType() {
        return QuestionType.MULTIPLE_CHOICE_GRID;
    }

}

