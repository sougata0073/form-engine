package com.sougata.form_service.repository.formSchema;

import com.sougata.form_service.constant.QuestionType;
import com.sougata.form_service.model.formSchema.Paragraph;
import org.springframework.stereotype.Repository;

@Repository("PARAGRAPH_REPOSITORY")
public interface ParagraphRepository extends AnyTypeQuestionRepository<Paragraph, Long> {

    @Override
    default QuestionType getQuestionType() {
        return QuestionType.PARAGRAPH;
    }

}

