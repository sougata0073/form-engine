package com.sougata.form_response_service.service.responseManager;

import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.formResponse.individual.CheckboxResponseIndividualDto;
import com.sougata.form_engine.dto.formResponse.question.CheckboxResponseQuestionDto;
import com.sougata.form_engine.dto.formResponse.summary.CheckboxResponseSummaryDto;
import com.sougata.form_engine.dto.question.details.CheckboxDetailsDto;
import com.sougata.form_engine.dto.question.responseputrequest.CheckboxResponsePutReqDto;
import com.sougata.form_engine.dto.question.schemaupdatereq.CheckboxUpdateReqDto;
import com.sougata.form_response_service.model.CheckboxResponse;
import com.sougata.form_response_service.model.QuestionResponse;
import com.sougata.form_response_service.model.QuestionResponseSummary;
import com.sougata.form_response_service.repository.CheckboxResponseRepository;
import com.sougata.form_response_service.repository.FormResponseRepository;
import com.sougata.form_response_service.repository.QuestionResponseRepository;
import com.sougata.form_response_service.repository.QuestionResponseSummaryRepository;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service("CHECKBOX_RESPONSE_MANAGER")
public class CheckboxResponseManager extends ResponseManager<
        CheckboxDetailsDto,
        CheckboxResponsePutReqDto,
        CheckboxResponseSummaryDto,
        CheckboxResponseQuestionDto,
        CheckboxResponseQuestionDto.Response,
        CheckboxResponseIndividualDto
        > {

    private final QuestionResponseSummaryRepository questionResponseSummaryRepository;
    private final QuestionResponseRepository questionResponseRepository;
    private final CheckboxResponseRepository checkboxResponseRepository;
    private final FormResponseRepository formResponseRepository;

    public CheckboxResponseManager(FormResponseRepository formResponseRepository, QuestionResponseSummaryRepository questionResponseSummaryRepository, QuestionResponseRepository questionResponseRepository, CheckboxResponseRepository checkboxResponseRepository) {
        super(formResponseRepository, questionResponseSummaryRepository);
        this.formResponseRepository = formResponseRepository;
        this.questionResponseSummaryRepository = questionResponseSummaryRepository;
        this.questionResponseRepository = questionResponseRepository;
        this.checkboxResponseRepository = checkboxResponseRepository;
    }

    @Override
    @Transactional
    public void create(UUID formId, CheckboxDetailsDto checkboxDetailsDto) {

        var questionResponses = new ArrayList<QuestionResponse>();
        var checkboxResponses = new ArrayList<CheckboxResponse>();

        var qResSummary = createQuestionResponseSummary(formId, checkboxDetailsDto);

        for (int i = 0; i < checkboxDetailsDto.getOptions().size(); i++) {
            var qr = new QuestionResponse();

            qr.setQuestionResponseSummary(qResSummary);

            questionResponses.add(qr);
        }

        var savedQuestionResponses = questionResponseRepository.saveAll(questionResponses);

        for (int i = 0; i < savedQuestionResponses.size(); i++) {
            var cb = new CheckboxResponse();

            cb.setQuestionResponse(savedQuestionResponses.get(i));
            cb.setOptionId(checkboxDetailsDto.getOptions().get(i).getId());

            checkboxResponses.add(cb);
        }

        checkboxResponseRepository.saveAll(checkboxResponses);
    }

    @Override
    public void update(UUID formId, CheckboxDetailsDto checkboxDetailsDto, Set<String> updatedFields) {

        if (!updatedFields.contains(CheckboxUpdateReqDto.Fields.options)) {
            return;
        }

        var checkboxResponses = checkboxResponseRepository.findAllByQuestionId(checkboxDetailsDto.getId());

        var prevOptionIds = checkboxResponses.stream().map(CheckboxResponse::getOptionId).collect(Collectors.toSet());
        var currentOptionIds = checkboxDetailsDto.getOptions().stream().map(CheckboxDetailsDto.Option::getId).collect(Collectors.toSet());

        var optionsToAdd = new HashSet<>(currentOptionIds);
        optionsToAdd.removeAll(prevOptionIds);

        var optionsToDelete = new HashSet<>(prevOptionIds);
        optionsToDelete.removeAll(currentOptionIds);

        if (!optionsToAdd.isEmpty()) {
            // Option adding start
            var questionResponseSummary = questionResponseSummaryRepository
                    .findByQuestionId(checkboxDetailsDto.getId())
                    .orElseGet(() -> {
                        var formResponseSummary = formResponseRepository.findById(formId)
                                .orElseThrow(() -> new IllegalArgumentException("Form response summary not found for form ID: " + formId));

                        var newQuestionResponseSummary = new QuestionResponseSummary();

                        newQuestionResponseSummary.setResponseCount(0L);
                        newQuestionResponseSummary.setQuestionId(checkboxDetailsDto.getId());
                        newQuestionResponseSummary.setFormResponseSummary(formResponseSummary);

                        return questionResponseSummaryRepository.save(newQuestionResponseSummary);
                    });

            var questionResponsesToSave = new ArrayList<QuestionResponse>();

            for (int i = 0; i < optionsToAdd.size(); i++) {
                var questionResponse = new QuestionResponse();

                questionResponse.setQuestionResponseSummary(questionResponseSummary);

                questionResponsesToSave.add(questionResponse);
            }

            var savedQuestionResponses = questionResponseRepository.saveAll(questionResponsesToSave);
            var optionsToAddList = optionsToAdd.stream().toList();

            var checkboxResponsesToSave = new ArrayList<CheckboxResponse>();

            for (int i = 0; i < optionsToAdd.size(); i++) {
                var cbRes = new CheckboxResponse();

                cbRes.setOptionId(optionsToAddList.get(i));
                cbRes.setResponseCount(0L);
                cbRes.setQuestionResponse(savedQuestionResponses.get(i));

                checkboxResponsesToSave.add(cbRes);
            }

            var savedCheckboxResponses = checkboxResponseRepository.saveAll(checkboxResponsesToSave);
            // Option adding end
        }

        if (!optionsToDelete.isEmpty()) {
            // Option deleting start
            var questionResponseIdsToDelete = checkboxResponses
                    .stream()
                    .filter(cb -> optionsToDelete.contains(cb.getOptionId()))
                    .map(CheckboxResponse::getQuestionResponseId)
                    .toList();

            questionResponseRepository.deleteAllQuestionResponsesById(questionResponseIdsToDelete);
            // Option deleting end
        }
    }

    @Override
    public void update(UUID formId, CheckboxResponsePutReqDto checkboxResponsePutReqDto) {

    }

    @Override
    public List<CheckboxResponseSummaryDto> getResponseSummaries(UUID formId, List<CheckboxDetailsDto> questionDetailsList) {
        var questionResponseSummaries = questionResponseSummaryRepository.findAllByFormId(formId);
        var checkboxResponses = checkboxResponseRepository.findAllByFormId(formId);

        var questionResponseSummariesMapByQuestionId = questionResponseSummaries
                .stream()
                .collect(Collectors.toMap(QuestionResponseSummary::getQuestionId, Function.identity()));

        var checkboxResponsesMapByQuestionId = checkboxResponses
                .stream()
                .collect(Collectors.groupingBy(cr -> cr.getQuestionResponse().getQuestionResponseSummary().getQuestionId()));

        var result = new ArrayList<CheckboxResponseSummaryDto>();

        questionDetailsList.forEach(qd -> {

            var questionResponseSummary = questionResponseSummariesMapByQuestionId.get(qd.getId());

            if (questionResponseSummary == null) {
                throw new IllegalArgumentException("Question response summary not found for question ID: " + qd.getId());
            }

            var cbSummary = new CheckboxResponseSummaryDto();

            cbSummary.setQuestionId(qd.getId());
            cbSummary.setQuestion(qd.getQuestion());
            cbSummary.setOrderIndex(qd.getOrderIndex());
            cbSummary.setQuestionType(qd.getQuestionType());
            cbSummary.setNumberOfResponses(questionResponseSummary.getResponseCount());

            var checkboxResponsesForThisQuestion = checkboxResponsesMapByQuestionId.get(qd.getId());

            if (checkboxResponsesForThisQuestion == null) {
                throw new IllegalArgumentException("Checkbox responses does not exists for question ID: " + qd.getId());
            }

            var checkboxResponsesMapByOptionId = checkboxResponsesForThisQuestion
                    .stream()
                    .collect(Collectors.toMap(CheckboxResponse::getOptionId, Function.identity()));

            var responses = qd.getOptions().stream().map(option -> {

                var cbResponse = checkboxResponsesMapByOptionId.get(option.getId());

                if (cbResponse == null) {
                    throw new IllegalArgumentException("Checkbox response does not exists form option ID: " + option.getId());
                }

                return new CheckboxResponseSummaryDto.Response(
                        cbResponse.getOptionId(),
                        option.getOption(),
                        cbResponse.getResponseCount()
                );

            }).toList();

            cbSummary.setResponses(responses);

            result.add(cbSummary);
        });

        return result;
    }

    @Override
    public CheckboxResponseSummaryDto getResponseSummary(Long questionId, CheckboxDetailsDto questionDetails, Pageable pageable) {
        var questionResponse = questionResponseSummaryRepository.findByQuestionId(questionId)
                .orElseThrow(() -> new IllegalArgumentException("Question response summary not found for question ID: " + questionId));

        var cbSummary = new CheckboxResponseSummaryDto();

        cbSummary.setQuestionId(questionDetails.getId());
        cbSummary.setQuestion(questionDetails.getQuestion());
        cbSummary.setQuestionType(questionDetails.getQuestionType());
        cbSummary.setOrderIndex(questionDetails.getOrderIndex());
        cbSummary.setNumberOfResponses(questionResponse.getResponseCount());
        cbSummary.setResponses(List.of());

        return cbSummary;
    }

    @Override
    public CheckboxResponseQuestionDto getResponseByQuestion(UUID formId, Long questionId, Map<String, String> extraParams, Pageable pageable) {

//        var checkboxResponses = checkboxResponseRepository.findAllByQuestionId(questionId, pageable);
//
//        var crQuestion = new CheckboxResponseQuestionDto();
//
//        var responses = checkboxResponses.stream().map(cr -> {
//
//            var crQuestionResponse = new CheckboxResponseQuestionDto.Response();
//
//            crQuestionResponse.setQuestionId(questionId);
//            crQuestionResponse.setQuestionType(getQuestionType());
//
//            return crQuestionResponse;
//
//        }).toList();
//
//        crQuestion.setQuestionId(questionId);
//        crQuestion.setQuestionType(getQuestionType());
//        crQuestion.setResponses(responses);
//
//        var grouped = checkboxRepository.groupedByResponseOptions(formId, questionId, pageable);
//
//        var cb = new CheckboxResponseQuestionDto();
//
//        var responses = grouped.stream().map(g -> {
//            var res = new CheckboxResponseQuestionDto.Response();
//
//            res.setQuestionId(questionId);
//            res.setQuestionType(getQuestionType());
//            res.setResponseCount(g.get("responseCount", Long.class));
//
//            var opIdArray = g.get("optionIds", Long[].class);
//
//            res.setOptionIds(opIdArray == null ? null : Arrays.stream(opIdArray).map(Object::toString).toList());
//
//            var map = new HashMap<String, List<String>>();
//
//            map.put("optionIds", res.getOptionIds() == null ? List.of() : res.getOptionIds());
//
//            res.setFormResponsesIdentifier(IdUtil.generateCompressedEncodedId(map));
//
//            return res;
//        }).toList();
//
//        cb.setQuestionId(questionId);
//        cb.setQuestionType(getQuestionType());
//        cb.setResponses(responses);
//
//        return crQuestion;

        return null;
    }

    @Override
    public List<CheckboxResponseIndividualDto> getIndividualResponses(UUID formId, Long formResponseId) {
//        var responses = checkboxRepository.getOptionIdsByFormResponse(formResponseId);
//
//        return responses.stream().map(tuple -> {
//            var qId = tuple.get("questionId", Long.class);
//            var optionIds = Arrays.stream(tuple.get("optionIds", Long[].class)).map(Object::toString).toList();
//
//            var res = new CheckboxResponseIndividualDto();
//
//            res.setQuestionId(qId);
//            res.setQuestionType(getQuestionType());
//            res.setOptionIds(optionIds);
//
//            return res;
//        }).toList();

        return null;
    }

    @Override
    public List<Tuple> getFormResponseAndUserIds(UUID formId, Long questionId, String formResponsesIdentifier, Pageable pageable) {
//        var map = IdUtil.reconstructCompressedEncodedId(formResponsesIdentifier);
//
//        var optionIds = map.get("optionIds");
//
//        if (optionIds.isEmpty()) {
//            throw new IllegalArgumentException("Invalid Form Responses Identifier. Identifier: " + formResponsesIdentifier);
//        }
//
//        var firstOptionId = optionIds.getFirst();
//
//        var groupedResponse = firstOptionId == null ? new Long[]{null} : optionIds.stream().map(Long::parseLong).toArray(Long[]::new);
//
//        return checkboxRepository.getResponseIdsByGroupedResponse(formId, questionId, groupedResponse, pageable);

        return null;
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.CHECKBOX;
    }

}
