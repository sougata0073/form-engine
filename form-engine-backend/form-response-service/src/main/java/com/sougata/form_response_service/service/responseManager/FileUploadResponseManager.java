package com.sougata.form_response_service.service.responseManager;

import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.formResponse.individual.FileUploadResponseIndividualDto;
import com.sougata.form_engine.dto.formResponse.question.FileUploadResponseQuestionDto;
import com.sougata.form_engine.dto.formResponse.summary.FileUploadResponseSummaryDto;
import com.sougata.form_engine.dto.pgfunctionparameter.QuestionResponseBatches;
import com.sougata.form_engine.dto.question.details.FileUploadDetailsDto;
import com.sougata.form_engine.dto.question.responseputreqbatch.FileUploadResponseBatch;
import com.sougata.form_engine.dto.question.responseputreqbatch.FormResponseInfoQuestionResponse;
import com.sougata.form_engine.dto.question.responseputrequest.FileUploadResponsePutReqDto;
import com.sougata.form_engine.util.IdUtil;
import com.sougata.form_engine.util.JsonUtil;
import com.sougata.form_response_service.model.QuestionResponseSummary;
import com.sougata.form_response_service.repository.FileUploadResponseRepository;
import com.sougata.form_response_service.repository.QuestionResponseSummaryRepository;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service("FILE_UPLOAD_RESPONSE_MANAGER")
@RequiredArgsConstructor
public class FileUploadResponseManager extends ResponseManager<
        FileUploadDetailsDto,
        FileUploadResponsePutReqDto,
        FileUploadResponseSummaryDto,
        FileUploadResponseQuestionDto,
        FileUploadResponseQuestionDto.Response,
        FileUploadResponseIndividualDto,
        FileUploadResponseBatch,
        FileUploadResponseBatch.Response
        > {

    private final FileUploadResponseRepository fileUploadRepository;
    private final QuestionResponseSummaryRepository questionResponseSummaryRepository;

    @Override
    public void saveBatched(List<FileUploadResponseBatch> fileUploadResponseBatches) {
        var responseBatches = new QuestionResponseBatches<>(fileUploadResponseBatches);
        var responseBatchesJson = JsonUtil.toJson(responseBatches);

        fileUploadRepository.createOrIncrement(
                responseBatchesJson
        );

    }

    @Override
    public List<FileUploadResponseSummaryDto> getResponseSummaries(UUID formId, List<FileUploadDetailsDto> questionDetailsList) {
        var questionResponseSummaries = questionResponseSummaryRepository.findAllByFormId(formId);

        var questionResponseSummariesMapByQuestionId = questionResponseSummaries
                .stream()
                .collect(Collectors.toMap(QuestionResponseSummary::getQuestionId, Function.identity()));

        return questionDetailsList.stream().map(qd -> {
            var questionResponseSummary = questionResponseSummariesMapByQuestionId.get(qd.getId());

            var fu = new FileUploadResponseSummaryDto();

            fu.setQuestionId(qd.getId());
            fu.setQuestion(qd.getQuestion());
            fu.setOrderIndex(qd.getOrderIndex());
            fu.setNumberOfResponses(
                    questionResponseSummary == null ? 0L : questionResponseSummary.getResponseCount()
            );
            fu.setQuestionType(qd.getQuestionType());
            fu.setResponses(List.of());

            return fu;

        }).toList();
    }

    @Override
    public FileUploadResponseSummaryDto getResponseSummary(Long questionId, FileUploadDetailsDto questionRes, Pageable pageable) {
        var questionResponseSummaryOptional = questionResponseSummaryRepository.findByQuestionId(questionId);
        var files = fileUploadRepository.getResponseFiles(questionId, pageable);

        var f = new FileUploadResponseSummaryDto();

        f.setNumberOfResponses(
                questionResponseSummaryOptional.isEmpty()
                        ? 0L : questionResponseSummaryOptional.get().getResponseCount()
        );
        f.setResponses(
                files.stream().map(tuple ->
                        new FileUploadResponseSummaryDto.Response(
                                tuple.get("fileName", String.class),
                                tuple.get("fileUrl", String.class),
                                tuple.get("fileMimeType", String.class)
                        )
                ).toList()
        );

        return f;
    }

    @Override
    public FileUploadResponseQuestionDto getResponseByQuestion(UUID formId, Long questionId, Map<String, String> extraParams, Pageable pageable) {
        var grouped = fileUploadRepository.groupedByFile(questionId, pageable);

        var fu = new FileUploadResponseQuestionDto();

        var responses = grouped.stream().map(g -> {
            var res = new FileUploadResponseQuestionDto.Response();

            res.setFileName(g.get("fileName", String.class));
            res.setFileUrl(g.get("fileUrl", String.class));
            res.setFileMimeType(g.get("fileMimeType", String.class));
            res.setResponseCount(g.get("responseCount", Long.class));

            var map = new HashMap<String, List<String>>();

            map.put("fileName", List.of(res.getFileName() == null ? "" : res.getFileName()));
            map.put("fileUrl", List.of(res.getFileUrl() == null ? "" : res.getFileUrl()));
            map.put("fileMimeType", List.of(res.getFileMimeType() == null ? "" : res.getFileMimeType()));

            res.setFormResponsesIdentifier(IdUtil.generateCompressedEncodedId(map));

            return res;
        }).toList();

        fu.setResponses(responses);

        return fu;
    }

    @Override
    public List<FileUploadResponseIndividualDto> getIndividualResponses(UUID formId, Long formResponseId) {
//        var responses = fileUploadRepository.getFileUploadsByFormResponse(formResponseId);
//
//        return responses.stream().map(tuple -> {
//            var qId = tuple.get("questionId", Long.class);
//            var fileName = tuple.get("fileName", String.class);
//            var fileUrl = tuple.get("fileUrl", String.class);
//            var fileMimeType = tuple.get("fileMimeType", String.class);
//
//            var res = new FileUploadResponseIndividualDto();
//
//            res.setQuestionId(qId);
//            res.setQuestionType(getQuestionType());
//            res.setFileName(fileName);
//            res.setFileUrl(fileUrl);
//            res.setFileMimeType(fileMimeType);
//
//            return res;
//        }).toList();

        return null;
    }

    @Override
    public List<Tuple> getFormResponseAndUserIds(UUID formId, Long questionId, String formResponsesIdentifier, Pageable pageable) {
//        var map = IdUtil.reconstructCompressedEncodedId(formResponsesIdentifier);
//
//        var fileName = map.get("fileName");
//        var fileUrl = map.get("fileUrl");
//        var fileMimeType = map.get("fileMimeType");
//
//        if (fileName.isEmpty() || fileUrl.isEmpty() || fileMimeType.isEmpty()) {
//            throw new IllegalArgumentException("Invalid Form Responses Identifier. Identifier: " + formResponsesIdentifier);
//        }
//
//        var fName = fileName.getFirst();
//        var fUrl = fileUrl.getFirst();
//        var fMimeType = fileMimeType.getFirst();
//
//        return fileUploadRepository.getResponseIdsByGroupedResponse(formId, questionId, fName, fUrl, fMimeType, pageable);

        return null;
    }

    @Override
    public FileUploadResponseBatch mapToBatchResponse(List<FormResponseInfoQuestionResponse<FileUploadResponsePutReqDto>> questionResponsePutReqs) {
        var batch = new FileUploadResponseBatch();

        var responseMap = new HashMap<String, ArrayList<UUID>>();

        questionResponsePutReqs.forEach(q -> {
                    var fileString = String.format(
                            "%s\u0000%s\u0000%s",
                            q.getQuestionResponsePutReq().getFileUrl(),
                            q.getQuestionResponsePutReq().getFileName(),
                            q.getQuestionResponsePutReq().getFileMimeType()
                    );
                    responseMap
                            .computeIfAbsent(fileString, _ -> new ArrayList<>())
                            .add(q.getFormResponseId());
                }
        );

        var responses = responseMap.entrySet()
                .stream()
                .map(entry -> {
                    var res = new FileUploadResponseBatch.Response();

                    var fileString = entry.getKey();
                    var fileArray = fileString.split("\u0000");

                    res.setFileUrl(fileArray[0]);
                    res.setFileName(fileArray[1]);
                    res.setFileMimeType(fileArray[2]);
                    res.setFormResponseIds(entry.getValue());
                    res.setResponseCount((long) entry.getValue().size());

                    return res;
                }).toList();

        batch.setResponses(responses);

        return batch;
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.FILE_UPLOAD;
    }

}
