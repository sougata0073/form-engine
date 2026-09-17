package com.sougata.form_service.repository.formSchema;

import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_service.model.formSchema.MultipleChoice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("MULTIPLE_CHOICE_REPOSITORY")
public interface MultipleChoiceRepository extends AnyTypeQuestionRepository<MultipleChoice, Long> {

    @Query("""
        select
        count(op)
        from MultipleChoice mc
        join mc.options op
        where mc.questionId = :questionId
        """)
    Long getOptionCount(Long questionId);

    @Override
    default QuestionType getQuestionType() {
        return QuestionType.MULTIPLE_CHOICE;
    }

}

