package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.FileUploadResponse;
import org.springframework.stereotype.Repository;

@Repository("FILE_UPLOAD_RESPONSE_REPOSITORY")
public interface FileUploadResponseRepository extends AnyTypeQuestionResponseRepository<FileUploadResponse, Long> {
}
