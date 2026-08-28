package com.sougata.form_response_service.service.responseManager;

import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.formResponse.individual.TimeResponseIndividualDto;
import com.sougata.form_engine.dto.formResponse.question.TimeResponseQuestionDto;
import com.sougata.form_engine.dto.formResponse.summary.TimeResponseSummaryDto;
import com.sougata.form_engine.dto.question.details.TimeDetailsDto;
import com.sougata.form_engine.util.IdUtil;
import com.sougata.form_response_service.repository.TimeRepository;
import jakarta.persistence.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service("TIME_RESPONSE_MANAGER")
public class TimeManager extends ResponseManager<
        TimeResponseSummaryDto,
        TimeDetailsDto,
        TimeResponseQuestionDto,
        TimeResponseQuestionDto.Response,
        TimeResponseIndividualDto
        > {

    private final TimeRepository timeRepository;

    @Autowired
    public TimeManager(TimeRepository timeRepository) {
        this.timeRepository = timeRepository;
    }

    @Override
    public List<TimeResponseSummaryDto> getResponseSummaries(UUID formId, List<TimeDetailsDto> questionResponses) {
        var responseSummaries = timeRepository.getResponseSummaries(formId);
        var result = new ArrayList<TimeResponseSummaryDto>();

        questionResponses.forEach(qr ->
                result.add(
                        responseSummaries.stream()
                                .filter(rs -> Objects.equals(rs.questionId(), qr.getId()))
                                .map(rs -> {
                                    var t = new TimeResponseSummaryDto();

                                    t.setQuestionId(qr.getId());
                                    t.setQuestion(qr.getQuestion());
                                    t.setOrderIndex(qr.getOrderIndex());
                                    t.setNumberOfResponses(rs.numberOfResponses());
                                    t.setQuestionType(getQuestionType());
                                    t.setResponses(List.of());

                                    return t;
                                })
                                .findFirst()
                                .orElseGet(() -> {
                                    var t = new TimeResponseSummaryDto();

                                    t.setQuestionId(qr.getId());
                                    t.setQuestion(qr.getQuestion());
                                    t.setOrderIndex(qr.getOrderIndex());
                                    t.setNumberOfResponses(0L);
                                    t.setQuestionType(getQuestionType());
                                    t.setResponses(List.of());

                                    return t;
                                })
                ));

        return result;
    }

    @Override
    public TimeResponseSummaryDto getResponseSummary(UUID formId, Long questionId, TimeDetailsDto questionRes, Pageable pageable) {
        var responseSummary = timeRepository.getResponseSummary(formId, questionId);
        var timeResponses = timeRepository.getResponseTimes(questionId, pageable);

        var t = new TimeResponseSummaryDto();

        t.setQuestionId(questionRes.getId());
        t.setQuestion(questionRes.getQuestion());
        t.setOrderIndex(questionRes.getOrderIndex());
        t.setNumberOfResponses(responseSummary.numberOfResponses());
        t.setQuestionType(getQuestionType());

        var responses = timeResponses.stream()
                .map(tuple -> {
                    var res = new TimeResponseSummaryDto.Response();

                    var times = Arrays.asList(tuple.get("times", String[].class));
                    var timeCounts = Arrays.asList(tuple.get("timeCounts", Long[].class));

                    var timeCountPairs = new ArrayList<TimeResponseSummaryDto.TimeCountPair>();

                    for (int i = 0; i < times.size(); i++) {
                        timeCountPairs.add(
                                new TimeResponseSummaryDto.TimeCountPair(Instant.parse(times.get(i)), timeCounts.get(i))
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
        var grouped = timeRepository.groupedByTime(questionId, pageable);

        var t = new TimeResponseQuestionDto();

        var responses = grouped.stream().map(g -> {
            var res = new TimeResponseQuestionDto.Response();

            res.setQuestionId(questionId);
            res.setQuestionType(getQuestionType());
            res.setTime(g.get("time", Instant.class));
            res.setResponseCount(g.get("responseCount", Long.class));

            var map = new HashMap<String, List<String>>();

            map.put("time", List.of(res.getTime() == null ? "" : res.getTime().toString()));

            res.setFormResponsesIdentifier(IdUtil.generateCompressedEncodedId(map));

            return res;

        }).toList();

        t.setQuestionId(questionId);
        t.setQuestionType(getQuestionType());
        t.setResponses(responses);

        return t;
    }

    @Override
    public List<TimeResponseIndividualDto> getIndividualResponses(UUID formId, Long formResponseId) {
        var responses = timeRepository.getTimesByFormResponse(formResponseId);

        return responses.stream().map(tuple -> {
            var qId = tuple.get("questionId", Long.class);
            var time = tuple.get("time", Instant.class);

            var res = new TimeResponseIndividualDto();

            res.setQuestionId(qId);
            res.setQuestionType(getQuestionType());
            res.setTime(time);

            return res;
        }).toList();
    }

    @Override
    public List<Tuple> getFormResponseAndUserIds(UUID formId, Long questionId, String formResponsesIdentifier, Pageable pageable) {
        var map = IdUtil.reconstructCompressedEncodedId(formResponsesIdentifier);

        var time = map.get("time");

        if (time.isEmpty()) {
            throw new IllegalArgumentException("Invalid Form Responses Identifier. Identifier: " + formResponsesIdentifier);
        }

        var groupedResponse = time.getFirst() == null ? null : Instant.parse(time.getFirst());

        return timeRepository.getResponseIdsByGroupedResponse(questionId, groupedResponse, pageable);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.TIME;
    }

}
