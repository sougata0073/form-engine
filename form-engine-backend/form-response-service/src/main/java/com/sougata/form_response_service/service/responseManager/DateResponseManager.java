package com.sougata.form_response_service.service.responseManager;

import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.formResponse.individual.DateResponseIndividualDto;
import com.sougata.form_engine.dto.formResponse.question.DateResponseQuestionDto;
import com.sougata.form_engine.dto.formResponse.summary.DateResponseSummaryDto;
import com.sougata.form_engine.dto.pgfunctionparameter.QuestionResponseBatches;
import com.sougata.form_engine.dto.question.details.DateDetailsDto;
import com.sougata.form_engine.dto.question.responseputreqbatch.DateResponseBatch;
import com.sougata.form_engine.dto.question.responseputreqbatch.FormResponseInfoQuestionResponse;
import com.sougata.form_engine.dto.question.responseputrequest.DateResponsePutReqDto;
import com.sougata.form_engine.util.IdUtil;
import com.sougata.form_engine.util.JsonUtil;
import com.sougata.form_response_service.model.QuestionResponseSummary;
import com.sougata.form_response_service.repository.DateResponseRepository;
import com.sougata.form_response_service.repository.QuestionResponseSummaryRepository;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service("DATE_RESPONSE_MANAGER")
@RequiredArgsConstructor
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
    private final QuestionResponseSummaryRepository questionResponseSummaryRepository;

    @Override
    public void saveBatched(List<DateResponseBatch> dateResponseBatches) {
        var responseBatches = new QuestionResponseBatches<>(dateResponseBatches);
        var responseBatchesJson = JsonUtil.toJson(responseBatches);

        dateRepository.createOrIncrement(
                responseBatchesJson
        );

    }

    @Override
    public List<DateResponseSummaryDto> getResponseSummaries(UUID formId, List<DateDetailsDto> questionDetailsList) {
        var questionResponseSummaries = questionResponseSummaryRepository.findAllByFormId(formId);

        var questionResponseSummariesMapByQuestionId = questionResponseSummaries
                .stream()
                .collect(Collectors.toMap(QuestionResponseSummary::getQuestionId, Function.identity()));

        return questionDetailsList.stream().map(qd -> {
            var questionResponseSummary = questionResponseSummariesMapByQuestionId.get(qd.getId());

            var d = new DateResponseSummaryDto();

            d.setQuestionId(qd.getId());
            d.setQuestion(qd.getQuestion());
            d.setOrderIndex(qd.getOrderIndex());
            d.setNumberOfResponses(
                    questionResponseSummary == null ? 0L : questionResponseSummary.getResponseCount()
            );
            d.setQuestionType(qd.getQuestionType());
            d.setResponses(List.of());

            return d;

        }).toList();
    }

    @Override
    public DateResponseSummaryDto getResponseSummary(Long questionId, DateDetailsDto questionRes, Pageable pageable) {
        var questionResponseSummaryOptional = questionResponseSummaryRepository.findByQuestionId(questionId);
        var groupedByYearMonth = dateRepository.groupedByYearMonth(questionId, pageable);

        var d = new DateResponseSummaryDto();

        d.setNumberOfResponses(
                questionResponseSummaryOptional.isEmpty()
                        ? 0L : questionResponseSummaryOptional.get().getResponseCount()
        );

        var responses = groupedByYearMonth.stream().map(tuple -> {
            var res = new DateResponseSummaryDto.Response();

            res.setYear(tuple.get("year", Integer.class));
            res.setMonth(tuple.get("month", Integer.class));

            var dates = tuple.get("dates", String[].class);
            var dateCounts = tuple.get("dateCounts", Long[].class);

            if (dates.length != dateCounts.length) {
                throw new RuntimeException("Date and date count array length mismatch. Date array length: " + dates.length + ". Date count array length: " + dateCounts.length);
            }

            var dateCountPairs = new ArrayList<DateResponseSummaryDto.DateCountPair>();

            for (int i = 0; i < dates.length; i++) {
                dateCountPairs.add(
                        new DateResponseSummaryDto.DateCountPair(
                                Instant.parse(dates[i]), dateCounts[i]
                        )
                );
            }

            res.setDates(dateCountPairs);

            return res;
        }).toList();

        d.setResponses(responses);

        return d;
    }

    @Override
    public DateResponseQuestionDto getResponseByQuestion(UUID formId, Long questionId, Map<String, String> extraParams, Pageable pageable) {
        var dateResponseQuestion = new DateResponseQuestionDto();

        var groupedByDates = dateRepository.groupedByDates(questionId, pageable);

        var responses = groupedByDates.stream().map(tuple -> {
            var res = new DateResponseQuestionDto.Response();

            res.setResponseCount(tuple.get("responseCount", Long.class));
            res.setDate(tuple.get("date", Instant.class));

            var formResponseIdentifierMap = new HashMap<String, List<String>>();
            formResponseIdentifierMap.put(
                    "date",
                    List.of(res.getDate() == null ? "" : res.getDate().toString())
            );

            res.setFormResponsesIdentifier(IdUtil.generateCompressedEncodedId(formResponseIdentifierMap));

            return res;

        }).toList();

        dateResponseQuestion.setResponses(responses);

        return dateResponseQuestion;
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
    public DateResponseBatch mapToBatchResponse(List<FormResponseInfoQuestionResponse<DateResponsePutReqDto>> questionResponsePutReqs) {
        var batch = new DateResponseBatch();

        var responseMap = new HashMap<Instant, ArrayList<UUID>>();

        questionResponsePutReqs.forEach(q ->
                responseMap
                        .computeIfAbsent(q.getQuestionResponsePutReq().getDate(), _ -> new ArrayList<>())
                        .add(q.getFormResponseId())
        );

        var responses = responseMap.entrySet()
                .stream()
                .map(entry -> {
                    var res = new DateResponseBatch.Response();

                    res.setDate(entry.getKey());
                    res.setFormResponseIds(entry.getValue());
                    res.setResponseCount((long) entry.getValue().size());

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
