package com.sougata.form_service.exception;

import com.sougata.form_service.constant.ExceptionMessages;
import com.sougata.form_service.constant.QuestionType;

public class QuestionNotFoundException extends RuntimeException {
    public QuestionNotFoundException(QuestionType questionType, Long questionId) {
        super(String.format(ExceptionMessages.QUESTION_NOT_FOUND, questionType.getDisplayName(), questionId));
    }

    public QuestionNotFoundException(Long questionId) {
        super("Question not found with ID: " + questionId);
    }

}
