package com.sougata.form_service.dto.question.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class FileUploadDetailsDto extends QuestionDetails {
    private List<FileTypeDetails> allowedFileTypes;
    private Integer maxFileSize;
}

