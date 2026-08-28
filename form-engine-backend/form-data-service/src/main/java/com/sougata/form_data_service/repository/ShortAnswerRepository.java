package com.sougata.form_data_service.repository;

import com.sougata.form_data_service.model.ShortAnswer;
import org.springframework.stereotype.Repository;

@Repository("SHORT_ANSWER_RESPONSE_REPOSITORY")
public interface ShortAnswerRepository extends AnyTypeQuestionResponseRepository<ShortAnswer, Long> {

}
