package com.sougata.form_service.service.formSchema;

import com.sougata.form_engine.dto.form.*;

import java.util.UUID;

public interface FormServiceCached {

    FormDetailsDto getFormDetails(UUID formId);

    FormDetailsDto loadFormDetailsFromDb(UUID formId);

}
