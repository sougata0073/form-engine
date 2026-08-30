package com.sougata.form_service.exception;

import com.sougata.form_engine.constant.QuestionType;

public class NoQuestionRepositoryFoundException extends RuntimeException {
    public NoQuestionRepositoryFoundException(QuestionType questionType) {
        super("No question manager found for question type: " + questionType.name());
    }
}
