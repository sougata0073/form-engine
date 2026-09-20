package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.ParagraphResponse;
import org.springframework.stereotype.Repository;

@Repository("PARAGRAPH_RESPONSE_REPOSITORY")
public interface ParagraphResponseRepository extends AnyTypeQuestionResponseRepository<ParagraphResponse, Long> {
}
