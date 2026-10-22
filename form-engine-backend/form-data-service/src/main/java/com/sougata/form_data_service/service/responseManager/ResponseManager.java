package com.sougata.form_data_service.service.responseManager;

import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.formResponse.individual.QuestionResponseIndividualDto;
import com.sougata.form_engine.dto.question.responseputrequest.QuestionResponsePutReqDto;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public abstract class ResponseManager<QR extends QuestionResponsePutReqDto, QRID extends QuestionResponseIndividualDto> {

    public abstract CompletableFuture<Void> create(QR response, UUID formResponseId);

    public abstract CompletableFuture<List<QRID>> getResponses(UUID formResponseId, List<Long> questionIds);

    public abstract CompletableFuture<Void> deleteAllByQuestionIdsAndFormResponseId(List<Long> questionIds, UUID formResponseId);

    public abstract QuestionType getQuestionType();

}
