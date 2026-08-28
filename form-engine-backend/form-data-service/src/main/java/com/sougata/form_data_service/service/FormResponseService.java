package com.sougata.form_data_service.service;

import com.sougata.form_data_service.dto.common.SuccessMessageDto;
import com.sougata.form_data_service.dto.form.FormResponsePutReqDto;
import com.sougata.form_data_service.dto.form.FormResponsePutResDto;

import java.util.UUID;

public interface FormResponseService {

    FormResponsePutResDto saveResponse(UUID formId, FormResponsePutReqDto req, UUID userId);

    SuccessMessageDto deleteFormResponse(UUID formId, UUID userId, Long formResponseId);
}
