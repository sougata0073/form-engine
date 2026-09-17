package com.sougata.form_service.repository.formSchema;

import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_service.model.formSchema.Dropdown;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("DROPDOWN_REPOSITORY")
public interface DropdownRepository extends AnyTypeQuestionRepository<Dropdown, Long> {

    @Query("""
        select
        count(op)
        from Dropdown dd
        join dd.options op
        where dd.questionId = :questionId
        """)
    Long getOptionCount(Long questionId);

    @Override
    default QuestionType getQuestionType() {
        return QuestionType.DROPDOWN;
    }

}
