package com.sougata.form_service.exception;

import com.sougata.form_service.constant.QuestionType;

public class NoQuestionTemplateManagerFoundException extends RuntimeException {
  public NoQuestionTemplateManagerFoundException(QuestionType questionType) {
    super("No question template manager found for question type: " + questionType.name());
  }
}
