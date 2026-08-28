package com.sougata.form_response_service.service.responseManager;

import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.formResponse.individual.RatingResponseIndividualDto;
import com.sougata.form_engine.dto.formResponse.question.RatingResponseQuestionDto;
import com.sougata.form_engine.dto.formResponse.summary.RatingResponseSummaryDto;
import com.sougata.form_engine.dto.question.details.RatingDetailsDto;
import com.sougata.form_engine.util.IdUtil;
import com.sougata.form_response_service.repository.RatingRepository;
import jakarta.persistence.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service("RATING_RESPONSE_MANAGER")
public class RatingManager extends ResponseManager<
        RatingResponseSummaryDto,
        RatingDetailsDto,
        RatingResponseQuestionDto,
        RatingResponseQuestionDto.Response,
        RatingResponseIndividualDto
        > {

    private final RatingRepository ratingRepository;

    @Autowired
    public RatingManager(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    @Override
    public List<RatingResponseSummaryDto> getResponseSummaries(UUID formId, List<RatingDetailsDto> questionResponses) {
        var responseSummaries = ratingRepository.getResponseSummaries(formId);
        var result = new ArrayList<RatingResponseSummaryDto>();

        var responseOptionCountMap = ratingRepository.getResponseRatingCount(formId)
                .stream().collect(Collectors.groupingBy(e -> e.get("questionId", Long.class)));

        questionResponses.forEach(qr ->
                result.add(
                        responseSummaries.stream()
                                .filter(rs -> Objects.equals(rs.questionId(), qr.getId()))
                                .map(rs -> {
                                    var r = new RatingResponseSummaryDto();

                                    r.setQuestionId(qr.getId());
                                    r.setQuestion(qr.getQuestion());
                                    r.setOrderIndex(qr.getOrderIndex());
                                    r.setNumberOfResponses(rs.numberOfResponses());
                                    r.setQuestionType(getQuestionType());
                                    r.setRatingIcon(qr.getRatingIcon());
                                    r.setMaxRatingNumber(qr.getMaxRatingNumber());

                                    var ratingSum = 0d;
                                    var countMap = new HashMap<Integer, Long>();

                                    for (var cm : responseOptionCountMap.get(qr.getId())) {
                                        var rating = cm.get("rating", Integer.class);
                                        ratingSum += cm.get("ratingSum", Long.class);
                                        countMap.put(rating, cm.get("responseCount", Long.class));
                                    }

                                    r.setAverageRating(ratingSum / rs.numberOfResponses());

                                    var ratings = IntStream.rangeClosed(1, qr.getMaxRatingNumber()).boxed();

                                    var responses = ratings.map(rt ->
                                            new RatingResponseSummaryDto.Response(
                                                    rt,
                                                    countMap.getOrDefault(rt, 0L)
                                            )).toList();

                                    r.setResponses(responses);

                                    return r;
                                })
                                .findFirst()
                                .orElseGet(() -> {
                                    var r = new RatingResponseSummaryDto();

                                    r.setQuestionId(qr.getId());
                                    r.setQuestion(qr.getQuestion());
                                    r.setOrderIndex(qr.getOrderIndex());
                                    r.setNumberOfResponses(0L);
                                    r.setQuestionType(QuestionType.RATING);
                                    r.setRatingIcon(qr.getRatingIcon());
                                    r.setMaxRatingNumber(qr.getMaxRatingNumber());
                                    r.setAverageRating(0d);
                                    r.setResponses(List.of());

                                    return r;
                                })
                )
        );

        return result;
    }

    @Override
    public RatingResponseSummaryDto getResponseSummary(UUID formId, Long questionId, RatingDetailsDto questionRes, Pageable pageable) {
        var responseSummary = ratingRepository.getResponseSummary(formId, questionId);
        var res = new RatingResponseSummaryDto();

        res.setQuestionId(questionRes.getId());
        res.setQuestion(questionRes.getQuestion());
        res.setQuestionType(getQuestionType());
        res.setOrderIndex(questionRes.getOrderIndex());
        res.setNumberOfResponses(responseSummary.numberOfResponses());
        res.setResponses(List.of());

        return res;
    }

    @Override
    public RatingResponseQuestionDto getResponseByQuestion(UUID formId, Long questionId, Map<String, String> extraParams, Pageable pageable) {
        var grouped = ratingRepository.groupedByRating(questionId, pageable);

        var r = new RatingResponseQuestionDto();

        var responses = grouped.stream().map(g -> {
            var res = new RatingResponseQuestionDto.Response();

            res.setQuestionId(questionId);
            res.setQuestionType(getQuestionType());
            res.setRating(g.get("rating", Integer.class));
            res.setResponseCount(g.get("responseCount", Long.class));

            var map = new HashMap<String, List<String>>();

            map.put("rating", List.of(res.getRating() == null ? "" : res.getRating().toString()));

            res.setFormResponsesIdentifier(IdUtil.generateCompressedEncodedId(map));

            return res;
        }).toList();

        r.setQuestionId(questionId);
        r.setQuestionType(getQuestionType());
        r.setResponses(responses);

        return r;
    }

    @Override
    public List<RatingResponseIndividualDto> getIndividualResponses(UUID formId, Long formResponseId) {
        var responses = ratingRepository.getRatingsByFormResponse(formResponseId);

        return responses.stream().map(tuple -> {
            var qId = tuple.get("questionId", Long.class);
            var rating = tuple.get("rating", Integer.class);

            var res = new RatingResponseIndividualDto();

            res.setQuestionId(qId);
            res.setQuestionType(getQuestionType());
            res.setRating(rating);

            return res;
        }).toList();
    }

    @Override
    public List<Tuple> getFormResponseAndUserIds(UUID formId, Long questionId, String formResponsesIdentifier, Pageable pageable) {
        var map = IdUtil.reconstructCompressedEncodedId(formResponsesIdentifier);

        var rating = map.get("rating");

        if (rating.isEmpty()) {
            throw new IllegalArgumentException("Invalid Form Responses Identifier. Identifier: " + formResponsesIdentifier);
        }

        var groupedResponse = rating.getFirst() == null ? null : Integer.parseInt(rating.getFirst());

        return ratingRepository.getResponseIdsByGroupedResponse(questionId, groupedResponse, pageable);
    }


    @Override
    public QuestionType getQuestionType() {
        return QuestionType.RATING;
    }

}
