package com.sougata.form_response_service.service.responseManager;

import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.formResponse.individual.RatingResponseIndividualDto;
import com.sougata.form_engine.dto.formResponse.question.RatingResponseQuestionDto;
import com.sougata.form_engine.dto.formResponse.summary.RatingResponseSummaryDto;
import com.sougata.form_engine.dto.pgfunctionparameter.ResponseIncrementOrCreate;
import com.sougata.form_engine.dto.question.details.RatingDetailsDto;
import com.sougata.form_engine.dto.question.responseputreqbatch.ParagraphResponseBatch;
import com.sougata.form_engine.dto.question.responseputreqbatch.QuestionResponseManagerBatchInput;
import com.sougata.form_engine.dto.question.responseputreqbatch.RatingResponseBatch;
import com.sougata.form_engine.dto.question.responseputrequest.RatingResponsePutReqDto;
import com.sougata.form_engine.util.JsonUtil;
import com.sougata.form_response_service.repository.RatingResponseRepository;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("RATING_RESPONSE_MANAGER")
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

    public RatingResponseManager(RatingResponseRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    @Override
    public void onResponseSave(UUID formId, UUID formResponseId, List<RatingResponsePutReqDto> questionResponsePutRequests) {
        var responseData = new ResponseIncrementOrCreate<>(questionResponsePutRequests);
        var responseDataJson = JsonUtil.toJson(responseData);

        ratingRepository.createOrIncrement(
                responseDataJson, formResponseId, 1L
        );
    }

    @Override
    public List<RatingResponseSummaryDto> getResponseSummaries(UUID formId, List<RatingDetailsDto> questionResponses) {
//        var responseSummaries = ratingRepository.getResponseSummaries(formId);
//        var result = new ArrayList<RatingResponseSummaryDto>();
//
//        var responseOptionCountMap = ratingRepository.getResponseRatingCount(formId)
//                .stream().collect(Collectors.groupingBy(e -> e.get("questionId", Long.class)));
//
//        questionResponses.forEach(qr ->
//                result.add(
//                        responseSummaries.stream()
//                                .filter(rs -> Objects.equals(rs.questionId(), qr.getId()))
//                                .map(rs -> {
//                                    var r = new RatingResponseSummaryDto();
//
//                                    r.setQuestionId(qr.getId());
//                                    r.setQuestion(qr.getQuestion());
//                                    r.setOrderIndex(qr.getOrderIndex());
//                                    r.setNumberOfResponses(rs.numberOfResponses());
//                                    r.setQuestionType(getQuestionType());
//                                    r.setRatingIcon(qr.getRatingIcon());
//                                    r.setMaxRatingNumber(qr.getMaxRatingNumber());
//
//                                    var ratingSum = 0d;
//                                    var countMap = new HashMap<Integer, Long>();
//
//                                    for (var cm : responseOptionCountMap.get(qr.getId())) {
//                                        var rating = cm.get("rating", Integer.class);
//                                        ratingSum += cm.get("ratingSum", Long.class);
//                                        countMap.put(rating, cm.get("responseCount", Long.class));
//                                    }
//
//                                    r.setAverageRating(ratingSum / rs.numberOfResponses());
//
//                                    var ratings = IntStream.rangeClosed(1, qr.getMaxRatingNumber()).boxed();
//
//                                    var responses = ratings.map(rt ->
//                                            new RatingResponseSummaryDto.Response(
//                                                    rt,
//                                                    countMap.getOrDefault(rt, 0L)
//                                            )).toList();
//
//                                    r.setResponses(responses);
//
//                                    return r;
//                                })
//                                .findFirst()
//                                .orElseGet(() -> {
//                                    var r = new RatingResponseSummaryDto();
//
//                                    r.setQuestionId(qr.getId());
//                                    r.setQuestion(qr.getQuestion());
//                                    r.setOrderIndex(qr.getOrderIndex());
//                                    r.setNumberOfResponses(0L);
//                                    r.setQuestionType(QuestionType.RATING);
//                                    r.setRatingIcon(qr.getRatingIcon());
//                                    r.setMaxRatingNumber(qr.getMaxRatingNumber());
//                                    r.setAverageRating(0d);
//                                    r.setResponses(List.of());
//
//                                    return r;
//                                })
//                )
//        );
//
//        return result;

        return null;
    }

    @Override
    public RatingResponseSummaryDto getResponseSummary(Long questionId, RatingDetailsDto questionRes, Pageable pageable) {
//        var responseSummary = ratingRepository.getResponseSummary(formId, questionId);
//        var res = new RatingResponseSummaryDto();
//
//        res.setQuestionId(questionRes.getId());
//        res.setQuestion(questionRes.getQuestion());
//        res.setQuestionType(getQuestionType());
//        res.setOrderIndex(questionRes.getOrderIndex());
//        res.setNumberOfResponses(responseSummary.numberOfResponses());
//        res.setResponses(List.of());
//
//        return res;

        return null;
    }

    @Override
    public RatingResponseQuestionDto getResponseByQuestion(UUID formId, Long questionId, Map<String, String> extraParams, Pageable pageable) {
//        var grouped = ratingRepository.groupedByRating(formId, questionId, pageable);
//
//        var r = new RatingResponseQuestionDto();
//
//        var responses = grouped.stream().map(g -> {
//            var res = new RatingResponseQuestionDto.Response();
//
//            res.setQuestionId(questionId);
//            res.setQuestionType(getQuestionType());
//            res.setRating(g.get("rating", Integer.class));
//            res.setResponseCount(g.get("responseCount", Long.class));
//
//            var map = new HashMap<String, List<String>>();
//
//            map.put("rating", List.of(res.getRating() == null ? "" : res.getRating().toString()));
//
//            res.setFormResponsesIdentifier(IdUtil.generateCompressedEncodedId(map));
//
//            return res;
//        }).toList();
//
//        r.setQuestionId(questionId);
//        r.setQuestionType(getQuestionType());
//        r.setResponses(responses);
//
//        return r;

        return null;
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
    public RatingResponseBatch mapToBatchResponse(Long questionId, List<QuestionResponseManagerBatchInput<RatingResponsePutReqDto>> questionResponsePutReqs) {
        var batch = new RatingResponseBatch();

        var responseMap = new HashMap<Integer, ArrayList<UUID>>();

        questionResponsePutReqs.forEach(q ->
                responseMap
                        .computeIfAbsent(q.getQuestionResponsePutReq().getRating(), _ -> new ArrayList<>())
                        .add(q.getFormResponseId())
        );

        batch.setQuestionId(questionId);
        batch.setQuestionType(getQuestionType());
        batch.setResponseCount((long) questionResponsePutReqs.size());

        var responses = responseMap.entrySet()
                .stream()
                .map(entry -> {
                    var res = new RatingResponseBatch.Response();

                    res.setRating(entry.getKey());
                    res.setFormResponseIds(entry.getValue());

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
