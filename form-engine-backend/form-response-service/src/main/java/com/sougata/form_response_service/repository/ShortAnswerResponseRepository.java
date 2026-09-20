package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.ShortAnswerResponse;
import org.springframework.stereotype.Repository;

@Repository("SHORT_ANSWER_RESPONSE_REPOSITORY")
public interface ShortAnswerResponseRepository extends AnyTypeQuestionResponseRepository<ShortAnswerResponse, Long> {
}
