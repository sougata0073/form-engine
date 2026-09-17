package com.sougata.form_service.repository.formSchema;

import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_service.model.formSchema.MultipleChoiceGrid;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("MULTIPLE_CHOICE_GRID_REPOSITORY")
public interface MultipleChoiceGridRepository extends AnyTypeQuestionRepository<MultipleChoiceGrid, Long> {

    @Query("""
        select
        count(r)
        from MultipleChoiceGrid mcg
        join mcg.rows r
        where mcg.questionId = :questionId
        """)
    Long getRowCount(Long questionId);

    @Query("""
        select
        count(c)
        from MultipleChoiceGrid mcg
        join mcg.columns c
        where mcg.questionId = :questionId
        """)
    Long getColumnCount(Long questionId);

    @Override
    default QuestionType getQuestionType() {
        return QuestionType.MULTIPLE_CHOICE_GRID;
    }

}

