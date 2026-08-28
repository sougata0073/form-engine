package com.sougata.form_response_service.service.responseManager;

import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.formResponse.individual.CheckboxResponseIndividualDto;
import com.sougata.form_engine.dto.formResponse.question.CheckboxResponseQuestionDto;
import com.sougata.form_engine.dto.formResponse.summary.CheckboxResponseSummaryDto;
import com.sougata.form_engine.dto.question.details.CheckboxDetailsDto;
import com.sougata.form_engine.util.IdUtil;
import com.sougata.form_response_service.repository.CheckboxRepository;
import jakarta.persistence.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service("CHECKBOX_RESPONSE_MANAGER")
public class CheckboxManager extends ResponseManager<
        CheckboxResponseSummaryDto,
        CheckboxDetailsDto,
        CheckboxResponseQuestionDto,
        CheckboxResponseQuestionDto.Response,
        CheckboxResponseIndividualDto
        > {

    private final CheckboxRepository checkboxRepository;

    @Autowired
    public CheckboxManager(CheckboxRepository checkboxRepository) {
        this.checkboxRepository = checkboxRepository;
    }

    @Override
    public List<CheckboxResponseSummaryDto> getResponseSummaries(UUID formId, List<CheckboxDetailsDto> questionResponses) {

        var responseSummaries = checkboxRepository.getResponseSummaries(formId);
        var result = new ArrayList<CheckboxResponseSummaryDto>();

        var responseOptionCountMap = checkboxRepository.getResponseOptionCount(formId)
                .stream().collect(Collectors.groupingBy(e -> e.get("questionId", Long.class)));

        questionResponses.forEach(qr ->
                result.add(
                        responseSummaries.stream()
                                .filter(rs -> Objects.equals(rs.questionId(), qr.getId()))
                                .map(rs -> {
                                    var cb = new CheckboxResponseSummaryDto();

                                    cb.setQuestionId(qr.getId());
                                    cb.setQuestion(qr.getQuestion());
                                    cb.setOrderIndex(qr.getOrderIndex());
                                    cb.setNumberOfResponses(rs.numberOfResponses());
                                    cb.setQuestionType(QuestionType.CHECKBOX);

                                    var countMap = new HashMap<Long, Long>();

                                    responseOptionCountMap.get(qr.getId()).forEach(cm ->
                                            countMap.put(cm.get("responseOptionId", Long.class), cm.get("responseCount", Long.class))
                                    );

                                    var responses = qr.getOptions().stream().map(op ->
                                            new CheckboxResponseSummaryDto.Response(
                                                    op.getId(),
                                                    op.getOption(),
                                                    countMap.getOrDefault(op.getId(), 0L)

                                            )).toList();

                                    cb.setResponses(responses);

                                    return cb;
                                })
                                .findFirst()
                                .orElseGet(() -> {
                                    var cb = new CheckboxResponseSummaryDto();

                                    cb.setQuestionId(qr.getId());
                                    cb.setQuestion(qr.getQuestion());
                                    cb.setOrderIndex(qr.getOrderIndex());
                                    cb.setNumberOfResponses(0L);
                                    cb.setQuestionType(QuestionType.CHECKBOX);
                                    cb.setResponses(List.of());

                                    return cb;
                                })
                )
        );

        return result;
    }

    @Override
    public CheckboxResponseSummaryDto getResponseSummary(UUID formId, Long questionId, CheckboxDetailsDto questionRes, Pageable pageable) {
        var responseSummary = checkboxRepository.getResponseSummary(formId, questionId);
        var res = new CheckboxResponseSummaryDto();

        res.setQuestionId(questionRes.getId());
        res.setQuestion(questionRes.getQuestion());
        res.setQuestionType(getQuestionType());
        res.setOrderIndex(questionRes.getOrderIndex());
        res.setNumberOfResponses(responseSummary.numberOfResponses());
        res.setResponses(List.of());

        return res;
    }

    @Override
    public CheckboxResponseQuestionDto getResponseByQuestion(UUID formId, Long questionId, Map<String, String> extraParams, Pageable pageable) {
        var grouped = checkboxRepository.groupedByResponseOptions(formId, questionId, pageable);

        var cb = new CheckboxResponseQuestionDto();

        var responses = grouped.stream().map(g -> {
            var res = new CheckboxResponseQuestionDto.Response();

            res.setQuestionId(questionId);
            res.setQuestionType(getQuestionType());
            res.setResponseCount(g.get("responseCount", Long.class));

            var opIdArray = g.get("optionIds", Long[].class);

            res.setOptionIds(opIdArray == null ? null : Arrays.stream(opIdArray).map(Object::toString).toList());

            var map = new HashMap<String, List<String>>();

            map.put("optionIds", res.getOptionIds() == null ? List.of() : res.getOptionIds());

            res.setFormResponsesIdentifier(IdUtil.generateCompressedEncodedId(map));

            return res;
        }).toList();

        cb.setQuestionId(questionId);
        cb.setQuestionType(getQuestionType());
        cb.setResponses(responses);

        return cb;
    }

    @Override
    public List<CheckboxResponseIndividualDto> getIndividualResponses(UUID formId, Long formResponseId) {
        var responses = checkboxRepository.getOptionIdsByFormResponse(formResponseId);

        return responses.stream().map(tuple -> {
            var qId = tuple.get("questionId", Long.class);
            var optionIds = Arrays.stream(tuple.get("optionIds", Long[].class)).map(Object::toString).toList();

            var res = new CheckboxResponseIndividualDto();

            res.setQuestionId(qId);
            res.setQuestionType(getQuestionType());
            res.setOptionIds(optionIds);

            return res;
        }).toList();
    }

    @Override
    public List<Tuple> getFormResponseAndUserIds(UUID formId, Long questionId, String formResponsesIdentifier, Pageable pageable) {
        var map = IdUtil.reconstructCompressedEncodedId(formResponsesIdentifier);

        var optionIds = map.get("optionIds");

        if (optionIds.isEmpty()) {
            throw new IllegalArgumentException("Invalid Form Responses Identifier. Identifier: " + formResponsesIdentifier);
        }

        var firstOptionId = optionIds.getFirst();

        var groupedResponse = firstOptionId == null ? new Long[]{null} : optionIds.stream().map(Long::parseLong).toArray(Long[]::new);

        return checkboxRepository.getResponseIdsByGroupedResponse(questionId, groupedResponse, pageable);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.CHECKBOX;
    }

}
