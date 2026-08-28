package com.sougata.form_data_service.formValidation.exception;


import com.sougata.form_data_service.constant.ExceptionMessages;
import com.sougata.form_data_service.constant.QuestionType;

public class QuestionSchemaNotFoundException extends RuntimeException {
    public QuestionSchemaNotFoundException(QuestionType questionType, Long questionId) {
        super(String.format(ExceptionMessages.QUESTION_NOT_FOUND, questionType.getDisplayName(), questionId));
    }

    public QuestionSchemaNotFoundException(Long questionId) {
        super("Question not found with ID: " + questionId);
    }

}
