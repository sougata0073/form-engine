package com.sougata.form_data_service.formValidation.responseValidator;

import com.sougata.form_data_service.constant.ValidationMessages;
import com.sougata.form_data_service.formValidation.exception.ResponseValidationException;
import com.sougata.form_engine.dto.question.responseputrequest.ParagraphResponsePutReqDto;
import com.sougata.form_engine.dto.validation.config.NoneValidationConfig;
import com.sougata.form_engine.dto.validation.config.ParagraphValidationConfig;

public class ParagraphValidator {

    public static class LengthMaxCharacterCount implements
            ResponseValidator<ParagraphResponsePutReqDto, ParagraphValidationConfig.MaxCharacterCount> {

        @Override
        public boolean isValid(ParagraphResponsePutReqDto validationRequestDto, ParagraphValidationConfig.MaxCharacterCount validationConfig) {
            if (validationRequestDto.getText().length() > validationConfig.getNumber()) {
                throw new ResponseValidationException(
                        String.format(
                                ValidationMessages.INVALID_MAX_CHARACTER_LENGTH, validationConfig.getNumber()
                        )
                );
            }

            return true;
        }

    }

    public static class LengthMinCharacterCount implements
            ResponseValidator<ParagraphResponsePutReqDto, ParagraphValidationConfig.MinCharacterCount> {

        @Override
        public boolean isValid(ParagraphResponsePutReqDto validationRequestDto, ParagraphValidationConfig.MinCharacterCount validationConfig) {
            if (validationRequestDto.getText().length() < validationConfig.getNumber()) {
                throw new ResponseValidationException(
                        String.format(
                                ValidationMessages.INVALID_MIN_CHARACTER_LENGTH, validationConfig.getNumber()
                        )
                );
            }

            return true;
        }

    }

    public static class RegexMatches implements
            ResponseValidator<ParagraphResponsePutReqDto, ParagraphValidationConfig.Matches> {

        @Override
        public boolean isValid(ParagraphResponsePutReqDto validationRequestDto, ParagraphValidationConfig.Matches validationConfig) {
            String pattern = validationConfig.getText();
            if (pattern == null) {
                pattern = "";
            }

            if (validationRequestDto.getText() == null || !validationRequestDto.getText().matches(pattern)) {
                throw new ResponseValidationException(ValidationMessages.INVALID_REGEX_MATCH);
            }

            return true;
        }

    }

    public static class DoesNotMatch implements
            ResponseValidator<ParagraphResponsePutReqDto, ParagraphValidationConfig.DoesNotMatch> {

        @Override
        public boolean isValid(ParagraphResponsePutReqDto validationRequestDto, ParagraphValidationConfig.DoesNotMatch validationConfig) {
            String pattern = validationConfig.getText();
            if (pattern == null) {
                pattern = "";
            }

            if (validationRequestDto.getText() != null && validationRequestDto.getText().matches(pattern)) {
                throw new ResponseValidationException(ValidationMessages.INVALID_REGEX_NOT_MATCH);
            }

            return true;
        }

    }

    public static class None implements ResponseValidator<ParagraphResponsePutReqDto, NoneValidationConfig> {

        @Override
        public boolean isValid(ParagraphResponsePutReqDto validationRequestDto, NoneValidationConfig validationConfig) {
            return true;
        }

    }

}
