package com.sougata.form_data_service.service.responseManager;

import com.sougata.form_data_service.model.AnyTypeQuestionResponse;
import com.sougata.form_data_service.model.FileUpload;
import com.sougata.form_data_service.model.FormResponse;
import com.sougata.form_data_service.repository.FileUploadRepository;
import com.sougata.form_data_service.repository.QuestionResponseRepository;
import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.question.responseputrequest.FileUploadResponsePutReqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service("FILE_UPLOAD_RESPONSE_MANAGER")
public class FileUploadManager extends ResponseManager<FileUploadResponsePutReqDto> {

    private final FileUploadRepository fileUploadRepository;

    @Autowired
    public FileUploadManager(FileUploadRepository fileUploadRepository, QuestionResponseRepository questionResponseRepository) {
        super(questionResponseRepository);
        this.fileUploadRepository = fileUploadRepository;
    }

    @Override
    @Async
    public CompletableFuture<Void> create(FileUploadResponsePutReqDto response, FormResponse formResponse) {
        FileUpload fileUpload = new FileUpload();

        var qr = createQuestionResponse(response.getQuestionId(), formResponse);

        fileUpload.setKey(new AnyTypeQuestionResponse.PartitionKey(response.getQuestionId(), qr.getKey().getQuestionResponseId()));
        fileUpload.setFileName(response.getFileName());
        fileUpload.setFileUrl(response.getFileUrl());
        fileUpload.setFileMimeType(response.getFileMimeType());
        fileUpload.setFileSize(response.getFileSize());

        fileUploadRepository.save(fileUpload);

        return CompletableFuture.completedFuture(null);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.FILE_UPLOAD;
    }

    @Override
    public void deleteResponsesByQuestionId(Long questionId) {
        fileUploadRepository.deleteAllByQuestionId(questionId);
    }

    @Override
    public void deleteResponsesByQuestionIdAndQuestionResponseId(Long questionId, Long questionResponseId) {
        fileUploadRepository.deleteAllByQuestionIdAndQuestionResponseId(questionId, questionResponseId);
    }

}
