package com.sougata.form_data_service.formValidation.responseValidator;

import com.sougata.form_data_service.constant.ValidationMessages;
import com.sougata.form_data_service.dto.question.request.ShortAnswerResponsePutReqDto;
import com.sougata.form_data_service.dto.validationConfig.NoneValidationConfig;
import com.sougata.form_data_service.dto.validationConfig.ShortAnswerValidationConfig;
import com.sougata.form_data_service.formValidation.exception.ResponseValidationException;
import com.sougata.form_data_service.util.StringUtil;

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

        @Override
        public Class<ShortAnswerResponsePutReqDto> getValidationRequestClass() {
            return ShortAnswerResponsePutReqDto.class;
        }

        @Override
        public Class<ShortAnswerValidationConfig.MaxCharacterCount> getValidationConfigClass() {
            return ShortAnswerValidationConfig.MaxCharacterCount.class;
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

        @Override
        public Class<ShortAnswerResponsePutReqDto> getValidationRequestClass() {
            return ShortAnswerResponsePutReqDto.class;
        }

        @Override
        public Class<ShortAnswerValidationConfig.MinCharacterCount> getValidationConfigClass() {
            return ShortAnswerValidationConfig.MinCharacterCount.class;
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

        @Override
        public Class<ShortAnswerResponsePutReqDto> getValidationRequestClass() {
            return ShortAnswerResponsePutReqDto.class;
        }

        @Override
        public Class<ShortAnswerValidationConfig.IsNumber> getValidationConfigClass() {
            return ShortAnswerValidationConfig.IsNumber.class;
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

        @Override
        public Class<ShortAnswerResponsePutReqDto> getValidationRequestClass() {
            return ShortAnswerResponsePutReqDto.class;
        }

        @Override
        public Class<ShortAnswerValidationConfig.GreaterThan> getValidationConfigClass() {
            return ShortAnswerValidationConfig.GreaterThan.class;
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

        @Override
        public Class<ShortAnswerResponsePutReqDto> getValidationRequestClass() {
            return ShortAnswerResponsePutReqDto.class;
        }

        @Override
        public Class<ShortAnswerValidationConfig.GreaterThanOrEqualTo> getValidationConfigClass() {
            return ShortAnswerValidationConfig.GreaterThanOrEqualTo.class;
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

        @Override
        public Class<ShortAnswerResponsePutReqDto> getValidationRequestClass() {
            return ShortAnswerResponsePutReqDto.class;
        }

        @Override
        public Class<ShortAnswerValidationConfig.LessThan> getValidationConfigClass() {
            return ShortAnswerValidationConfig.LessThan.class;
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

        @Override
        public Class<ShortAnswerResponsePutReqDto> getValidationRequestClass() {
            return ShortAnswerResponsePutReqDto.class;
        }

        @Override
        public Class<ShortAnswerValidationConfig.LessThanOrEqualTo> getValidationConfigClass() {
            return ShortAnswerValidationConfig.LessThanOrEqualTo.class;
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

        @Override
        public Class<ShortAnswerResponsePutReqDto> getValidationRequestClass() {
            return ShortAnswerResponsePutReqDto.class;
        }

        @Override
        public Class<ShortAnswerValidationConfig.EqualTo> getValidationConfigClass() {
            return ShortAnswerValidationConfig.EqualTo.class;
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

        @Override
        public Class<ShortAnswerResponsePutReqDto> getValidationRequestClass() {
            return ShortAnswerResponsePutReqDto.class;
        }

        @Override
        public Class<ShortAnswerValidationConfig.NotEqualTo> getValidationConfigClass() {
            return ShortAnswerValidationConfig.NotEqualTo.class;
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

        @Override
        public Class<ShortAnswerResponsePutReqDto> getValidationRequestClass() {
            return ShortAnswerResponsePutReqDto.class;
        }

        @Override
        public Class<ShortAnswerValidationConfig.Between> getValidationConfigClass() {
            return ShortAnswerValidationConfig.Between.class;
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

        @Override
        public Class<ShortAnswerResponsePutReqDto> getValidationRequestClass() {
            return ShortAnswerResponsePutReqDto.class;
        }

        @Override
        public Class<ShortAnswerValidationConfig.NotBetween> getValidationConfigClass() {
            return ShortAnswerValidationConfig.NotBetween.class;
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

        @Override
        public Class<ShortAnswerResponsePutReqDto> getValidationRequestClass() {
            return ShortAnswerResponsePutReqDto.class;
        }

        @Override
        public Class<ShortAnswerValidationConfig.WholeNumber> getValidationConfigClass() {
            return ShortAnswerValidationConfig.WholeNumber.class;
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

        @Override
        public Class<ShortAnswerResponsePutReqDto> getValidationRequestClass() {
            return ShortAnswerResponsePutReqDto.class;
        }

        @Override
        public Class<ShortAnswerValidationConfig.Matches> getValidationConfigClass() {
            return ShortAnswerValidationConfig.Matches.class;
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

        @Override
        public Class<ShortAnswerResponsePutReqDto> getValidationRequestClass() {
            return ShortAnswerResponsePutReqDto.class;
        }

        @Override
        public Class<ShortAnswerValidationConfig.DoesNotMatch> getValidationConfigClass() {
            return ShortAnswerValidationConfig.DoesNotMatch.class;
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

        @Override
        public Class<ShortAnswerResponsePutReqDto> getValidationRequestClass() {
            return ShortAnswerResponsePutReqDto.class;
        }

        @Override
        public Class<ShortAnswerValidationConfig.Contains> getValidationConfigClass() {
            return ShortAnswerValidationConfig.Contains.class;
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

        @Override
        public Class<ShortAnswerResponsePutReqDto> getValidationRequestClass() {
            return ShortAnswerResponsePutReqDto.class;
        }

        @Override
        public Class<ShortAnswerValidationConfig.DoesNotContains> getValidationConfigClass() {
            return ShortAnswerValidationConfig.DoesNotContains.class;
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

        @Override
        public Class<ShortAnswerResponsePutReqDto> getValidationRequestClass() {
            return ShortAnswerResponsePutReqDto.class;
        }

        @Override
        public Class<ShortAnswerValidationConfig.Email> getValidationConfigClass() {
            return ShortAnswerValidationConfig.Email.class;
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

        @Override
        public Class<ShortAnswerResponsePutReqDto> getValidationRequestClass() {
            return ShortAnswerResponsePutReqDto.class;
        }

        @Override
        public Class<ShortAnswerValidationConfig.Url> getValidationConfigClass() {
            return ShortAnswerValidationConfig.Url.class;
        }
    }

    public static class None implements ResponseValidator<ShortAnswerResponsePutReqDto, NoneValidationConfig> {

        @Override
        public boolean isValid(ShortAnswerResponsePutReqDto validationRequestDto, NoneValidationConfig validationConfig) {
            return true;
        }

        @Override
        public Class<ShortAnswerResponsePutReqDto> getValidationRequestClass() {
            return ShortAnswerResponsePutReqDto.class;
        }

        @Override
        public Class<NoneValidationConfig> getValidationConfigClass() {
            return NoneValidationConfig.class;
        }
    }

}
