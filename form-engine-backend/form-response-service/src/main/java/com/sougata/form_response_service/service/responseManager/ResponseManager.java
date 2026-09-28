package com.sougata.form_response_service.service.responseManager;

import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.formResponse.individual.QuestionResponseIndividualDto;
import com.sougata.form_engine.dto.formResponse.question.ResponseByQuestionResponse;
import com.sougata.form_engine.dto.formResponse.question.ResponseQuestionDto;
import com.sougata.form_engine.dto.formResponse.summary.ResponseSummaryDto;
import com.sougata.form_engine.dto.question.details.QuestionDetailsDto;
import com.sougata.form_engine.dto.question.responseputreqbatch.QuestionResponseBatch;
import com.sougata.form_engine.dto.question.responseputreqbatch.QuestionResponseManagerBatchInput;
import com.sougata.form_engine.dto.question.responseputrequest.QuestionResponsePutReqDto;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class ResponseManager<
        TQuestionDetails extends QuestionDetailsDto,
        TQuestionResponsePutReq extends QuestionResponsePutReqDto,
        TResponseSummary extends ResponseSummaryDto<?>,
        ResByQ extends ResponseQuestionDto<ResByQRes>,
        ResByQRes extends ResponseByQuestionResponse,
        ResIndi extends QuestionResponseIndividualDto,
        QResBatch extends QuestionResponseBatch<QResBatchResponse>,
        QResBatchResponse extends QuestionResponseBatch.Response
        > {

    public abstract void onResponseSave(UUID formId, UUID formResponseId, List<TQuestionResponsePutReq> questionResponsePutRequests);

    public abstract List<TResponseSummary> getResponseSummaries(UUID formId, List<TQuestionDetails> questionDetailsList);

    public abstract TResponseSummary getResponseSummary(Long questionId, TQuestionDetails questionDetails, Pageable pageable);

    public abstract ResByQ getResponseByQuestion(UUID formId, Long questionId, Map<String, String> extraParams, Pageable pageable);

    public abstract List<ResIndi> getIndividualResponses(UUID formId, Long formResponseId);

    public abstract List<Tuple> getFormResponseAndUserIds(UUID formId, Long questionId, String formResponsesIdentifier, Pageable pageable);

    public abstract QResBatch mapToBatchResponse(Long questionId, List<QuestionResponseManagerBatchInput<TQuestionResponsePutReq>> questionResponsePutReqs);

    public abstract QuestionType getQuestionType();

}
