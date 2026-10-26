package com.sougata.form_service.service.formSchema;

import com.sougata.form_engine.dto.form.*;
import com.sougata.form_engine.dto.others.SuccessMessageDto;
import com.sougata.form_engine.dto.template.TemplateDetails;
import com.sougata.form_service.model.formSchema.Form;

import java.util.UUID;

public interface FormService {
    FormInfoDto createForm(UUID userId);

    FormInfoDto copyForm(UUID formId, CopyFormReqDto req, UUID userId);

    FormInfoDto updateForm(UUID formId, FormUpdateReqDto dto, UUID userId);

    FormDetailsDto getForm(UUID formId, UUID userId);

    FormDetailsDto viewForm(UUID id, UUID userId);

    FormSummariesDto getFormsSummaries(UUID userId);

    Form getFormById(UUID id);

    FormInfoDto getFormInfo(UUID formId);

    SuccessMessageDto deleteForm(UUID formId, UUID userId);

    Form createFromTemplate(TemplateDetails template, UUID userId);

}
