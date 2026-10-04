package com.sougata.form_data_service.service.responseManager;

import com.sougata.form_data_service.model.AnyTypeQuestionResponse;
import com.sougata.form_data_service.model.FileUpload;
import com.sougata.form_data_service.repository.FileUploadRepository;
import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.question.responseputrequest.FileUploadResponsePutReqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service("FILE_UPLOAD_RESPONSE_MANAGER")
public class FileUploadManager extends ResponseManager<FileUploadResponsePutReqDto> {

    private final FileUploadRepository fileUploadRepository;

    @Autowired
    public FileUploadManager(FileUploadRepository fileUploadRepository) {
        this.fileUploadRepository = fileUploadRepository;
    }

    @Override
    @Async
    public CompletableFuture<Void> create(FileUploadResponsePutReqDto response, UUID formResponseId) {
        FileUpload fileUpload = new FileUpload();

        var key = new AnyTypeQuestionResponse.PartitionKey();
        key.setQuestionId(response.getQuestionId());
        key.setFormResponseId(formResponseId);

        fileUpload.setKey(key);
        fileUpload.setFileName(response.getFileName());
        fileUpload.setFileUrl(response.getFileUrl());
        fileUpload.setFileMimeType(response.getFileMimeType());
        fileUpload.setFileSize(response.getFileSize());

        fileUploadRepository.save(fileUpload);

        return CompletableFuture.completedFuture(null);
    }

    @Override
    @Async
    public CompletableFuture<Void> deleteAllByQuestionIdsAndFormResponseId(List<Long> questionIds, UUID formResponseId) {
        fileUploadRepository.deleteAllByQuestionIdsAndFormResponseId(questionIds, formResponseId);

        return CompletableFuture.completedFuture(null);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.FILE_UPLOAD;
    }

}
