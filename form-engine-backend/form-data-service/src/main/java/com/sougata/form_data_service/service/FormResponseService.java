package com.sougata.form_data_service.service;

import com.sougata.form_engine.dto.form.FormResponsePutReqDto;
import com.sougata.form_engine.dto.form.FormResponsePutResDto;
import com.sougata.form_engine.dto.formResponse.individual.FormResponseIndividualDtoNoPage;
import com.sougata.form_engine.dto.others.SuccessMessageDto;

import java.util.UUID;

public interface FormResponseService {

    FormResponsePutResDto saveResponse(UUID formId, FormResponsePutReqDto req, UUID responderId);

    FormResponseIndividualDtoNoPage getFormResponse(UUID formId, UUID formResponseId);

    SuccessMessageDto deleteFormResponse(UUID formId, UUID formResponseId);

}
