package com.sougata.form_data_service.repository;

import com.sougata.form_data_service.model.FileUpload;
import org.springframework.stereotype.Repository;

@Repository("FILE_UPLOAD_RESPONSE_REPOSITORY")
public interface FileUploadRepository extends AnyTypeQuestionResponseRepository<FileUpload, Long> {

}
