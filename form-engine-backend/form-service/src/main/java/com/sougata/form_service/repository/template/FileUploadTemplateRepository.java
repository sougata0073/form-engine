package com.sougata.form_service.repository.template;

import com.sougata.form_service.model.template.FileUploadTemplate;
import org.springframework.stereotype.Repository;

@Repository("FILE_UPLOAD_TEMPLATE_REPOSITORY")
public interface FileUploadTemplateRepository extends AnyTypeQuestionTemplateRepository<FileUploadTemplate, Long> {
}