package com.sougata.form_data_service.exception;

import com.sougata.form_engine.constant.ProblemDetailTitle;
import com.sougata.form_engine.exception.FormDetailsMayChangedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FormDetailsMayChangedException.class)
    public ProblemDetail handleFormDetailsMayChangedException(FormDetailsMayChangedException ex) {
        var pb = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        pb.setTitle(ProblemDetailTitle.FORM_DETAILS_MAY_CHANGED);

        return pb;
    }

    @ExceptionHandler(FormSubmitException.class)
    public ProblemDetail handleFormSubmitException(FormSubmitException ex) {
        var pb = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                ex.getMessage()
        );

        pb.setTitle(ProblemDetailTitle.FORM_CAN_NOT_BE_SUBMITTED);

        return pb;
    }

}
