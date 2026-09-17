package com.sougata.form_service.repository.formSchema;

import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_service.model.formSchema.TickBoxGrid;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("TICK_BOX_GRID_REPOSITORY")
public interface TickBoxGridRepository extends AnyTypeQuestionRepository<TickBoxGrid, Long> {

    @Query("""
        select
        count(r)
        from TickBoxGrid tbg
        join tbg.rows r
        where tbg.questionId = :questionId
        """)
    Long getRowCount(Long questionId);

    @Query("""
        select
        count(c)
        from TickBoxGrid tbg
        join tbg.columns c
        where tbg.questionId = :questionId
        """)
    Long getColumnCount(Long questionId);

    @Override
    default QuestionType getQuestionType() {
        return QuestionType.TICK_BOX_GRID;
    }

}

