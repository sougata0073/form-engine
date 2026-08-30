package com.sougata.form_service.repository.formSchema;

import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_service.model.formSchema.Duration;
import org.springframework.stereotype.Repository;

@Repository("DURATION_REPOSITORY")
public interface DurationRepository extends AnyTypeQuestionRepository<Duration, Long> {

    @Override
    default QuestionType getQuestionType() {
        return QuestionType.DURATION;
    }

}

