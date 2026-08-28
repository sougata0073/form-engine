package com.sougata.form_engine.dto.template.questionTemplate;

import com.sougata.form_engine.dto.others.FileTypeDetails;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadTemplateDetails extends QuestionTemplateDetails {
    public Integer maxFileSize;
    public List<FileTypeDetails> allowedFileTypes;
}
