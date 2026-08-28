package com.sougata.form_engine.dto.question.details;

import com.sougata.form_engine.dto.others.FileTypeDetails;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class FileUploadDetailsDto extends QuestionDetailsDto {
    private List<FileTypeDetails> allowedFileTypes;
    private Integer maxFileSize;
}

