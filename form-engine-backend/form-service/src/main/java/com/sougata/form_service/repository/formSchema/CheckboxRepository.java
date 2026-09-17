package com.sougata.form_service.repository.formSchema;

import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_service.model.formSchema.Checkbox;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("CHECKBOX_REPOSITORY")
public interface CheckboxRepository extends AnyTypeQuestionRepository<Checkbox, Long> {

    @Query("""
        select
        count(op)
        from Checkbox cb
        join cb.options op
        where cb.questionId = :questionId
        """)
    Long getOptionCount(Long questionId);

    @Override
    default QuestionType getQuestionType() {
        return QuestionType.CHECKBOX;
    }

}
