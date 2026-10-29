package com.sougata.form_data_service.service.responseManager;

import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.formResponse.individual.QuestionResponseIndividualDto;
import com.sougata.form_engine.dto.question.responseputrequest.QuestionResponsePutReqDto;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public abstract class ResponseManager<QR extends QuestionResponsePutReqDto, QRID extends QuestionResponseIndividualDto> {

    public abstract CompletableFuture<Void> create(UUID formId, UUID formResponseId, QR response);

    public abstract CompletableFuture<List<QRID>> getResponses(UUID formId, UUID formResponseId, Set<Long> questionIds);

    public abstract CompletableFuture<Void> deleteAllByQuestionIds(UUID formId, UUID formResponseId, Set<Long> questionIds);

    public abstract CompletableFuture<Void> deleteAllByFormId(UUID formId);

    public abstract QuestionType getQuestionType();

}
