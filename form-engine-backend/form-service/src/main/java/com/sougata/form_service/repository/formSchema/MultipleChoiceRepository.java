package com.sougata.form_service.repository.formSchema;

import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_service.model.formSchema.MultipleChoice;
import org.springframework.stereotype.Repository;

@Repository("MULTIPLE_CHOICE_REPOSITORY")
public interface MultipleChoiceRepository extends AnyTypeQuestionRepository<MultipleChoice, Long> {

    @Override
    default QuestionType getQuestionType() {
        return QuestionType.MULTIPLE_CHOICE;
    }

}

