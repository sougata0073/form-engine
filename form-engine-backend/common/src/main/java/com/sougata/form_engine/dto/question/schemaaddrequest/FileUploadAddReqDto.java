package com.sougata.form_engine.dto.question.schemaaddrequest;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class FileUploadAddReqDto extends QuestionAddReqDto {

    @NotNull
    private List<String> allowedFileCategories;

    @NotNull
    @Min(value = 1)
    @Max(value = 1_0485_7600)
    private Integer maxFileSize;

}
