package com.sougata.form_response_service.service.responseManager;

import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.formResponse.individual.MultipleChoiceResponseIndividualDto;
import com.sougata.form_engine.dto.formResponse.question.MultipleChoiceResponseQuestionDto;
import com.sougata.form_engine.dto.formResponse.summary.MultipleChoiceResponseSummaryDto;
import com.sougata.form_engine.dto.question.details.MultipleChoiceDetailsDto;
import com.sougata.form_engine.util.IdUtil;
import com.sougata.form_response_service.repository.MultipleChoiceRepository;
import jakarta.persistence.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service("MULTIPLE_CHOICE_RESPONSE_MANAGER")
public class MultipleChoiceManager extends ResponseManager<
        MultipleChoiceResponseSummaryDto,
        MultipleChoiceDetailsDto,
        MultipleChoiceResponseQuestionDto,
        MultipleChoiceResponseQuestionDto.Response,
        MultipleChoiceResponseIndividualDto
        > {

    private final MultipleChoiceRepository multipleChoiceRepository;

    @Autowired
    public MultipleChoiceManager(MultipleChoiceRepository multipleChoiceRepository) {
        this.multipleChoiceRepository = multipleChoiceRepository;
    }

    @Override
    public List<MultipleChoiceResponseSummaryDto> getResponseSummaries(UUID formId, List<MultipleChoiceDetailsDto> questionResponses) {
        var responseSummaries = multipleChoiceRepository.getResponseSummaries(formId);
        var result = new ArrayList<MultipleChoiceResponseSummaryDto>();

        var responseOptionCountMap = multipleChoiceRepository.getResponseOptionCount(formId)
                .stream().collect(Collectors.groupingBy(e -> e.get("questionId", Long.class)));

        questionResponses.forEach(qr ->
                result.add(
                        responseSummaries.stream()
                                .filter(rs -> Objects.equals(rs.questionId(), qr.getId()))
                                .map(rs -> {
                                    var dd = new MultipleChoiceResponseSummaryDto();

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
                                            new MultipleChoiceResponseSummaryDto.Response(
                                                    op.getId(),
                                                    op.getOption(),
                                                    countMap.getOrDefault(op.getId(), 0L)

                                            )).toList();

                                    dd.setResponses(responses);

                                    return dd;
                                })
                                .findFirst()
                                .orElseGet(() -> {
                                    var dd = new MultipleChoiceResponseSummaryDto();

                                    dd.setQuestionId(qr.getId());
                                    dd.setQuestion(qr.getQuestion());
                                    dd.setOrderIndex(qr.getOrderIndex());
                                    dd.setNumberOfResponses(0L);
                                    dd.setQuestionType(QuestionType.MULTIPLE_CHOICE);
                                    dd.setResponses(List.of());

                                    return dd;
                                })
                )
        );

        return result;
    }

    @Override
    public MultipleChoiceResponseSummaryDto getResponseSummary(UUID formId, Long questionId, MultipleChoiceDetailsDto questionRes, Pageable pageable) {
        var responseSummary = multipleChoiceRepository.getResponseSummary(formId, questionId);
        var res = new MultipleChoiceResponseSummaryDto();

        res.setQuestionId(questionRes.getId());
        res.setQuestion(questionRes.getQuestion());
        res.setQuestionType(getQuestionType());
        res.setOrderIndex(questionRes.getOrderIndex());
        res.setNumberOfResponses(responseSummary.numberOfResponses());
        res.setResponses(List.of());

        return res;
    }

    @Override
    public MultipleChoiceResponseQuestionDto getResponseByQuestion(UUID formId, Long questionId, Map<String, String> extraParams, Pageable pageable) {
        var grouped = multipleChoiceRepository.groupedByResponseOption(questionId, pageable);

        var mc = new MultipleChoiceResponseQuestionDto();

        var responses = grouped.stream().map(g -> {
            var res = new MultipleChoiceResponseQuestionDto.Response();

            res.setQuestionId(questionId);
            res.setQuestionType(getQuestionType());
            res.setOptionId(g.get("optionId", Long.class));
            res.setResponseCount(g.get("responseCount", Long.class));

            var map = new HashMap<String, List<String>>();

            map.put("optionId", List.of(res.getOptionId() == null ? "" : res.getOptionId().toString()));

            res.setFormResponsesIdentifier(IdUtil.generateCompressedEncodedId(map));

            return res;
        }).toList();


        mc.setQuestionId(questionId);
        mc.setQuestionType(getQuestionType());
        mc.setResponses(responses);

        return mc;
    }

    @Override
    public List<MultipleChoiceResponseIndividualDto> getIndividualResponses(UUID formId, Long formResponseId) {
        var responses = multipleChoiceRepository.getOptionIdsByFormResponse(formResponseId);

        return responses.stream().map(tuple -> {
            var qId = tuple.get("questionId", Long.class);
            var optionId = tuple.get("optionId", Long.class);

            var res = new MultipleChoiceResponseIndividualDto();

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

        return multipleChoiceRepository.getResponseIdsByGroupedResponse(questionId, groupedResponse, pageable);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.MULTIPLE_CHOICE;
    }

}
