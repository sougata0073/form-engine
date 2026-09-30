package com.sougata.form_response_service.service.responseManager;

import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.formResponse.individual.DateResponseIndividualDto;
import com.sougata.form_engine.dto.formResponse.question.DateResponseQuestionDto;
import com.sougata.form_engine.dto.formResponse.summary.DateResponseSummaryDto;
import com.sougata.form_engine.dto.pgfunctionparameter.ResponseIncrementOrCreate;
import com.sougata.form_engine.dto.question.details.DateDetailsDto;
import com.sougata.form_engine.dto.question.responseputreqbatch.CheckboxResponseBatch;
import com.sougata.form_engine.dto.question.responseputreqbatch.DateResponseBatch;
import com.sougata.form_engine.dto.question.responseputreqbatch.QuestionResponseManagerBatchInput;
import com.sougata.form_engine.dto.question.responseputrequest.DateResponsePutReqDto;
import com.sougata.form_engine.util.JsonUtil;
import com.sougata.form_response_service.repository.DateResponseRepository;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Service("DATE_RESPONSE_MANAGER")
public class DateResponseManager extends ResponseManager<
        DateDetailsDto,
        DateResponsePutReqDto,
        DateResponseSummaryDto,
        DateResponseQuestionDto,
        DateResponseQuestionDto.Response,
        DateResponseIndividualDto,
        DateResponseBatch,
        DateResponseBatch.Response
        > {

    private final DateResponseRepository dateRepository;

    public DateResponseManager(DateResponseRepository dateRepository) {
        this.dateRepository = dateRepository;
    }

    @Override
    @Transactional
    public void onResponseSave(UUID formId, UUID formResponseId, List<DateResponsePutReqDto> questionResponsePutRequests) {

        var responseData = new ResponseIncrementOrCreate<>(questionResponsePutRequests);
        var responseDataJson = JsonUtil.toJson(responseData);

        dateRepository.createOrIncrement(
                responseDataJson, formResponseId, 1L
        );

    }

    @Override
    public List<DateResponseSummaryDto> getResponseSummaries(UUID formId, List<DateDetailsDto> questionResponses) {
//        var responseSummaries = dateRepository.getResponseSummaries(formId);
//        var result = new ArrayList<DateResponseSummaryDto>();
//
//        questionResponses.forEach(qr ->
//                result.add(
//                        responseSummaries.stream()
//                                .filter(rs -> Objects.equals(rs.questionId(), qr.getId()))
//                                .map(rs -> {
//                                    var d = new DateResponseSummaryDto();
//
//                                    d.setQuestionId(qr.getId());
//                                    d.setQuestion(qr.getQuestion());
//                                    d.setOrderIndex(qr.getOrderIndex());
//                                    d.setNumberOfResponses(rs.numberOfResponses());
//                                    d.setQuestionType(QuestionType.DATE);
//                                    d.setResponses(List.of());
//
//                                    return d;
//                                })
//                                .findFirst()
//                                .orElseGet(() -> {
//                                    var d = new DateResponseSummaryDto();
//
//                                    d.setQuestionId(qr.getId());
//                                    d.setQuestion(qr.getQuestion());
//                                    d.setOrderIndex(qr.getOrderIndex());
//                                    d.setNumberOfResponses(0L);
//                                    d.setQuestionType(QuestionType.DATE);
//                                    d.setResponses(List.of());
//
//                                    return d;
//                                })
//                ));
//
//        return result;

        return null;
    }

    @Override
    public DateResponseSummaryDto getResponseSummary(Long questionId, DateDetailsDto questionRes, Pageable pageable) {
//        var responseSummary = dateRepository.getResponseSummary(formId, questionId);
//        var dateResponses = dateRepository.getResponseDates(questionId, pageable);
//
//        var d = new DateResponseSummaryDto();
//
//        d.setQuestionId(questionRes.getId());
//        d.setQuestion(questionRes.getQuestion());
//        d.setOrderIndex(questionRes.getOrderIndex());
//        d.setNumberOfResponses(responseSummary.numberOfResponses());
//        d.setQuestionType(getQuestionType());
//
//        var responses = dateResponses.stream().map(tuple -> {
//            var res = new DateResponseSummaryDto.Response();
//
//            res.setYear(tuple.get("year", Integer.class));
//            res.setMonth(tuple.get("month", Integer.class));
//
//            var dates = tuple.get("dates", String[].class);
//            var dateCounts = tuple.get("dateCounts", Long[].class);
//
//            var dateCountPairs = new ArrayList<DateResponseSummaryDto.DateCountPair>();
//
//            for (int i = 0; i < dates.length; i++) {
//                dateCountPairs.add(
//                        new DateResponseSummaryDto.DateCountPair(
//                                Instant.parse(dates[i]), dateCounts[i]
//                        )
//                );
//            }
//
//            res.setDates(dateCountPairs);
//
//            return res;
//        }).toList();
//
//        d.setResponses(responses);
//
//        return d;

        return null;
    }

    @Override
    public DateResponseQuestionDto getResponseByQuestion(UUID formId, Long questionId, Map<String, String> extraParams, Pageable pageable) {
//        var grouped = dateRepository.groupedByDate(formId, questionId, pageable);
//
//        var d = new DateResponseQuestionDto();
//
//        var responses = grouped.stream().map(g -> {
//            var res = new DateResponseQuestionDto.Response();
//
//            res.setQuestionId(questionId);
//            res.setQuestionType(getQuestionType());
//            res.setDate(g.get("date", Instant.class));
//            res.setResponseCount(g.get("responseCount", Long.class));
//
//            var map = new HashMap<String, List<String>>();
//
//            map.put("date", List.of(res.getDate() == null ? "" : res.getDate().toString()));
//
//            res.setFormResponsesIdentifier(IdUtil.generateCompressedEncodedId(map));
//
//            return res;
//
//        }).toList();
//
//        d.setQuestionId(questionId);
//        d.setQuestionType(getQuestionType());
//        d.setResponses(responses);
//
//        return d;

        return null;
    }

    @Override
    public List<DateResponseIndividualDto> getIndividualResponses(UUID formId, Long formResponseId) {
//        var responses = dateRepository.getDatesByFormResponse(formResponseId);
//
//        return responses.stream().map(tuple -> {
//            var qId = tuple.get("questionId", Long.class);
//            var date = tuple.get("date", Instant.class);
//
//            var res = new DateResponseIndividualDto();
//
//            res.setQuestionId(qId);
//            res.setQuestionType(getQuestionType());
//            res.setDate(date);
//
//            return res;
//        }).toList();

        return null;
    }

    @Override
    public List<Tuple> getFormResponseAndUserIds(UUID formId, Long questionId, String formResponsesIdentifier, Pageable pageable) {
//        var map = IdUtil.reconstructCompressedEncodedId(formResponsesIdentifier);
//
//        var date = map.get("date");
//
//        if (date.isEmpty()) {
//            throw new IllegalArgumentException("Invalid Form Responses Identifier. Identifier: " + formResponsesIdentifier);
//        }
//
//        var groupedResponse = date.getFirst() == null ? null :  Instant.parse(date.getFirst());
//
//        return dateRepository.getResponseIdsByGroupedResponse(formId, questionId, groupedResponse, pageable);

        return null;
    }

    @Override
    public DateResponseBatch mapToBatchResponse(Long questionId, List<QuestionResponseManagerBatchInput<DateResponsePutReqDto>> questionResponsePutReqs) {
        var batch = new DateResponseBatch();

        var responseMap = new HashMap<Instant, ArrayList<UUID>>();

        questionResponsePutReqs.forEach(q ->
                responseMap
                        .computeIfAbsent(q.getQuestionResponsePutReq().getDate(), _ -> new ArrayList<>())
                        .add(q.getFormResponseId())
        );

        batch.setQuestionId(questionId);
        batch.setQuestionType(getQuestionType());
        batch.setResponseCount((long) questionResponsePutReqs.size());

        var responses = responseMap.entrySet()
                .stream()
                .map(entry -> {
                    var res = new DateResponseBatch.Response();

                    res.setDate(entry.getKey());
                    res.setFormResponseIds(entry.getValue());

                    return res;
                }).toList();

        batch.setResponses(responses);

        return batch;
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.DATE;
    }

}
