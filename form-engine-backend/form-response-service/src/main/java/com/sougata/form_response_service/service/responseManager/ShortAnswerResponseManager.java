package com.sougata.form_response_service.service.responseManager;

import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.formResponse.individual.ShortAnswerResponseIndividualDto;
import com.sougata.form_engine.dto.formResponse.question.ShortAnswerResponseQuestionDto;
import com.sougata.form_engine.dto.formResponse.summary.ShortAnswerResponseSummaryDto;
import com.sougata.form_engine.dto.pgfunctionparameter.QuestionResponseBatches;
import com.sougata.form_engine.dto.question.details.ShortAnswerDetailsDto;
import com.sougata.form_engine.dto.question.responseputreqbatch.FormResponseInfoQuestionResponse;
import com.sougata.form_engine.dto.question.responseputreqbatch.ShortAnswerResponseBatch;
import com.sougata.form_engine.dto.question.responseputrequest.ShortAnswerResponsePutReqDto;
import com.sougata.form_engine.util.IdUtil;
import com.sougata.form_engine.util.JsonUtil;
import com.sougata.form_engine.util.StringUtil;
import com.sougata.form_response_service.model.QuestionResponseSummary;
import com.sougata.form_response_service.repository.QuestionResponseSummaryRepository;
import com.sougata.form_response_service.repository.ShortAnswerResponseRepository;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service("SHORT_ANSWER_RESPONSE_MANAGER")
@RequiredArgsConstructor
public class ShortAnswerResponseManager extends ResponseManager<
        ShortAnswerDetailsDto,
        ShortAnswerResponsePutReqDto,
        ShortAnswerResponseSummaryDto,
        ShortAnswerResponseQuestionDto,
        ShortAnswerResponseQuestionDto.Response,
        ShortAnswerResponseIndividualDto,
        ShortAnswerResponseBatch,
        ShortAnswerResponseBatch.Response
        > {

    private final ShortAnswerResponseRepository shortAnswerRepository;
    private final QuestionResponseSummaryRepository questionResponseSummaryRepository;

    @Override
    public void saveBatched(List<ShortAnswerResponseBatch> shortAnswerResponseBatches) {
        var responseBatches = new QuestionResponseBatches<>(shortAnswerResponseBatches);
        var responseBatchesJson = JsonUtil.toJson(responseBatches);

        shortAnswerRepository.createOrIncrement(
                responseBatchesJson
        );

    }

    @Override
    public List<ShortAnswerResponseSummaryDto> getResponseSummaries(UUID formId, List<ShortAnswerDetailsDto> questionDetailsList) {
        var questionResponseSummaries = questionResponseSummaryRepository.findAllByFormId(formId);

        var questionResponseSummariesMapByQuestionId = questionResponseSummaries
                .stream()
                .collect(Collectors.toMap(QuestionResponseSummary::getQuestionId, Function.identity()));

        return questionDetailsList.stream().map(qd -> {
            var questionResponseSummary = questionResponseSummariesMapByQuestionId.get(qd.getId());

            var sa = new ShortAnswerResponseSummaryDto();

            sa.setQuestionId(qd.getId());
            sa.setQuestion(qd.getQuestion());
            sa.setOrderIndex(qd.getOrderIndex());
            sa.setNumberOfResponses(
                    questionResponseSummary == null ? 0L : questionResponseSummary.getResponseCount()
            );
            sa.setQuestionType(qd.getQuestionType());
            sa.setResponses(List.of());

            return sa;

        }).toList();
    }

    @Override
    public ShortAnswerResponseSummaryDto getResponseSummary(Long questionId, ShortAnswerDetailsDto questionRes, Pageable pageable) {
        var questionResponseSummaryOptional = questionResponseSummaryRepository.findByQuestionId(questionId);
        var texts = shortAnswerRepository.getResponseTexts(questionId, pageable);

        var sa = new ShortAnswerResponseSummaryDto();

        sa.setNumberOfResponses(
                questionResponseSummaryOptional.isEmpty()
                        ? 0L : questionResponseSummaryOptional.get().getResponseCount()
        );
        sa.setResponses(texts);

        return sa;
    }

    @Override
    public ShortAnswerResponseQuestionDto getResponseByQuestion(UUID formId, Long questionId, Map<String, String> extraParams, Pageable pageable) {

        var grouped = shortAnswerRepository.groupedByText(questionId, pageable);

        var sa = new ShortAnswerResponseQuestionDto();

        var responses = grouped.stream().map(g -> {
            var res = new ShortAnswerResponseQuestionDto.Response();

            res.setText(g.get("text", String.class));
            res.setResponseCount(g.get("responseCount", Long.class));

            var map = new HashMap<String, List<String>>();

            map.put("text", List.of(StringUtil.emptyIfNull(res.getText())));

            res.setFormResponsesIdentifier(IdUtil.generateCompressedEncodedId(map));

            return res;
        }).toList();

        sa.setResponses(responses);

        return sa;
    }

    @Override
    public List<ShortAnswerResponseIndividualDto> getIndividualResponses(UUID formId, Long formResponseId) {
//        var responses = shortAnswerRepository.getTextsByFormResponse(formResponseId);
//
//        return responses.stream().map(tuple -> {
//            var qId = tuple.get("questionId", Long.class);
//            var text = tuple.get("text", String.class);
//
//            var res = new ShortAnswerResponseIndividualDto();
//
//            res.setQuestionId(qId);
//            res.setQuestionType(getQuestionType());
//            res.setText(text);
//
//            return res;
//        }).toList();

        return null;
    }

    @Override
    public List<Tuple> getFormResponseAndUserIds(UUID formId, Long questionId, String formResponsesIdentifier, Pageable pageable) {
//        var map = IdUtil.reconstructCompressedEncodedId(formResponsesIdentifier);
//
//        var text = map.get("text");
//
//        if (text.isEmpty()) {
//            throw new IllegalArgumentException("Invalid Form Responses Identifier. Identifier: " + formResponsesIdentifier);
//        }
//
//        var groupedResponse = text.getFirst();
//
//        return shortAnswerRepository.getResponseIdsByGroupedResponse(formId, questionId, groupedResponse, pageable);

        return null;
    }

    @Override
    public ShortAnswerResponseBatch mapToBatchResponse(List<FormResponseInfoQuestionResponse<ShortAnswerResponsePutReqDto>> questionResponsePutReqs) {
        var batch = new ShortAnswerResponseBatch();

        var responseMap = new HashMap<String, ArrayList<UUID>>();

        questionResponsePutReqs.forEach(q ->
                responseMap
                        .computeIfAbsent(q.getQuestionResponsePutReq().getText(), _ -> new ArrayList<>())
                        .add(q.getFormResponseId())
        );

        var responses = responseMap.entrySet()
                .stream()
                .map(entry -> {
                    var res = new ShortAnswerResponseBatch.Response();

                    res.setText(entry.getKey());
                    res.setFormResponseIds(entry.getValue());
                    res.setResponseCount((long) entry.getValue().size());

                    return res;
                }).toList();

        batch.setResponses(responses);

        return batch;
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.SHORT_ANSWER;
    }

}
