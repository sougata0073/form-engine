package com.sougata.form_service.projection;

import com.sougata.form_engine.constant.QuestionType;

public interface QuestionSummaryProjection {

    Long getId();

    String getQuestion();

    Integer getOrderIndex();

    QuestionType getQuestionType();

}
