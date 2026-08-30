package com.sougata.form_data_service.formValidation.responseValidator;

import com.sougata.form_data_service.constant.ValidationMessages;
import com.sougata.form_data_service.formValidation.exception.ResponseValidationException;
import com.sougata.form_engine.dto.validation.config.NoneValidationConfig;
import com.sougata.form_engine.dto.validation.config.ShortAnswerValidationConfig;
import com.sougata.form_engine.util.StringUtil;
import com.sougata.form_engine.dto.question.responseRequest.ShortAnswerResponsePutReqDto;

public class ShortAnswerValidator {

    private static Double parseNumber(String response) {
        try {
            return Double.parseDouble(response);
        } catch (Exception e) {
            return null;
        }
    }

    public static class LengthMaxCharacterCount implements
            ResponseValidator<ShortAnswerResponsePutReqDto, ShortAnswerValidationConfig.MaxCharacterCount> {

        @Override
        public boolean isValid(ShortAnswerResponsePutReqDto validationRequestDto, ShortAnswerValidationConfig.MaxCharacterCount validationConfig) {
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
            ResponseValidator<ShortAnswerResponsePutReqDto, ShortAnswerValidationConfig.MinCharacterCount> {

        @Override
        public boolean isValid(ShortAnswerResponsePutReqDto validationRequestDto, ShortAnswerValidationConfig.MinCharacterCount validationConfig) {
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

    public static class NumberIsNumber implements
            ResponseValidator<ShortAnswerResponsePutReqDto, ShortAnswerValidationConfig.IsNumber> {

        @Override
        public boolean isValid(ShortAnswerResponsePutReqDto validationRequestDto, ShortAnswerValidationConfig.IsNumber validationConfig) {
            if (parseNumber(validationRequestDto.getText()) == null) {
                throw new ResponseValidationException(ValidationMessages.INVALID_NUMBER);
            }
            return true;
        }

    }

    public static class NumberGreaterThan implements
            ResponseValidator<ShortAnswerResponsePutReqDto, ShortAnswerValidationConfig.GreaterThan> {

        @Override
        public boolean isValid(ShortAnswerResponsePutReqDto validationRequestDto, ShortAnswerValidationConfig.GreaterThan validationConfig) {
            Double parsed = parseNumber(validationRequestDto.getText());
            if (parsed == null || !(parsed > validationConfig.getNumber())) {
                throw new ResponseValidationException(
                        String.format(ValidationMessages.INVALID_GREATER_THAN, validationConfig.getNumber())
                );
            }
            return true;
        }

    }

    public static class NumberGreaterThanOrEqualTo implements
            ResponseValidator<ShortAnswerResponsePutReqDto, ShortAnswerValidationConfig.GreaterThanOrEqualTo> {

        @Override
        public boolean isValid(ShortAnswerResponsePutReqDto validationRequestDto, ShortAnswerValidationConfig.GreaterThanOrEqualTo validationConfig) {
            Double parsed = parseNumber(validationRequestDto.getText());
            if (parsed == null || !(parsed >= validationConfig.getNumber())) {
                throw new ResponseValidationException(
                        String.format(ValidationMessages.INVALID_GREATER_THAN_OR_EQUAL_TO, validationConfig.getNumber())
                );
            }
            return true;
        }

    }

    public static class NumberLessThan implements
            ResponseValidator<ShortAnswerResponsePutReqDto, ShortAnswerValidationConfig.LessThan> {

        @Override
        public boolean isValid(ShortAnswerResponsePutReqDto validationRequestDto, ShortAnswerValidationConfig.LessThan validationConfig) {
            Double parsed = parseNumber(validationRequestDto.getText());
            if (parsed == null || !(parsed < validationConfig.getNumber())) {
                throw new ResponseValidationException(
                        String.format(ValidationMessages.INVALID_LESS_THAN, validationConfig.getNumber())
                );
            }
            return true;
        }

    }

    public static class NumberLessThanOrEqualTo implements
            ResponseValidator<ShortAnswerResponsePutReqDto, ShortAnswerValidationConfig.LessThanOrEqualTo> {

        @Override
        public boolean isValid(ShortAnswerResponsePutReqDto validationRequestDto, ShortAnswerValidationConfig.LessThanOrEqualTo validationConfig) {
            Double parsed = parseNumber(validationRequestDto.getText());
            if (parsed == null || !(parsed <= validationConfig.getNumber())) {
                throw new ResponseValidationException(
                        String.format(ValidationMessages.INVALID_LESS_THAN_OR_EQUAL_TO, validationConfig.getNumber())
                );
            }
            return true;
        }

    }

    public static class NumberEqualTo implements
            ResponseValidator<ShortAnswerResponsePutReqDto, ShortAnswerValidationConfig.EqualTo> {

        @Override
        public boolean isValid(ShortAnswerResponsePutReqDto validationRequestDto, ShortAnswerValidationConfig.EqualTo validationConfig) {
            Double parsed = parseNumber(validationRequestDto.getText());
            if (parsed == null || !parsed.equals(validationConfig.getNumber())) {
                throw new ResponseValidationException(
                        String.format(ValidationMessages.INVALID_EQUAL_TO, validationConfig.getNumber())
                );
            }
            return true;
        }

    }

    public static class NumberNotEqualTo implements
            ResponseValidator<ShortAnswerResponsePutReqDto, ShortAnswerValidationConfig.NotEqualTo> {

        @Override
        public boolean isValid(ShortAnswerResponsePutReqDto validationRequestDto, ShortAnswerValidationConfig.NotEqualTo validationConfig) {
            Double parsed = parseNumber(validationRequestDto.getText());
            if (parsed == null || parsed.equals(validationConfig.getNumber())) {
                throw new ResponseValidationException(
                        String.format(ValidationMessages.INVALID_NOT_EQUAL_TO, validationConfig.getNumber())
                );
            }
            return true;
        }

    }

    public static class NumberBetween implements
            ResponseValidator<ShortAnswerResponsePutReqDto, ShortAnswerValidationConfig.Between> {

        @Override
        public boolean isValid(ShortAnswerResponsePutReqDto validationRequestDto, ShortAnswerValidationConfig.Between validationConfig) {
            Double parsed = parseNumber(validationRequestDto.getText());
            if (parsed == null || !(parsed >= validationConfig.getFromNumber() && parsed <= validationConfig.getToNumber())) {
                throw new ResponseValidationException(
                        String.format(ValidationMessages.INVALID_BETWEEN, validationConfig.getFromNumber(), validationConfig.getToNumber())
                );
            }
            return true;
        }

    }

    public static class NumberNotBetween implements
            ResponseValidator<ShortAnswerResponsePutReqDto, ShortAnswerValidationConfig.NotBetween> {

        @Override
        public boolean isValid(ShortAnswerResponsePutReqDto validationRequestDto, ShortAnswerValidationConfig.NotBetween validationConfig) {
            Double parsed = parseNumber(validationRequestDto.getText());
            if (parsed == null || (parsed >= validationConfig.getFromNumber() && parsed <= validationConfig.getToNumber())) {
                throw new ResponseValidationException(
                        String.format(ValidationMessages.INVALID_NOT_BETWEEN, validationConfig.getFromNumber(), validationConfig.getToNumber())
                );
            }
            return true;
        }

    }

    public static class NumberWholeNumber implements
            ResponseValidator<ShortAnswerResponsePutReqDto, ShortAnswerValidationConfig.WholeNumber> {

        @Override
        public boolean isValid(ShortAnswerResponsePutReqDto validationRequestDto, ShortAnswerValidationConfig.WholeNumber validationConfig) {
            try {
                int parsed = Integer.parseInt(validationRequestDto.getText());
                if (parsed < 0) {
                    throw new ResponseValidationException(ValidationMessages.INVALID_WHOLE_NUMBER);
                }
            } catch (NumberFormatException e) {
                throw new ResponseValidationException(ValidationMessages.INVALID_WHOLE_NUMBER);
            }
            return true;
        }

    }

    public static class RegexMatches implements
            ResponseValidator<ShortAnswerResponsePutReqDto, ShortAnswerValidationConfig.Matches> {

        @Override
        public boolean isValid(ShortAnswerResponsePutReqDto validationRequestDto, ShortAnswerValidationConfig.Matches validationConfig) {
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

    public static class RegexDoesNotMatch implements
            ResponseValidator<ShortAnswerResponsePutReqDto, ShortAnswerValidationConfig.DoesNotMatch> {

        @Override
        public boolean isValid(ShortAnswerResponsePutReqDto validationRequestDto, ShortAnswerValidationConfig.DoesNotMatch validationConfig) {
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

    public static class TextContains implements
            ResponseValidator<ShortAnswerResponsePutReqDto, ShortAnswerValidationConfig.Contains> {

        @Override
        public boolean isValid(ShortAnswerResponsePutReqDto validationRequestDto, ShortAnswerValidationConfig.Contains validationConfig) {
            if (validationRequestDto.getText() == null || validationConfig.getText() == null) {
                throw new ResponseValidationException(
                        String.format(ValidationMessages.INVALID_CONTAINS, validationConfig.getText())
                );
            }
            if (!validationRequestDto.getText().contains(validationConfig.getText())) {
                throw new ResponseValidationException(
                        String.format(ValidationMessages.INVALID_CONTAINS, validationConfig.getText())
                );
            }
            return true;
        }

    }

    public static class TextDoesNotContains implements
            ResponseValidator<ShortAnswerResponsePutReqDto, ShortAnswerValidationConfig.DoesNotContains> {

        @Override
        public boolean isValid(ShortAnswerResponsePutReqDto validationRequestDto, ShortAnswerValidationConfig.DoesNotContains validationConfig) {
            if (validationRequestDto.getText() == null || validationConfig.getText() == null) {
                return true;
            }
            if (validationRequestDto.getText().contains(validationConfig.getText())) {
                throw new ResponseValidationException(
                        String.format(ValidationMessages.INVALID_NOT_CONTAINS, validationConfig.getText())
                );
            }
            return true;
        }

    }

    public static class TextEmail implements
            ResponseValidator<ShortAnswerResponsePutReqDto, ShortAnswerValidationConfig.Email> {

        @Override
        public boolean isValid(ShortAnswerResponsePutReqDto validationRequestDto, ShortAnswerValidationConfig.Email validationConfig) {
            if (validationRequestDto.getText() == null || !StringUtil.isEmail(validationRequestDto.getText())) {
                throw new ResponseValidationException(ValidationMessages.INVALID_EMAIL);
            }
            return true;
        }

    }

    public static class TextUrl implements
            ResponseValidator<ShortAnswerResponsePutReqDto, ShortAnswerValidationConfig.Url> {

        @Override
        public boolean isValid(ShortAnswerResponsePutReqDto validationRequestDto, ShortAnswerValidationConfig.Url validationConfig) {
            if (validationRequestDto.getText() == null || !StringUtil.isUrl(validationRequestDto.getText())) {
                throw new ResponseValidationException(ValidationMessages.INVALID_URL);
            }
            return true;
        }

    }

    public static class None implements ResponseValidator<ShortAnswerResponsePutReqDto, NoneValidationConfig> {

        @Override
        public boolean isValid(ShortAnswerResponsePutReqDto validationRequestDto, NoneValidationConfig validationConfig) {
            return true;
        }

    }

}
