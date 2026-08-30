package com.sougata.form_service.repository.formSchema;

import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_service.model.formSchema.Date;
import org.springframework.stereotype.Repository;

@Repository("DATE_REPOSITORY")
public interface DateRepository extends AnyTypeQuestionRepository<Date, Long> {

    @Override
    default QuestionType getQuestionType() {
        return QuestionType.DATE;
    }

}

