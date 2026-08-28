package com.sougata.form_data_service.formValidation.responseValidator;

import com.sougata.form_data_service.constant.ExceptionMessages;
import com.sougata.form_data_service.dto.question.request.CheckboxResponsePutReqDto;
import com.sougata.form_data_service.dto.validationConfig.CheckboxValidationConfig;
import com.sougata.form_data_service.dto.validationConfig.NoneValidationConfig;
import com.sougata.form_data_service.formValidation.exception.ResponseValidationException;

public class CheckboxValidator {

    public static class SelectAtLeast implements
            ResponseValidator<CheckboxResponsePutReqDto, CheckboxValidationConfig.SelectAtLeast> {

        @Override
        public boolean isValid(CheckboxResponsePutReqDto validationRequestDto, CheckboxValidationConfig.SelectAtLeast validationConfig) {
            if (validationRequestDto.getResponseOptionIds().size() < validationConfig.getNumber()) {
                throw new ResponseValidationException(
                        String.format(
                                ExceptionMessages.INVALID_SELECT_AT_LEAST,
                                validationConfig.getNumber(),
                                validationRequestDto.getResponseOptionIds().size()
                        )
                );
            }

            return true;
        }

        @Override
        public Class<CheckboxResponsePutReqDto> getValidationRequestClass() {
            return CheckboxResponsePutReqDto.class;
        }

        @Override
        public Class<CheckboxValidationConfig.SelectAtLeast> getValidationConfigClass() {
            return CheckboxValidationConfig.SelectAtLeast.class;
        }
    }

    public static class SelectAtMost implements
            ResponseValidator<CheckboxResponsePutReqDto, CheckboxValidationConfig.SelectAtMost> {

        @Override
        public boolean isValid(CheckboxResponsePutReqDto validationRequestDto, CheckboxValidationConfig.SelectAtMost validationConfig) {
            if (validationRequestDto.getResponseOptionIds().size() > validationConfig.getNumber()) {
                throw new ResponseValidationException(
                        String.format(
                                ExceptionMessages.INVALID_SELECT_AT_MOST,
                                validationConfig.getNumber(),
                                validationRequestDto.getResponseOptionIds().size()
                        )
                );
            }

            return true;
        }

        @Override
        public Class<CheckboxResponsePutReqDto> getValidationRequestClass() {
            return CheckboxResponsePutReqDto.class;
        }

        @Override
        public Class<CheckboxValidationConfig.SelectAtMost> getValidationConfigClass() {
            return CheckboxValidationConfig.SelectAtMost.class;
        }
    }

    public static class SelectExactly implements
            ResponseValidator<CheckboxResponsePutReqDto, CheckboxValidationConfig.SelectExactly> {

        @Override
        public boolean isValid(CheckboxResponsePutReqDto validationRequestDto, CheckboxValidationConfig.SelectExactly validationConfig) {
            if (validationRequestDto.getResponseOptionIds().size() != validationConfig.getNumber()) {
                throw new ResponseValidationException(
                        String.format(
                                ExceptionMessages.INVALID_SELECT_EXACTLY,
                                validationConfig.getNumber(),
                                validationRequestDto.getResponseOptionIds().size()
                        )
                );
            }

            return true;
        }

        @Override
        public Class<CheckboxResponsePutReqDto> getValidationRequestClass() {
            return CheckboxResponsePutReqDto.class;
        }

        @Override
        public Class<CheckboxValidationConfig.SelectExactly> getValidationConfigClass() {
            return CheckboxValidationConfig.SelectExactly.class;
        }
    }

    public static class None implements ResponseValidator<CheckboxResponsePutReqDto, NoneValidationConfig> {

        @Override
        public boolean isValid(CheckboxResponsePutReqDto validationRequestDto, NoneValidationConfig validationConfig) {
            return true;
        }

        @Override
        public Class<CheckboxResponsePutReqDto> getValidationRequestClass() {
            return CheckboxResponsePutReqDto.class;
        }

        @Override
        public Class<NoneValidationConfig> getValidationConfigClass() {
            return NoneValidationConfig.class;
        }
    }

}
