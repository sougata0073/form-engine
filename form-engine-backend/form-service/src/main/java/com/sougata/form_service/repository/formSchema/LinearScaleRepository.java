package com.sougata.form_service.repository.formSchema;

import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_service.model.formSchema.LinearScale;
import org.springframework.stereotype.Repository;

@Repository("LINEAR_SCALE_REPOSITORY")
public interface LinearScaleRepository extends AnyTypeQuestionRepository<LinearScale, Long> {

    @Override
    default QuestionType getQuestionType() {
        return QuestionType.LINEAR_SCALE;
    }

}

