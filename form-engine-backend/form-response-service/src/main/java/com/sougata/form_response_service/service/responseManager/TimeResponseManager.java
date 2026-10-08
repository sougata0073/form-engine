package com.sougata.form_response_service.service.responseManager;

import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.formResponse.individual.TimeResponseIndividualDto;
import com.sougata.form_engine.dto.formResponse.question.TimeResponseQuestionDto;
import com.sougata.form_engine.dto.formResponse.summary.TimeResponseSummaryDto;
import com.sougata.form_engine.dto.pgfunctionparameter.QuestionResponseBatches;
import com.sougata.form_engine.dto.question.details.TimeDetailsDto;
import com.sougata.form_engine.dto.question.responseputreqbatch.FormResponseInfoQuestionResponse;
import com.sougata.form_engine.dto.question.responseputreqbatch.TimeResponseBatch;
import com.sougata.form_engine.dto.question.responseputrequest.TimeResponsePutReqDto;
import com.sougata.form_engine.util.IdUtil;
import com.sougata.form_engine.util.JsonUtil;
import com.sougata.form_response_service.model.QuestionResponseSummary;
import com.sougata.form_response_service.repository.QuestionResponseSummaryRepository;
import com.sougata.form_response_service.repository.TimeResponseRepository;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service("TIME_RESPONSE_MANAGER")
@RequiredArgsConstructor
public class TimeResponseManager extends ResponseManager<
        TimeDetailsDto,
        TimeResponsePutReqDto,
        TimeResponseSummaryDto,
        TimeResponseQuestionDto,
        TimeResponseQuestionDto.Response,
        TimeResponseIndividualDto,
        TimeResponseBatch,
        TimeResponseBatch.Response
        > {

    private final TimeResponseRepository timeRepository;
    private final QuestionResponseSummaryRepository questionResponseSummaryRepository;

    @Override
    public void saveBatched(List<TimeResponseBatch> timeResponseBatches) {
        var responseBatches = new QuestionResponseBatches<>(timeResponseBatches);
        var responseBatchesJson = JsonUtil.toJson(responseBatches);

        timeRepository.createOrIncrement(
                responseBatchesJson
        );

    }

    @Override
    public List<TimeResponseSummaryDto> getResponseSummaries(UUID formId, List<TimeDetailsDto> questionDetailsList) {
        var questionResponseSummaries = questionResponseSummaryRepository.findAllByFormId(formId);

        var questionResponseSummariesMapByQuestionId = questionResponseSummaries
                .stream()
                .collect(Collectors.toMap(QuestionResponseSummary::getQuestionId, Function.identity()));

        return questionDetailsList.stream().map(qd -> {
            var questionResponseSummary = questionResponseSummariesMapByQuestionId.get(qd.getId());

            var t = new TimeResponseSummaryDto();

            t.setQuestionId(qd.getId());
            t.setQuestion(qd.getQuestion());
            t.setOrderIndex(qd.getOrderIndex());
            t.setNumberOfResponses(
                    questionResponseSummary == null ? 0L : questionResponseSummary.getResponseCount()
            );
            t.setQuestionType(qd.getQuestionType());
            t.setResponses(List.of());

            return t;

        }).toList();
    }

    @Override
    public TimeResponseSummaryDto getResponseSummary(Long questionId, TimeDetailsDto questionRes, Pageable pageable) {
        var questionResponseSummaryOptional = questionResponseSummaryRepository.findByQuestionId(questionId);
        var groupedByHour = timeRepository.groupedByHour(questionId, pageable);

        var t = new TimeResponseSummaryDto();

        t.setNumberOfResponses(
                questionResponseSummaryOptional.isEmpty()
                        ? 0L : questionResponseSummaryOptional.get().getResponseCount()
        );

        var responses = groupedByHour.stream()
                .map(tuple -> {
                    var res = new TimeResponseSummaryDto.Response();

                    var times = tuple.get("times", String[].class);
                    var timeCounts = tuple.get("timeCounts", Long[].class);

                    if (times.length != timeCounts.length) {
                        throw new RuntimeException("Time and time count array length mismatch. Time array length: " + times.length + ". Time count array length: " + timeCounts.length);
                    }

                    var timeCountPairs = new ArrayList<TimeResponseSummaryDto.TimeCountPair>();

                    for (int i = 0; i < times.length; i++) {
                        timeCountPairs.add(
                                new TimeResponseSummaryDto.TimeCountPair(
                                        Instant.parse(times[i]), timeCounts[i]
                                )
                        );
                    }

                    res.setHour(tuple.get("hour", Integer.class));
                    res.setTimes(timeCountPairs);

                    return res;
                }).toList();

        t.setResponses(responses);

        return t;
    }

    @Override
    public TimeResponseQuestionDto getResponseByQuestion(UUID formId, Long questionId, Map<String, String> extraParams, Pageable pageable) {
        var grouped = timeRepository.groupedByTime(formId, questionId, pageable);

        var t = new TimeResponseQuestionDto();

        var responses = grouped.stream().map(g -> {
            var res = new TimeResponseQuestionDto.Response();

            res.setTime(g.get("time", Instant.class));
            res.setResponseCount(g.get("responseCount", Long.class));

            var map = new HashMap<String, List<String>>();

            map.put("time", List.of(res.getTime() == null ? "" : res.getTime().toString()));

            res.setFormResponsesIdentifier(IdUtil.generateCompressedEncodedId(map));

            return res;

        }).toList();

        t.setResponses(responses);

        return t;
    }

    @Override
    public List<TimeResponseIndividualDto> getIndividualResponses(UUID formId, Long formResponseId) {
//        var responses = timeRepository.getTimesByFormResponse(formResponseId);
//
//        return responses.stream().map(tuple -> {
//            var qId = tuple.get("questionId", Long.class);
//            var time = tuple.get("time", Instant.class);
//
//            var res = new TimeResponseIndividualDto();
//
//            res.setQuestionId(qId);
//            res.setQuestionType(getQuestionType());
//            res.setTime(time);
//
//            return res;
//        }).toList();

        return null;
    }

    @Override
    public List<Tuple> getFormResponseAndUserIds(UUID formId, Long questionId, String formResponsesIdentifier, Pageable pageable) {
//        var map = IdUtil.reconstructCompressedEncodedId(formResponsesIdentifier);
//
//        var time = map.get("time");
//
//        if (time.isEmpty()) {
//            throw new IllegalArgumentException("Invalid Form Responses Identifier. Identifier: " + formResponsesIdentifier);
//        }
//
//        var groupedResponse = time.getFirst() == null ? null : Instant.parse(time.getFirst());
//
//        return timeRepository.getResponseIdsByGroupedResponse(formId, questionId, groupedResponse, pageable);

        return null;
    }

    @Override
    public TimeResponseBatch mapToBatchResponse(List<FormResponseInfoQuestionResponse<TimeResponsePutReqDto>> questionResponsePutReqs) {
        var batch = new TimeResponseBatch();

        var responseMap = new HashMap<Instant, ArrayList<UUID>>();

        questionResponsePutReqs.forEach(q ->
                responseMap
                        .computeIfAbsent(q.getQuestionResponsePutReq().getTime(), _ -> new ArrayList<>())
                        .add(q.getFormResponseId())
        );

        var responses = responseMap.entrySet()
                .stream()
                .map(entry -> {
                    var res = new TimeResponseBatch.Response();

                    res.setTime(entry.getKey());
                    res.setFormResponseIds(entry.getValue());
                    res.setResponseCount((long) entry.getValue().size());

                    return res;
                }).toList();

        batch.setResponses(responses);

        return batch;
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.TIME;
    }

}
