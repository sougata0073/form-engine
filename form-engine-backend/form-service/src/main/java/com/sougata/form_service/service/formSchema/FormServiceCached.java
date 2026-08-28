package com.sougata.form_service.service.formSchema;

import com.sougata.form_service.dto.form.FormDetailsDto;

import java.util.UUID;

public interface FormServiceCached {

    FormDetailsDto getFormDetails(UUID formId);

    FormDetailsDto loadFormDetailsFromDb(UUID formId);

}
