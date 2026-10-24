package com.sougata.form_service.service.formSchema;

import com.sougata.form_engine.dto.form.FormDetailsDto;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface FormServiceCached {

    FormDetailsDto getFormDetails(UUID formId);

    CompletableFuture<FormDetailsDto> loadFormDetailsFromDb(UUID formId);

}
