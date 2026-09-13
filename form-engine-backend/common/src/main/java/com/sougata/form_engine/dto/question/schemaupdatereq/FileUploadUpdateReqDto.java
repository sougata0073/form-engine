package com.sougata.form_engine.dto.question.schemaupdatereq;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@FieldNameConstants
public class FileUploadUpdateReqDto extends QuestionUpdateReqDto {

    private Set<String> allowedFileCategories;

    @Min(value = 1)
    @Max(value = 1_0485_7600)
    private Integer maxFileSize;

}
