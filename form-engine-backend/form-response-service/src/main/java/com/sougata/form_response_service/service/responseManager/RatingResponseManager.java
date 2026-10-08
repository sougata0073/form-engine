package com.sougata.form_response_service.service.responseManager;

import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.formResponse.individual.RatingResponseIndividualDto;
import com.sougata.form_engine.dto.formResponse.question.RatingResponseQuestionDto;
import com.sougata.form_engine.dto.formResponse.summary.RatingResponseSummaryDto;
import com.sougata.form_engine.dto.pgfunctionparameter.QuestionResponseBatches;
import com.sougata.form_engine.dto.question.details.RatingDetailsDto;
import com.sougata.form_engine.dto.question.responseputreqbatch.FormResponseInfoQuestionResponse;
import com.sougata.form_engine.dto.question.responseputreqbatch.RatingResponseBatch;
import com.sougata.form_engine.dto.question.responseputrequest.RatingResponsePutReqDto;
import com.sougata.form_engine.util.IdUtil;
import com.sougata.form_engine.util.JsonUtil;
import com.sougata.form_response_service.model.AnyTypeQuestionResponse;
import com.sougata.form_response_service.model.QuestionResponseSummary;
import com.sougata.form_response_service.model.RatingResponse;
import com.sougata.form_response_service.repository.QuestionResponseSummaryRepository;
import com.sougata.form_response_service.repository.RatingResponseRepository;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service("RATING_RESPONSE_MANAGER")
@RequiredArgsConstructor
public class RatingResponseManager extends ResponseManager<
        RatingDetailsDto,
        RatingResponsePutReqDto,
        RatingResponseSummaryDto,
        RatingResponseQuestionDto,
        RatingResponseQuestionDto.Response,
        RatingResponseIndividualDto,
        RatingResponseBatch,
        RatingResponseBatch.Response
        > {

    private final RatingResponseRepository ratingRepository;
    private final QuestionResponseSummaryRepository questionResponseSummaryRepository;

    @Override
    public void saveBatched(List<RatingResponseBatch> ratingResponseBatches) {
        var responseBatches = new QuestionResponseBatches<>(ratingResponseBatches);
        var responseBatchesJson = JsonUtil.toJson(responseBatches);

        ratingRepository.createOrIncrement(
                responseBatchesJson
        );

    }

    @Override
    public List<RatingResponseSummaryDto> getResponseSummaries(UUID formId, List<RatingDetailsDto> questionDetailsList) {
        var questionResponseSummaries = questionResponseSummaryRepository.findAllByFormId(formId);
        var ratingResponses = ratingRepository.findAllByFormId(formId, Pageable.unpaged());

        var questionResponseSummariesMapByQuestionId = questionResponseSummaries
                .stream()
                .collect(Collectors.toMap(QuestionResponseSummary::getQuestionId, Function.identity()));

        var ratingResponsesMapByQuestionId = ratingResponses
                .stream()
                .collect(Collectors.groupingBy(AnyTypeQuestionResponse::getQuestionId));

        return questionDetailsList.stream().map(qd -> {
            var questionResponseSummary = questionResponseSummariesMapByQuestionId.get(qd.getId());

            var rtSummary = new RatingResponseSummaryDto();

            rtSummary.setQuestionId(qd.getId());
            rtSummary.setQuestion(qd.getQuestion());
            rtSummary.setOrderIndex(qd.getOrderIndex());
            rtSummary.setQuestionType(qd.getQuestionType());
            rtSummary.setNumberOfResponses(
                    questionResponseSummary == null ? 0L : questionResponseSummary.getResponseCount()
            );
            rtSummary.setRatingIcon(qd.getRatingIcon());
            rtSummary.setMaxRatingNumber(qd.getMaxRatingNumber());

            var ratingResponsesForThisQuestion = ratingResponsesMapByQuestionId.get(qd.getId());

            Map<Integer, RatingResponse> ratingResponsesMapByRating = ratingResponsesForThisQuestion == null
                    ? Map.of()
                    : ratingResponsesForThisQuestion
                    .stream()
                    .collect(Collectors.toMap(RatingResponse::getRating, Function.identity()));

            double totalRatingSum = 0;
            double totalRatings = 0;

            var responses = new ArrayList<RatingResponseSummaryDto.Response>();

            for (int i = 1; i <= qd.getMaxRatingNumber(); i++) {
                var rtResponse = ratingResponsesMapByRating.get(i);

                responses.add(
                        new RatingResponseSummaryDto.Response(
                                i,
                                rtResponse == null ? 0L : rtResponse.getResponseCount()
                        )
                );

                totalRatingSum += i * (rtResponse == null ? 0 : rtResponse.getResponseCount());
                totalRatings += rtResponse == null ? 0 : rtResponse.getResponseCount();
            }

            rtSummary.setResponses(responses);
            rtSummary.setAverageRating(totalRatingSum / totalRatings);

            return rtSummary;
        }).toList();
    }

    @Override
    public RatingResponseSummaryDto getResponseSummary(Long questionId, RatingDetailsDto questionRes, Pageable pageable) {
        var questionResponseSummaryOptional = questionResponseSummaryRepository.findByQuestionId(questionId);
        var res = new RatingResponseSummaryDto();

        res.setNumberOfResponses(
                questionResponseSummaryOptional.isEmpty()
                        ? 0L : questionResponseSummaryOptional.get().getResponseCount()
        );
        res.setResponses(List.of());
        res.setRatingIcon(questionRes.getRatingIcon());
        res.setMaxRatingNumber(questionRes.getMaxRatingNumber());

        return res;
    }

    @Override
    public RatingResponseQuestionDto getResponseByQuestion(UUID formId, Long questionId, Map<String, String> extraParams, Pageable pageable) {
        var grouped = ratingRepository.groupedByRating(questionId, pageable);

        var r = new RatingResponseQuestionDto();

        var responses = grouped.stream().map(g -> {
            var res = new RatingResponseQuestionDto.Response();

            res.setRating(g.get("rating", Integer.class));
            res.setResponseCount(g.get("responseCount", Long.class));

            var map = new HashMap<String, List<String>>();

            map.put("rating", List.of(res.getRating() == null ? "" : res.getRating().toString()));

            res.setFormResponsesIdentifier(IdUtil.generateCompressedEncodedId(map));

            return res;
        }).toList();

        r.setResponses(responses);

        return r;
    }

    @Override
    public List<RatingResponseIndividualDto> getIndividualResponses(UUID formId, Long formResponseId) {
//        var responses = ratingRepository.getRatingsByFormResponse(formResponseId);
//
//        return responses.stream().map(tuple -> {
//            var qId = tuple.get("questionId", Long.class);
//            var rating = tuple.get("rating", Integer.class);
//
//            var res = new RatingResponseIndividualDto();
//
//            res.setQuestionId(qId);
//            res.setQuestionType(getQuestionType());
//            res.setRating(rating);
//
//            return res;
//        }).toList();

        return null;
    }

    @Override
    public List<Tuple> getFormResponseAndUserIds(UUID formId, Long questionId, String formResponsesIdentifier, Pageable pageable) {
//        var map = IdUtil.reconstructCompressedEncodedId(formResponsesIdentifier);
//
//        var rating = map.get("rating");
//
//        if (rating.isEmpty()) {
//            throw new IllegalArgumentException("Invalid Form Responses Identifier. Identifier: " + formResponsesIdentifier);
//        }
//
//        var groupedResponse = rating.getFirst() == null ? null : Integer.parseInt(rating.getFirst());
//
//        return ratingRepository.getResponseIdsByGroupedResponse(formId, questionId, groupedResponse, pageable);

        return null;
    }

    @Override
    public RatingResponseBatch mapToBatchResponse(List<FormResponseInfoQuestionResponse<RatingResponsePutReqDto>> questionResponsePutReqs) {
        var batch = new RatingResponseBatch();

        var responseMap = new HashMap<Integer, ArrayList<UUID>>();

        questionResponsePutReqs.forEach(q ->
                responseMap
                        .computeIfAbsent(q.getQuestionResponsePutReq().getRating(), _ -> new ArrayList<>())
                        .add(q.getFormResponseId())
        );

        var responses = responseMap.entrySet()
                .stream()
                .map(entry -> {
                    var res = new RatingResponseBatch.Response();

                    res.setRating(entry.getKey());
                    res.setFormResponseIds(entry.getValue());
                    res.setResponseCount((long) entry.getValue().size());

                    return res;
                }).toList();

        batch.setResponses(responses);

        return batch;
    }


    @Override
    public QuestionType getQuestionType() {
        return QuestionType.RATING;
    }

}
