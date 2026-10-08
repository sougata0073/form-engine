package com.sougata.form_response_service.service.responseManager;

import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.formResponse.individual.DurationResponseIndividualDto;
import com.sougata.form_engine.dto.formResponse.question.DurationResponseQuestionDto;
import com.sougata.form_engine.dto.formResponse.summary.DurationResponseSummaryDto;
import com.sougata.form_engine.dto.pgfunctionparameter.QuestionResponseBatches;
import com.sougata.form_engine.dto.question.details.DurationDetailsDto;
import com.sougata.form_engine.dto.question.responseputreqbatch.DurationResponseBatch;
import com.sougata.form_engine.dto.question.responseputreqbatch.FormResponseInfoQuestionResponse;
import com.sougata.form_engine.dto.question.responseputrequest.DurationResponsePutReqDto;
import com.sougata.form_engine.util.IdUtil;
import com.sougata.form_engine.util.JsonUtil;
import com.sougata.form_response_service.model.QuestionResponseSummary;
import com.sougata.form_response_service.repository.DurationResponseRepository;
import com.sougata.form_response_service.repository.QuestionResponseSummaryRepository;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service("DURATION_RESPONSE_MANAGER")
@RequiredArgsConstructor
public class DurationResponseManager extends ResponseManager<
        DurationDetailsDto,
        DurationResponsePutReqDto,
        DurationResponseSummaryDto,
        DurationResponseQuestionDto,
        DurationResponseQuestionDto.Response,
        DurationResponseIndividualDto,
        DurationResponseBatch,
        DurationResponseBatch.Response
        > {

    private final DurationResponseRepository durationRepository;
    private final QuestionResponseSummaryRepository questionResponseSummaryRepository;

    @Override
    public void saveBatched(List<DurationResponseBatch> durationResponseBatches) {
        var responseBatches = new QuestionResponseBatches<>(durationResponseBatches);
        var responseBatchesJson = JsonUtil.toJson(responseBatches);

        durationRepository.createOrIncrement(
                responseBatchesJson
        );

    }

    @Override
    public List<DurationResponseSummaryDto> getResponseSummaries(UUID formId, List<DurationDetailsDto> questionDetailsList) {
        var questionResponseSummaries = questionResponseSummaryRepository.findAllByFormId(formId);

        var questionResponseSummariesMapByQuestionId = questionResponseSummaries
                .stream()
                .collect(Collectors.toMap(QuestionResponseSummary::getQuestionId, Function.identity()));

        return questionDetailsList.stream().map(qd -> {
            var questionResponseSummary = questionResponseSummariesMapByQuestionId.get(qd.getId());

            var dr = new DurationResponseSummaryDto();

            dr.setQuestionId(qd.getId());
            dr.setQuestion(qd.getQuestion());
            dr.setOrderIndex(qd.getOrderIndex());
            dr.setNumberOfResponses(
                    questionResponseSummary == null ? 0L : questionResponseSummary.getResponseCount()
            );
            dr.setQuestionType(qd.getQuestionType());
            dr.setResponses(List.of());

            return dr;

        }).toList();
    }

    @Override
    public DurationResponseSummaryDto getResponseSummary(Long questionId, DurationDetailsDto questionRes, Pageable pageable) {
        var questionResponseSummaryOptional = questionResponseSummaryRepository.findByQuestionId(questionId);
        var groupedByHour = durationRepository.groupedByHour(questionId, pageable);

        var d = new DurationResponseSummaryDto();

        d.setNumberOfResponses(
                questionResponseSummaryOptional.isEmpty()
                        ? 0L : questionResponseSummaryOptional.get().getResponseCount()
        );

        var responses = groupedByHour.stream().map(tuple -> {
            var res = new DurationResponseSummaryDto.Response();

            res.setHours(tuple.get("hours", Integer.class));

            var minutes = tuple.get("minutes", Integer[].class);
            var seconds = tuple.get("seconds", Integer[].class);
            var responseCounts = tuple.get("responseCounts", Long[].class);

            var durationCountPairs = new ArrayList<DurationResponseSummaryDto.DurationCountPair>();

            for (int i = 0; i < minutes.length; i++) {
                var min = minutes[i];
                var sec = seconds[i];
                var cnt = responseCounts[i];

                durationCountPairs.add(
                        new DurationResponseSummaryDto.DurationCountPair(
                                min, sec, cnt
                        )
                );
            }

            res.setDurations(durationCountPairs);

            return res;
        }).toList();

        d.setResponses(responses);

        return d;
    }

    @Override
    public DurationResponseQuestionDto getResponseByQuestion(UUID formId, Long questionId, Map<String, String> extraParams, Pageable pageable) {
        var grouped = durationRepository.groupedByDuration(questionId, pageable);

        var d = new DurationResponseQuestionDto();

        var responses = grouped.stream().map(g -> {
            Integer hours = g.get("hours", Integer.class);
            Integer minutes = g.get("minutes", Integer.class);
            Integer seconds = g.get("seconds", Integer.class);

            var res = new DurationResponseQuestionDto.Response();

            res.setHours(hours);
            res.setMinutes(minutes);
            res.setSeconds(seconds);
            res.setResponseCount(g.get("responseCount", Long.class));

            var map = new HashMap<String, List<String>>();

            map.put("hours", List.of(res.getHours() == null ? "" : res.getHours().toString()));
            map.put("minutes", List.of(res.getMinutes() == null ? "" : res.getMinutes().toString()));
            map.put("seconds", List.of(res.getSeconds() == null ? "" : res.getSeconds().toString()));

            res.setFormResponsesIdentifier(IdUtil.generateCompressedEncodedId(map));

            return res;

        }).toList();

        d.setResponses(responses);

        return d;
    }

    @Override
    public List<DurationResponseIndividualDto> getIndividualResponses(UUID formId, Long formResponseId) {
//        var responses = durationRepository.getDurationsByFormResponse(formResponseId);
//
//        return responses.stream().map(tuple -> {
//            var qId = tuple.get("questionId", Long.class);
//            var hours = tuple.get("hours", Integer.class);
//            var minutes = tuple.get("minutes", Integer.class);
//            var seconds = tuple.get("seconds", Integer.class);
//
//            var res = new DurationResponseIndividualDto();
//
//            res.setQuestionId(qId);
//            res.setQuestionType(getQuestionType());
//            res.setHours(hours);
//            res.setMinutes(minutes);
//            res.setSeconds(seconds);
//
//            return res;
//        }).toList();

        return null;
    }

    @Override
    public List<Tuple> getFormResponseAndUserIds(UUID formId, Long questionId, String formResponsesIdentifier, Pageable pageable) {
//        var map = IdUtil.reconstructCompressedEncodedId(formResponsesIdentifier);
//
//        var hours = map.get("hours");
//        var minutes = map.get("minutes");
//        var seconds = map.get("seconds");
//
//        if (hours.isEmpty() || minutes.isEmpty() || seconds.isEmpty()) {
//            throw new IllegalArgumentException("Invalid Form Responses Identifier. Identifier: " + formResponsesIdentifier);
//        }
//
//        var h = hours.getFirst() == null ? null : Integer.parseInt(hours.getFirst());
//        var m = minutes.getFirst() == null ? null : Integer.parseInt(minutes.getFirst());
//        var s = seconds.getFirst() == null ? null : Integer.parseInt(seconds.getFirst());
//
//        return durationRepository.getResponseIdsByGroupedResponse(formId, questionId, h, m, s, pageable);

        return null;
    }

    @Override
    public DurationResponseBatch mapToBatchResponse(List<FormResponseInfoQuestionResponse<DurationResponsePutReqDto>> questionResponsePutReqs) {
        var batch = new DurationResponseBatch();

        var responseMap = new HashMap<String, ArrayList<UUID>>();

        questionResponsePutReqs.forEach(q -> {
                    var durationString = String.format(
                            "%d_%d_%d",
                            q.getQuestionResponsePutReq().getHours(),
                            q.getQuestionResponsePutReq().getMinutes(),
                            q.getQuestionResponsePutReq().getSeconds()
                    );
                    responseMap
                            .computeIfAbsent(durationString, _ -> new ArrayList<>())
                            .add(q.getFormResponseId());
                }
        );

        var responses = responseMap.entrySet()
                .stream()
                .map(entry -> {
                    var res = new DurationResponseBatch.Response();

                    var durationString = entry.getKey();
                    var durationArray = durationString.split("_");

                    res.setHours(Integer.parseInt(durationArray[0]));
                    res.setMinutes(Integer.parseInt(durationArray[1]));
                    res.setSeconds(Integer.parseInt(durationArray[2]));
                    res.setFormResponseIds(entry.getValue());
                    res.setResponseCount((long) entry.getValue().size());

                    return res;
                }).toList();

        batch.setResponses(responses);

        return batch;
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.DURATION;
    }

}
