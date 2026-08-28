package com.sougata.form_engine.exception;

import com.sougata.form_engine.constant.QuestionType;

public class NoResponseManagerFoundException extends RuntimeException {
    public NoResponseManagerFoundException(QuestionType questionType) {
        super("No question manager found for question type: " + questionType);
    }
}
