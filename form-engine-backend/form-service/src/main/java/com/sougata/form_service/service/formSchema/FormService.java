package com.sougata.form_service.service.formSchema;

import com.sougata.form_service.dto.common.SuccessMessageDto;
import com.sougata.form_service.dto.form.*;
import com.sougata.form_service.dto.template.TemplateDetails;
import com.sougata.form_service.model.formSchema.Form;

import java.util.UUID;

public interface FormService {
    FormInfoDto createForm(FormPutReqDto dto, UUID userId);

    FormInfoDto copyForm(UUID formId, CopyFormReqDto req, UUID userId);

    FormInfoDto updateForm(UUID formId, FormPutReqDto dto, UUID userId);

    FormDetailsDto getForm(UUID formId, UUID userId);

    FormDetailsDto viewForm(UUID id, UUID userId);

    FormSummariesDto getFormsSummaries(UUID userId);

    Form getFormById(UUID id);

    SuccessMessageDto renameForm(UUID formId, FormRenameReqDto dto, UUID userId);

    FormInfoDto getFormInfo(UUID formId);

    SuccessMessageDto deleteForm(UUID formId, UUID userId);

    Form createFromTemplate(TemplateDetails template, UUID userId);

}
