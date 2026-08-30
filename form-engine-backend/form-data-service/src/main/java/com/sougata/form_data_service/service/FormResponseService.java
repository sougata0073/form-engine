package com.sougata.form_data_service.service;

import com.sougata.form_engine.dto.others.SuccessMessageDto;
import com.sougata.form_engine.dto.form.FormResponsePutResDto;
import com.sougata.form_engine.dto.form.FormResponsePutReqDto;

import java.util.UUID;

public interface FormResponseService {

    FormResponsePutResDto saveResponse(UUID formId, FormResponsePutReqDto req, UUID responderId);

    SuccessMessageDto deleteFormResponse(UUID formId, UUID responderId, Long formResponseId);
}
