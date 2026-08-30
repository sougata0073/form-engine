package com.sougata.form_response_service.service.responseManager;

import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.formResponse.individual.DropdownResponseIndividualDto;
import com.sougata.form_engine.dto.formResponse.question.DropdownResponseQuestionDto;
import com.sougata.form_engine.dto.formResponse.summary.DropdownResponseSummaryDto;
import com.sougata.form_engine.dto.question.details.DropdownDetailsDto;
import com.sougata.form_engine.util.IdUtil;
import com.sougata.form_response_service.repository.DropdownRepository;
import jakarta.persistence.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service("DROPDOWN_RESPONSE_MANAGER")
public class DropdownManager extends ResponseManager<
        DropdownResponseSummaryDto,
        DropdownDetailsDto,
        DropdownResponseQuestionDto,
        DropdownResponseQuestionDto.Response,
        DropdownResponseIndividualDto
        > {

    private final DropdownRepository dropdownRepository;

    @Autowired
    public DropdownManager(DropdownRepository dropdownRepository) {
        this.dropdownRepository = dropdownRepository;
    }

    @Override
    public List<DropdownResponseSummaryDto> getResponseSummaries(UUID formId, List<DropdownDetailsDto> questionResponses) {
        var responseSummaries = dropdownRepository.getResponseSummaries(formId);
        var result = new ArrayList<DropdownResponseSummaryDto>();

        var responseOptionCountMap = dropdownRepository.getResponseOptionCount(formId)
                .stream().collect(Collectors.groupingBy(e -> e.get("questionId", Long.class)));

        questionResponses.forEach(qr ->
                result.add(
                        responseSummaries.stream()
                                .filter(rs -> Objects.equals(rs.questionId(), qr.getId()))
                                .map(rs -> {
                                    var dd = new DropdownResponseSummaryDto();

                                    dd.setQuestionId(qr.getId());
                                    dd.setQuestion(qr.getQuestion());
                                    dd.setOrderIndex(qr.getOrderIndex());
                                    dd.setNumberOfResponses(rs.numberOfResponses());
                                    dd.setQuestionType(getQuestionType());

                                    var countMap = new HashMap<Long, Long>();

                                    responseOptionCountMap.get(qr.getId()).forEach(cm ->
                                            countMap.put(cm.get("responseOptionId", Long.class), cm.get("responseCount", Long.class))
                                    );

                                    var responses = qr.getOptions().stream().map(op ->
                                            new DropdownResponseSummaryDto.Response(
                                                    op.getId(),
                                                    op.getOption(),
                                                    countMap.getOrDefault(op.getId(), 0L)

                                            )).toList();

                                    dd.setResponses(responses);

                                    return dd;
                                })
                                .findFirst()
                                .orElseGet(() -> {
                                    var dd = new DropdownResponseSummaryDto();

                                    dd.setQuestionId(qr.getId());
                                    dd.setQuestion(qr.getQuestion());
                                    dd.setOrderIndex(qr.getOrderIndex());
                                    dd.setNumberOfResponses(0L);
                                    dd.setQuestionType(QuestionType.DROPDOWN);
                                    dd.setResponses(List.of());

                                    return dd;
                                })
                )
        );

        return result;
    }

    @Override
    public DropdownResponseSummaryDto getResponseSummary(UUID formId, Long questionId, DropdownDetailsDto questionRes, Pageable pageable) {
        var responseSummary = dropdownRepository.getResponseSummary(formId, questionId);
        var res = new DropdownResponseSummaryDto();

        res.setQuestionId(questionRes.getId());
        res.setQuestion(questionRes.getQuestion());
        res.setQuestionType(getQuestionType());
        res.setOrderIndex(questionRes.getOrderIndex());
        res.setNumberOfResponses(responseSummary.numberOfResponses());
        res.setResponses(List.of());

        return res;
    }

    @Override
    public DropdownResponseQuestionDto getResponseByQuestion(UUID formId, Long questionId, Map<String, String> extraParams, Pageable pageable) {
        var grouped = dropdownRepository.groupedByResponseOption(formId, questionId, pageable);

        var d = new DropdownResponseQuestionDto();

        var responses = grouped.stream().map(g -> {
            var res = new DropdownResponseQuestionDto.Response();

            res.setQuestionId(questionId);
            res.setQuestionType(getQuestionType());
            res.setOptionId(g.get("optionId", Long.class));
            res.setResponseCount(g.get("responseCount", Long.class));

            var map = new HashMap<String, List<String>>();

            map.put("optionId", List.of(res.getOptionId() == null ? "" : res.getOptionId().toString()));

            res.setFormResponsesIdentifier(IdUtil.generateCompressedEncodedId(map));

            return res;
        }).toList();

        d.setQuestionId(questionId);
        d.setQuestionType(getQuestionType());
        d.setResponses(responses);

        return d;
    }

    @Override
    public List<DropdownResponseIndividualDto> getIndividualResponses(UUID formId, Long formResponseId) {
        var responses = dropdownRepository.getOptionIdsByFormResponse(formResponseId);

        return responses.stream().map(tuple -> {
            var qId = tuple.get("questionId", Long.class);
            var optionId = tuple.get("optionId", Long.class);

            var res = new DropdownResponseIndividualDto();

            res.setQuestionId(qId);
            res.setQuestionType(getQuestionType());
            res.setOptionId(optionId);

            return res;
        }).toList();
    }

    @Override
    public List<Tuple> getFormResponseAndUserIds(UUID formId, Long questionId, String formResponsesIdentifier, Pageable pageable) {
        var map = IdUtil.reconstructCompressedEncodedId(formResponsesIdentifier);

        var optionId = map.get("optionId");

        if (optionId.isEmpty()) {
            throw new IllegalArgumentException("Invalid Form Responses Identifier. Identifier: " + formResponsesIdentifier);
        }

        var groupedResponse = optionId.getFirst() == null ? null : Long.parseLong(optionId.getFirst());

        return dropdownRepository.getResponseIdsByGroupedResponse(formId, questionId, groupedResponse, pageable);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.DROPDOWN;
    }

}
