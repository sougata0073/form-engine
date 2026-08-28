package com.sougata.form_response_service.service.responseManager;

import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.formResponse.individual.LinearScaleResponseIndividualDto;
import com.sougata.form_engine.dto.formResponse.question.LinearScaleResponseQuestionDto;
import com.sougata.form_engine.dto.formResponse.summary.LinearScaleResponseSummaryDto;
import com.sougata.form_engine.dto.question.details.LinearScaleDetailsDto;
import com.sougata.form_engine.util.IdUtil;
import com.sougata.form_response_service.repository.LinearScaleRepository;
import jakarta.persistence.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service("LINEAR_SCALE_RESPONSE_MANAGER")
public class LinearScaleManager extends ResponseManager<
        LinearScaleResponseSummaryDto,
        LinearScaleDetailsDto,
        LinearScaleResponseQuestionDto,
        LinearScaleResponseQuestionDto.Response,
        LinearScaleResponseIndividualDto
        > {

    private final LinearScaleRepository linearScaleRepository;

    @Autowired
    public LinearScaleManager(LinearScaleRepository linearScaleRepository) {
        this.linearScaleRepository = linearScaleRepository;
    }

    @Override
    public List<LinearScaleResponseSummaryDto> getResponseSummaries(UUID formId, List<LinearScaleDetailsDto> questionResponses) {
        var responseSummaries = linearScaleRepository.getResponseSummaries(formId);
        var result = new ArrayList<LinearScaleResponseSummaryDto>();

        var responseOptionCountMap = linearScaleRepository.getResponseScaleCount(formId)
                .stream().collect(Collectors.groupingBy(e -> e.get("questionId", Long.class)));

        questionResponses.forEach(qr ->
                result.add(
                        responseSummaries.stream()
                                .filter(rs -> Objects.equals(rs.questionId(), qr.getId()))
                                .map(rs -> {
                                    var ls = new LinearScaleResponseSummaryDto();

                                    ls.setQuestionId(qr.getId());
                                    ls.setQuestion(qr.getQuestion());
                                    ls.setOrderIndex(qr.getOrderIndex());
                                    ls.setNumberOfResponses(rs.numberOfResponses());
                                    ls.setQuestionType(QuestionType.LINEAR_SCALE);

                                    var countMap = new HashMap<Integer, Long>();

                                    responseOptionCountMap.get(qr.getId()).forEach(cm ->
                                            countMap.put(cm.get("scale", Integer.class), cm.get("responseCount", Long.class))
                                    );

                                    var scales = IntStream.rangeClosed(qr.getFromNumber(), qr.getToNumber()).boxed();

                                    var responses = scales.map(sc ->
                                            new LinearScaleResponseSummaryDto.Response(
                                                    sc,
                                                    countMap.getOrDefault(sc, 0L)

                                            )).toList();

                                    ls.setResponses(responses);

                                    return ls;
                                })
                                .findFirst()
                                .orElseGet(() -> {
                                    var ls = new LinearScaleResponseSummaryDto();

                                    ls.setQuestionId(qr.getId());
                                    ls.setQuestion(qr.getQuestion());
                                    ls.setOrderIndex(qr.getOrderIndex());
                                    ls.setNumberOfResponses(0L);
                                    ls.setQuestionType(QuestionType.LINEAR_SCALE);
                                    ls.setResponses(List.of());

                                    return ls;
                                })
                )
        );

        return result;
    }

    @Override
    public LinearScaleResponseSummaryDto getResponseSummary(UUID formId, Long questionId, LinearScaleDetailsDto questionRes, Pageable pageable) {
        var responseSummary = linearScaleRepository.getResponseSummary(formId, questionId);
        var res = new LinearScaleResponseSummaryDto();

        res.setQuestionId(questionRes.getId());
        res.setQuestion(questionRes.getQuestion());
        res.setQuestionType(getQuestionType());
        res.setOrderIndex(questionRes.getOrderIndex());
        res.setNumberOfResponses(responseSummary.numberOfResponses());
        res.setResponses(List.of());

        return res;
    }

    @Override
    public LinearScaleResponseQuestionDto getResponseByQuestion(UUID formId, Long questionId, Map<String, String> extraParams, Pageable pageable) {
        var grouped = linearScaleRepository.groupedByResponseScale(questionId, pageable);

        var ls = new LinearScaleResponseQuestionDto();

        var responses = grouped.stream().map(g -> {
            var res = new LinearScaleResponseQuestionDto.Response();

            res.setQuestionId(questionId);
            res.setQuestionType(getQuestionType());
            res.setScale(g.get("scale", Integer.class));
            res.setResponseCount(g.get("responseCount", Long.class));

            var map = new HashMap<String, List<String>>();

            map.put("scale", List.of(res.getScale() == null ? "" : res.getScale().toString()));

            res.setFormResponsesIdentifier(IdUtil.generateCompressedEncodedId(map));

            return res;
        }).toList();


        ls.setQuestionId(questionId);
        ls.setQuestionType(getQuestionType());
        ls.setResponses(responses);

        return ls;
    }

    @Override
    public List<LinearScaleResponseIndividualDto> getIndividualResponses(UUID formId, Long formResponseId) {
        var responses = linearScaleRepository.getScalesByFormResponse(formResponseId);

        return responses.stream().map(tuple -> {
            var qId = tuple.get("questionId", Long.class);
            var scale = tuple.get("scale", Integer.class);

            var res = new LinearScaleResponseIndividualDto();

            res.setQuestionId(qId);
            res.setQuestionType(getQuestionType());
            res.setScale(scale);

            return res;
        }).toList();
    }

    @Override
    public List<Tuple> getFormResponseAndUserIds(UUID formId, Long questionId, String formResponsesIdentifier, Pageable pageable) {
        var map = IdUtil.reconstructCompressedEncodedId(formResponsesIdentifier);

        var scale = map.get("scale");

        if (scale.isEmpty()) {
            throw new IllegalArgumentException("Invalid Form Responses Identifier. Identifier: " + formResponsesIdentifier);
        }

        var groupedResponse = scale.getFirst() == null ? null : Integer.parseInt(scale.getFirst());

        return linearScaleRepository.getResponseIdsByGroupedResponse(questionId, groupedResponse, pageable);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.LINEAR_SCALE;
    }

}
