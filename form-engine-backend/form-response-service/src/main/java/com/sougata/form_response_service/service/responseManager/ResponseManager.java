package com.sougata.form_response_service.service.responseManager;

import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.formResponse.individual.QuestionResponseIndividualDto;
import com.sougata.form_engine.dto.formResponse.question.ResponseByQuestionResponse;
import com.sougata.form_engine.dto.formResponse.question.ResponseQuestionDto;
import com.sougata.form_engine.dto.formResponse.summary.ResponseSummaryDto;
import com.sougata.form_engine.dto.question.details.QuestionDetailsDto;
import com.sougata.form_engine.dto.question.responseputrequest.QuestionResponsePutReqDto;
import com.sougata.form_response_service.model.QuestionResponseSummary;
import com.sougata.form_response_service.repository.FormResponseRepository;
import com.sougata.form_response_service.repository.QuestionResponseSummaryRepository;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public abstract class ResponseManager<
        TQuestionDetails extends QuestionDetailsDto,
        TQuestionResponsePutReq extends QuestionResponsePutReqDto,
        TResponseSummary extends ResponseSummaryDto<?>,
        ResByQ extends ResponseQuestionDto<ResByQRes>,
        ResByQRes extends ResponseByQuestionResponse,
        ResIndi extends QuestionResponseIndividualDto
        > {

    private final FormResponseRepository formResponseRepository;
    private final QuestionResponseSummaryRepository questionResponseSummaryRepository;

    protected ResponseManager(FormResponseRepository formResponseRepository, QuestionResponseSummaryRepository questionResponseSummaryRepository) {
        this.formResponseRepository = formResponseRepository;
        this.questionResponseSummaryRepository = questionResponseSummaryRepository;
    }

    public abstract void create(UUID formId, TQuestionDetails questionDetails);

    public abstract void update(UUID formId, TQuestionDetails questionDetails, Set<String> updatedFields);

    public abstract void update(UUID formId, TQuestionResponsePutReq questionResponsePutReq);

    public abstract List<TResponseSummary> getResponseSummaries(UUID formId, List<TQuestionDetails> questionDetailsList);

    public abstract TResponseSummary getResponseSummary(Long questionId, TQuestionDetails questionDetails, Pageable pageable);

    public abstract ResByQ getResponseByQuestion(UUID formId, Long questionId, Map<String, String> extraParams, Pageable pageable);

    public abstract List<ResIndi> getIndividualResponses(UUID formId, Long formResponseId);

    public abstract List<Tuple> getFormResponseAndUserIds(UUID formId, Long questionId, String formResponsesIdentifier, Pageable pageable);

    public abstract QuestionType getQuestionType();

    public QuestionResponseSummary createQuestionResponseSummary(UUID formId, TQuestionDetails questionDetails) {
        var formResponse = formResponseRepository.findById(formId)
                .orElseThrow(() -> new IllegalArgumentException("Form response summary not found with ID: " + formId));

        var qResSummary = new QuestionResponseSummary();

        qResSummary.setFormResponseSummary(formResponse);
        qResSummary.setQuestionId(questionDetails.getId());
        qResSummary.setResponseCount(0L);

        return questionResponseSummaryRepository.save(qResSummary);
    }

}
