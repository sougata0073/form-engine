package com.sougata.form_service.repository.template;

import com.sougata.form_engine.dto.template.questionTemplate.QuestionTemplateSummary;
import com.sougata.form_service.model.template.QuestionTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionTemplateRepository extends JpaRepository<QuestionTemplate, Long> {

    @Query("""
            select new com.sougata.form_engine.dto.template.questionTemplate.QuestionTemplateSummary(
                        qt.id, qt.questionType
            )
            from QuestionTemplate qt
            where qt.template.id = :templateId
            """)
    List<QuestionTemplateSummary> findQuestionTemplateSummariesByTemplateId(Long templateId);
}
