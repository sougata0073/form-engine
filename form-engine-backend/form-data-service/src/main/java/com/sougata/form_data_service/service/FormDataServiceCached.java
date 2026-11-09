package com.sougata.form_data_service.service;

import com.sougata.form_engine.dto.formResponse.individual.FormResponseIndividualDtoNoPage;
import com.sougata.form_engine.dto.others.IsResponseSubmittedDto;

import java.util.UUID;

public interface FormDataServiceCached {

    FormResponseIndividualDtoNoPage getFormResponse(UUID formId, UUID formResponseId);

    IsResponseSubmittedDto getIsResponseAlreadySubmitted(UUID formId, String responderId);
}
