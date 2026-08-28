package com.sougata.form_response_service.service.responseManager;

import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.formResponse.individual.ResponseIndividualDto;
import com.sougata.form_engine.dto.formResponse.question.ResponseByQuestionResponse;
import com.sougata.form_engine.dto.formResponse.question.ResponseQuestionDto;
import com.sougata.form_engine.dto.formResponse.summary.ResponseSummaryDto;
import com.sougata.form_engine.dto.question.details.QuestionDetailsDto;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class ResponseManager<
        RS extends ResponseSummaryDto<?>,
        QRes extends QuestionDetailsDto,
        ResByQ extends ResponseQuestionDto<ResByQRes>,
        ResByQRes extends ResponseByQuestionResponse,
        ResIndi extends ResponseIndividualDto
        > {

    public abstract List<RS> getResponseSummaries(UUID formId, List<QRes> questionResponses);

    public abstract RS getResponseSummary(UUID formId, Long questionId, QRes questionRes, Pageable pageable);

    public abstract ResByQ getResponseByQuestion(UUID formId, Long questionId, Map<String, String> extraParams, Pageable pageable);

    public abstract List<ResIndi> getIndividualResponses(UUID formId, Long formResponseId);

    public abstract List<Tuple> getFormResponseAndUserIds(UUID formId, Long questionId, String formResponsesIdentifier, Pageable pageable);

    public abstract QuestionType getQuestionType();


}
