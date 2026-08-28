package com.sougata.form_service.exception;

import com.sougata.form_service.constant.ExceptionMessages;
import com.sougata.form_service.constant.QuestionType;

public class NoQuestionManagerFoundException extends RuntimeException {
    public NoQuestionManagerFoundException(QuestionType questionType) {
        super(String.format(ExceptionMessages.QUESTION_MANAGER_NOT_FOUND, questionType.name()));
    }
}
