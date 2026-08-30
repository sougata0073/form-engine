package com.sougata.form_service.repository.formSchema;

import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_service.model.formSchema.Time;
import org.springframework.stereotype.Repository;

@Repository("TIME_REPOSITORY")
public interface TimeRepository extends AnyTypeQuestionRepository<Time, Long> {

    @Override
    default QuestionType getQuestionType() {
        return QuestionType.TIME;
    }


}