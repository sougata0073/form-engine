package com.sougata.form_response_service.service.responseManager;

import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.formResponse.individual.DateTimeResponseIndividualDto;
import com.sougata.form_engine.dto.formResponse.question.DateResponseQuestionDto;
import com.sougata.form_engine.dto.formResponse.question.DateTimeResponseQuestionDto;
import com.sougata.form_engine.dto.formResponse.summary.DateTimeResponseSummaryDto;
import com.sougata.form_engine.dto.pgfunctionparameter.QuestionResponseBatches;
import com.sougata.form_engine.dto.question.details.DateTimeDetailsDto;
import com.sougata.form_engine.dto.question.responseputreqbatch.DateTimeResponseBatch;
import com.sougata.form_engine.dto.question.responseputreqbatch.FormResponseInfoQuestionResponse;
import com.sougata.form_engine.dto.question.responseputrequest.DateTimeResponsePutReqDto;
import com.sougata.form_engine.util.IdUtil;
import com.sougata.form_engine.util.JsonUtil;
import com.sougata.form_response_service.model.QuestionResponseSummary;
import com.sougata.form_response_service.repository.DateTimeResponseRepository;
import com.sougata.form_response_service.repository.QuestionResponseSummaryRepository;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service("DATE_TIME_RESPONSE_MANAGER")
@RequiredArgsConstructor
public class DateTimeResponseManager extends ResponseManager<
        DateTimeDetailsDto,
        DateTimeResponsePutReqDto,
        DateTimeResponseSummaryDto,
        DateTimeResponseQuestionDto,
        DateTimeResponseQuestionDto.Response,
        DateTimeResponseIndividualDto,
        DateTimeResponseBatch,
        DateTimeResponseBatch.Response
        > {

    private final DateTimeResponseRepository dateTimeRepository;
    private final QuestionResponseSummaryRepository questionResponseSummaryRepository;

    @Override
    public void saveBatched(List<DateTimeResponseBatch> dateTimeResponseBatches) {
        var responseBatches = new QuestionResponseBatches<>(dateTimeResponseBatches);
        var responseBatchesJson = JsonUtil.toJson(responseBatches);

        dateTimeRepository.createOrIncrement(
                responseBatchesJson
        );

    }

    @Override
    public List<DateTimeResponseSummaryDto> getResponseSummaries(UUID formId, List<DateTimeDetailsDto> questionDetailsList) {
        var questionResponseSummaries = questionResponseSummaryRepository.findAllByFormId(formId);

        var questionResponseSummariesMapByQuestionId = questionResponseSummaries
                .stream()
                .collect(Collectors.toMap(QuestionResponseSummary::getQuestionId, Function.identity()));

        return questionDetailsList.stream().map(qd -> {
            var questionResponseSummary = questionResponseSummariesMapByQuestionId.get(qd.getId());

            var dt = new DateTimeResponseSummaryDto();

            dt.setQuestionId(qd.getId());
            dt.setQuestion(qd.getQuestion());
            dt.setOrderIndex(qd.getOrderIndex());
            dt.setNumberOfResponses(
                    questionResponseSummary == null ? 0L : questionResponseSummary.getResponseCount()
            );
            dt.setQuestionType(qd.getQuestionType());
            dt.setResponses(List.of());

            return dt;

        }).toList();
    }

    @Override
    public DateTimeResponseSummaryDto getResponseSummary(Long questionId, DateTimeDetailsDto questionRes, Pageable pageable) {
        var questionResponseSummaryOptional = questionResponseSummaryRepository.findByQuestionId(questionId);
        var dateTimes = dateTimeRepository.getResponseDateTimes(questionId, pageable);

        var dt = new DateTimeResponseSummaryDto();

        dt.setNumberOfResponses(
                questionResponseSummaryOptional.isEmpty()
                        ? 0L : questionResponseSummaryOptional.get().getResponseCount()
        );

        var responses = dateTimes.stream().map(tuple -> {
            var res = new DateTimeResponseSummaryDto.Response();

            res.setDate(tuple.get("date", LocalDate.class));
            res.setTime(tuple.get("time", Instant.class));
            res.setTimeCount(tuple.get("timeCount", Long.class));

            return res;

        }).toList();

        dt.setResponses(responses);

        return dt;
    }

    @Override
    public DateTimeResponseQuestionDto getResponseByQuestion(UUID formId, Long questionId, Map<String, String> extraParams, Pageable pageable) {
        var dateTimeResponseQuestion = new DateTimeResponseQuestionDto();

        var groupedByDateTimes = dateTimeRepository.groupedByDateTimes(questionId, pageable);

        var responses = groupedByDateTimes.stream().map(tuple -> {
            var res = new DateTimeResponseQuestionDto.Response();

            res.setResponseCount(tuple.get("responseCount", Long.class));
            res.setDateTime(tuple.get("dateTime", Instant.class));

            var formResponseIdentifierMap = new HashMap<String, List<String>>();
            formResponseIdentifierMap.put(
                    "dateTime",
                    List.of(res.getDateTime() == null ? "" : res.getDateTime().toString())
            );

            res.setFormResponsesIdentifier(IdUtil.generateCompressedEncodedId(formResponseIdentifierMap));

            return res;

        }).toList();

        dateTimeResponseQuestion.setResponses(responses);

        return dateTimeResponseQuestion;
    }

    @Override
    public List<DateTimeResponseIndividualDto> getIndividualResponses(UUID formId, Long formResponseId) {
//        var responses = dateTimeRepository.getDateTimesByFormResponse(formResponseId);
//
//        return responses.stream().map(tuple -> {
//            var qId = tuple.get("questionId", Long.class);
//            var dateTime = tuple.get("dateTime", Instant.class);
//
//            var res = new DateTimeResponseIndividualDto();
//
//            res.setQuestionId(qId);
//            res.setQuestionType(getQuestionType());
//            res.setDateTime(dateTime);
//
//            return res;
//        }).toList();

        return null;
    }

    @Override
    public List<Tuple> getFormResponseAndUserIds(UUID formId, Long questionId, String formResponsesIdentifier, Pageable pageable) {
//        var map = IdUtil.reconstructCompressedEncodedId(formResponsesIdentifier);
//
//        var dateTime = map.get("dateTime");
//
//        if (dateTime.isEmpty()) {
//            throw new IllegalArgumentException("Invalid Form Responses Identifier. Identifier: " + formResponsesIdentifier);
//        }
//
//        var groupedResponse = dateTime.getFirst() == null ? null :  Instant.parse(dateTime.getFirst());
//
//        return dateTimeRepository.getResponseIdsByGroupedResponse(formId, questionId, groupedResponse, pageable);

        return null;
    }

    @Override
    public DateTimeResponseBatch mapToBatchResponse(List<FormResponseInfoQuestionResponse<DateTimeResponsePutReqDto>> questionResponsePutReqs) {
        var batch = new DateTimeResponseBatch();

        var responseMap = new HashMap<Instant, ArrayList<UUID>>();

        questionResponsePutReqs.forEach(q ->
                responseMap
                        .computeIfAbsent(q.getQuestionResponsePutReq().getDateTime(), _ -> new ArrayList<>())
                        .add(q.getFormResponseId())
        );

        var responses = responseMap.entrySet()
                .stream()
                .map(entry -> {
                    var res = new DateTimeResponseBatch.Response();

                    res.setDateTime(entry.getKey());
                    res.setFormResponseIds(entry.getValue());
                    res.setResponseCount((long) entry.getValue().size());

                    return res;
                }).toList();

        batch.setResponses(responses);

        return batch;
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.DATE_TIME;
    }

}
