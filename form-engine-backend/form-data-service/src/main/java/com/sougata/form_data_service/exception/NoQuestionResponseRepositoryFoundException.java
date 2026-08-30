package com.sougata.form_data_service.exception;

import com.sougata.form_engine.constant.QuestionType;

public class NoQuestionResponseRepositoryFoundException extends RuntimeException {
    public NoQuestionResponseRepositoryFoundException(QuestionType questionType) {
        super("No question response manager found for question type: " + questionType.name());
    }
}
