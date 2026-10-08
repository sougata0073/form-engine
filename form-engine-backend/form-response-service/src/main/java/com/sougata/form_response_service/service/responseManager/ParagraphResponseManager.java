package com.sougata.form_response_service.service.responseManager;

import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.formResponse.individual.ParagraphResponseIndividualDto;
import com.sougata.form_engine.dto.formResponse.question.ParagraphResponseQuestionDto;
import com.sougata.form_engine.dto.formResponse.summary.ParagraphResponseSummaryDto;
import com.sougata.form_engine.dto.pgfunctionparameter.QuestionResponseBatches;
import com.sougata.form_engine.dto.question.details.ParagraphDetailsDto;
import com.sougata.form_engine.dto.question.responseputreqbatch.FormResponseInfoQuestionResponse;
import com.sougata.form_engine.dto.question.responseputreqbatch.ParagraphResponseBatch;
import com.sougata.form_engine.dto.question.responseputrequest.ParagraphResponsePutReqDto;
import com.sougata.form_engine.util.IdUtil;
import com.sougata.form_engine.util.JsonUtil;
import com.sougata.form_engine.util.StringUtil;
import com.sougata.form_response_service.model.QuestionResponseSummary;
import com.sougata.form_response_service.repository.ParagraphResponseRepository;
import com.sougata.form_response_service.repository.QuestionResponseSummaryRepository;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service("PARAGRAPH_RESPONSE_MANAGER")
@RequiredArgsConstructor
public class ParagraphResponseManager extends ResponseManager<
        ParagraphDetailsDto,
        ParagraphResponsePutReqDto,
        ParagraphResponseSummaryDto,
        ParagraphResponseQuestionDto,
        ParagraphResponseQuestionDto.Response,
        ParagraphResponseIndividualDto,
        ParagraphResponseBatch,
        ParagraphResponseBatch.Response
        > {

    private final ParagraphResponseRepository paragraphRepository;
    private final QuestionResponseSummaryRepository questionResponseSummaryRepository;

    @Override
    public void saveBatched(List<ParagraphResponseBatch> paragraphResponseBatches) {
        var responseBatches = new QuestionResponseBatches<>(paragraphResponseBatches);
        var responseBatchesJson = JsonUtil.toJson(responseBatches);

        paragraphRepository.createOrIncrement(
                responseBatchesJson
        );

    }

    @Override
    public List<ParagraphResponseSummaryDto> getResponseSummaries(UUID formId, List<ParagraphDetailsDto> questionDetailsList) {
        var questionResponseSummaries = questionResponseSummaryRepository.findAllByFormId(formId);

        var questionResponseSummariesMapByQuestionId = questionResponseSummaries
                .stream()
                .collect(Collectors.toMap(QuestionResponseSummary::getQuestionId, Function.identity()));

        return questionDetailsList.stream().map(qd -> {
            var questionResponseSummary = questionResponseSummariesMapByQuestionId.get(qd.getId());

            var p = new ParagraphResponseSummaryDto();

            p.setQuestionId(qd.getId());
            p.setQuestion(qd.getQuestion());
            p.setOrderIndex(qd.getOrderIndex());
            p.setNumberOfResponses(
                    questionResponseSummary == null ? 0L : questionResponseSummary.getResponseCount()
            );
            p.setQuestionType(qd.getQuestionType());
            p.setResponses(List.of());

            return p;

        }).toList();
    }

    @Override
    public ParagraphResponseSummaryDto getResponseSummary(Long questionId, ParagraphDetailsDto questionRes, Pageable pageable) {
        var questionResponseSummaryOptional = questionResponseSummaryRepository.findByQuestionId(questionId);
        var texts = paragraphRepository.getResponseTexts(questionId, pageable);

        var p = new ParagraphResponseSummaryDto();

        p.setNumberOfResponses(
                questionResponseSummaryOptional.isEmpty()
                        ? 0L : questionResponseSummaryOptional.get().getResponseCount()
        );
        p.setResponses(texts);

        return p;
    }

    @Override
    public ParagraphResponseQuestionDto getResponseByQuestion(UUID formId, Long questionId, Map<String, String> extraParams, Pageable pageable) {
        var grouped = paragraphRepository.groupedByText(questionId, pageable);

        var p = new ParagraphResponseQuestionDto();

        var responses = grouped.stream().map(g -> {
            var res = new ParagraphResponseQuestionDto.Response();

            res.setText(g.get("text", String.class));
            res.setResponseCount(g.get("responseCount", Long.class));

            var map = new HashMap<String, List<String>>();

            map.put("text", List.of(StringUtil.emptyIfNull(res.getText())));

            res.setFormResponsesIdentifier(IdUtil.generateCompressedEncodedId(map));

            return res;
        }).toList();

        p.setResponses(responses);

        return p;
    }

    @Override
    public List<ParagraphResponseIndividualDto> getIndividualResponses(UUID formId, Long formResponseId) {
//        var responses = paragraphRepository.getTextsByFormResponse(formResponseId);
//
//        return responses.stream().map(tuple -> {
//            var qId = tuple.get("questionId", Long.class);
//            var text = tuple.get("text", String.class);
//
//            var res = new ParagraphResponseIndividualDto();
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
//        return paragraphRepository.getResponseIdsByGroupedResponse(formId, questionId, groupedResponse, pageable);

        return null;
    }

    @Override
    public ParagraphResponseBatch mapToBatchResponse(List<FormResponseInfoQuestionResponse<ParagraphResponsePutReqDto>> questionResponsePutReqs) {
        var batch = new ParagraphResponseBatch();

        var responseMap = new HashMap<String, ArrayList<UUID>>();

        questionResponsePutReqs.forEach(q ->
                responseMap
                        .computeIfAbsent(q.getQuestionResponsePutReq().getText(), _ -> new ArrayList<>())
                        .add(q.getFormResponseId())
        );

        var responses = responseMap.entrySet()
                .stream()
                .map(entry -> {
                    var res = new ParagraphResponseBatch.Response();

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
        return QuestionType.PARAGRAPH;
    }

}
