package com.sougata.form_data_service.repository;

import com.sougata.form_data_service.model.Paragraph;
import org.springframework.stereotype.Repository;

@Repository("PARAGRAPH_RESPONSE_REPOSITORY")
public interface ParagraphRepository extends AnyTypeQuestionResponseRepository<Paragraph, Long> {
}
