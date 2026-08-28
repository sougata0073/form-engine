package com.sougata.form_engine.dto.question.responseRequest;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FileUploadResponsePutReqDto extends QuestionResponsePutReqDto {

    private String fileName;

    @NotNull
    private String fileUrl;

    @NotNull
    private String fileMimeType;

    @NotNull
    @Min(value = 1)
    @Max(value = 1_0485_7600)
    private Integer fileSize;

}
