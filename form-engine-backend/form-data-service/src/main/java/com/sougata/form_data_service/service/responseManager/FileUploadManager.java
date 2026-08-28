package com.sougata.form_data_service.service.responseManager;

import com.sougata.form_data_service.constant.QuestionType;
import com.sougata.form_data_service.dto.question.request.FileUploadResponsePutReqDto;
import com.sougata.form_data_service.model.FileUpload;
import com.sougata.form_data_service.model.FormResponse;
import com.sougata.form_data_service.repository.FileUploadRepository;
import com.sougata.form_data_service.repository.QuestionResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service("FILE_UPLOAD_RESPONSE_MANAGER")
public class FileUploadManager extends ResponseManager<
        FileUploadResponsePutReqDto
        > {

    private final FileUploadRepository fileUploadRepository;

    @Autowired
    public FileUploadManager(FileUploadRepository fileUploadRepository, QuestionResponseRepository questionResponseRepository) {
        super(questionResponseRepository);
        this.fileUploadRepository = fileUploadRepository;
    }

    @Override
    @Transactional
    public void create(FileUploadResponsePutReqDto response, FormResponse formResponse) {
        FileUpload fileUpload = new FileUpload();

        var qr = createQuestionResponse(response.getQuestionId(), formResponse);

        fileUpload.setFileName(response.getFileName());
        fileUpload.setFileUrl(response.getFileUrl());
        fileUpload.setFileMimeType(response.getFileMimeType());
        fileUpload.setFileSize(response.getFileSize());
        fileUpload.setQuestionResponse(qr);

        fileUploadRepository.save(fileUpload);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.FILE_UPLOAD;
    }

    @Override
    public void deleteResponsesByQuestion(UUID formId, Long questionId) {
        fileUploadRepository.deleteAllByFormIdAndQuestionId(formId, questionId);
    }

    @Override
    public void deleteResponsesByFormResponse(UUID formId, Long formResponseId) {
        fileUploadRepository.deleteAllByFormIdAndFormResponseId(formId, formResponseId);
    }
}
