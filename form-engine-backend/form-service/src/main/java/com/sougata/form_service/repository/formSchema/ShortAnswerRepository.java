package com.sougata.form_service.repository.formSchema;

import com.sougata.form_service.constant.QuestionType;
import com.sougata.form_service.model.formSchema.ShortAnswer;
import org.springframework.stereotype.Repository;

@Repository("SHORT_ANSWER_REPOSITORY")
public interface ShortAnswerRepository extends AnyTypeQuestionRepository<ShortAnswer, Long> {

    @Override
    default QuestionType getQuestionType() {
        return QuestionType.SHORT_ANSWER;
    }

}

