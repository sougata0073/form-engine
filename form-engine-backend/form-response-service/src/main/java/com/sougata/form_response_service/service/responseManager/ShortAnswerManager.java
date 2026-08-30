package com.sougata.form_response_service.service.responseManager;

import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.formResponse.individual.ShortAnswerResponseIndividualDto;
import com.sougata.form_engine.dto.formResponse.question.ShortAnswerResponseQuestionDto;
import com.sougata.form_engine.dto.formResponse.summary.ShortAnswerResponseSummaryDto;
import com.sougata.form_engine.dto.question.details.ShortAnswerDetailsDto;
import com.sougata.form_engine.util.IdUtil;
import com.sougata.form_engine.util.StringUtil;
import com.sougata.form_response_service.repository.ShortAnswerRepository;
import jakarta.persistence.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("SHORT_ANSWER_RESPONSE_MANAGER")
public class ShortAnswerManager extends ResponseManager<
        ShortAnswerResponseSummaryDto,
        ShortAnswerDetailsDto,
        ShortAnswerResponseQuestionDto,
        ShortAnswerResponseQuestionDto.Response,
        ShortAnswerResponseIndividualDto
        > {

    private final ShortAnswerRepository shortAnswerRepository;

    @Autowired
    public ShortAnswerManager(ShortAnswerRepository shortAnswerRepository) {
        this.shortAnswerRepository = shortAnswerRepository;
    }

    @Override
    public List<ShortAnswerResponseSummaryDto> getResponseSummaries(UUID formId, List<ShortAnswerDetailsDto> questionResponses) {
        var responseSummaries = shortAnswerRepository.getResponseSummaries(formId);
        var result = new ArrayList<ShortAnswerResponseSummaryDto>();

        questionResponses.forEach(qr ->
                result.add(
                        responseSummaries.stream()
                                .filter(rs -> Objects.equals(rs.questionId(), qr.getId()))
                                .map(rs -> {
                                    var sa = new ShortAnswerResponseSummaryDto();

                                    sa.setQuestionId(qr.getId());
                                    sa.setQuestion(qr.getQuestion());
                                    sa.setOrderIndex(qr.getOrderIndex());
                                    sa.setNumberOfResponses(rs.numberOfResponses());
                                    sa.setQuestionType(getQuestionType());
                                    sa.setResponses(List.of());

                                    return sa;
                                })
                                .findFirst()
                                .orElseGet(() -> {
                                    var sa = new ShortAnswerResponseSummaryDto();

                                    sa.setQuestionId(qr.getId());
                                    sa.setQuestion(qr.getQuestion());
                                    sa.setOrderIndex(qr.getOrderIndex());
                                    sa.setNumberOfResponses(0L);
                                    sa.setQuestionType(getQuestionType());
                                    sa.setResponses(List.of());

                                    return sa;
                                })
                ));

        return result;
    }

    @Override
    public ShortAnswerResponseSummaryDto getResponseSummary(UUID formId, Long questionId, ShortAnswerDetailsDto questionRes, Pageable pageable) {
        var responseSummary = shortAnswerRepository.getResponseSummary(formId, questionId);
        var texts = shortAnswerRepository.getResponseTexts(questionId, pageable);

        var sa = new ShortAnswerResponseSummaryDto();

        sa.setQuestionId(questionRes.getId());
        sa.setQuestion(questionRes.getQuestion());
        sa.setOrderIndex(questionRes.getOrderIndex());
        sa.setNumberOfResponses(responseSummary.numberOfResponses());
        sa.setQuestionType(getQuestionType());
        sa.setResponses(texts);

        return sa;
    }

    @Override
    public ShortAnswerResponseQuestionDto getResponseByQuestion(UUID formId, Long questionId, Map<String, String> extraParams, Pageable pageable) {

        var grouped = shortAnswerRepository.groupedByText(formId, questionId, pageable);

        var sa = new ShortAnswerResponseQuestionDto();

        var responses = grouped.stream().map(g -> {
            var res = new ShortAnswerResponseQuestionDto.Response();

            res.setQuestionId(questionId);
            res.setQuestionType(getQuestionType());
            res.setText(g.get("text", String.class));
            res.setResponseCount(g.get("responseCount", Long.class));

            var map = new HashMap<String, List<String>>();

            map.put("text", List.of(StringUtil.emptyIfNull(res.getText())));

            res.setFormResponsesIdentifier(IdUtil.generateCompressedEncodedId(map));

            return res;
        }).toList();

        sa.setQuestionId(questionId);
        sa.setQuestionType(getQuestionType());
        sa.setResponses(responses);

        return sa;
    }

    @Override
    public List<ShortAnswerResponseIndividualDto> getIndividualResponses(UUID formId, Long formResponseId) {
        var responses = shortAnswerRepository.getTextsByFormResponse(formResponseId);

        return responses.stream().map(tuple -> {
            var qId = tuple.get("questionId", Long.class);
            var text = tuple.get("text", String.class);

            var res = new ShortAnswerResponseIndividualDto();

            res.setQuestionId(qId);
            res.setQuestionType(getQuestionType());
            res.setText(text);

            return res;
        }).toList();
    }

    @Override
    public List<Tuple> getFormResponseAndUserIds(UUID formId, Long questionId, String formResponsesIdentifier, Pageable pageable) {
        var map = IdUtil.reconstructCompressedEncodedId(formResponsesIdentifier);

        var text = map.get("text");

        if (text.isEmpty()) {
            throw new IllegalArgumentException("Invalid Form Responses Identifier. Identifier: " + formResponsesIdentifier);
        }

        var groupedResponse = text.getFirst();

        return shortAnswerRepository.getResponseIdsByGroupedResponse(formId, questionId, groupedResponse, pageable);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.SHORT_ANSWER;
    }

}
