package com.sougata.form_service.exception;

import com.sougata.form_engine.constant.QuestionType;

public class NoQuestionTemplateRepositoryFoundException extends RuntimeException {
    public NoQuestionTemplateRepositoryFoundException(QuestionType questionType) {
        super("No question template repository found for question type: " + questionType.name());
    }
}
