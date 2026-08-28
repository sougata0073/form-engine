package com.sougata.form_service.repository.formSchema;

import com.sougata.form_service.constant.QuestionType;
import com.sougata.form_service.model.formSchema.Checkbox;
import org.springframework.stereotype.Repository;

@Repository("CHECKBOX_REPOSITORY")
public interface CheckboxRepository extends AnyTypeQuestionRepository<Checkbox, Long> {

    @Override
    default QuestionType getQuestionType() {
        return QuestionType.CHECKBOX;
    }

}
