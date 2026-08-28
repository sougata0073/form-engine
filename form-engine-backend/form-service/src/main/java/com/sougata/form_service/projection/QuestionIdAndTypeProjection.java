package com.sougata.form_service.projection;

import com.sougata.form_service.constant.QuestionType;

public interface QuestionIdAndTypeProjection {
    Long getId();

    QuestionType getQuestionType();
}
