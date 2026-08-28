package com.sougata.form_data_service.formValidation.responseValidator;

import com.sougata.form_data_service.constant.ValidationId;
import com.sougata.form_data_service.dto.question.request.QuestionResponsePutReqDto;
import com.sougata.form_data_service.dto.validationConfig.ValidationConfig;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ResponseValidatorFactory {

    private final ApplicationContext applicationContext;

    public ResponseValidatorFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @SuppressWarnings("unchecked")
    public <V extends QuestionResponsePutReqDto, C extends ValidationConfig> ResponseValidator<V, C> getValidator(
            ValidationId validationId
    ) {
        String validatorBeanName = String.format("%s_RESPONSE_VALIDATOR", validationId.name());

        try {
            return applicationContext.getBean(validatorBeanName, ResponseValidator.class);
        } catch (BeansException e) {
            throw new IllegalArgumentException("No response validator found for validation ID: " + validationId);
        }
    }

}
