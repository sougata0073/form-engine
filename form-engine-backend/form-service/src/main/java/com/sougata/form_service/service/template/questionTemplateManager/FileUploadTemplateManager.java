package com.sougata.form_service.service.template.questionTemplateManager;

import com.sougata.form_service.constant.QuestionType;
import com.sougata.form_service.dto.question.response.FileTypeDetails;
import com.sougata.form_service.dto.template.questionTemplate.FileUploadTemplateDetails;
import com.sougata.form_service.model.template.FileUploadTemplate;
import com.sougata.form_service.service.template.QuestionTemplateManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("FILE_UPLOAD_TEMPLATE_MANAGER")
public class FileUploadTemplateManager extends QuestionTemplateManager<FileUploadTemplate, FileUploadTemplateDetails> {

    @Override
    public FileUploadTemplateDetails toQuestionTemplateDetails(FileUploadTemplate template) {
        var fu = new FileUploadTemplateDetails();

        populateCommonFields(template, fu);

        fu.setMaxFileSize(template.getMaxFileSize());
        fu.setAllowedFileTypes(
                template.getAllowedFileTypeTemplates().stream()
                        .map(ft -> new FileTypeDetails(ft.getCategory(), List.of(ft.getMimeTypes())))
                        .toList()
        );

        return fu;
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.FILE_UPLOAD;
    }
}
