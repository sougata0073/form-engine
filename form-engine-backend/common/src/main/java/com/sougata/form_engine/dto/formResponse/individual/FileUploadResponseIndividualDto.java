package com.sougata.form_engine.dto.formResponse.individual;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadResponseIndividualDto extends ResponseIndividualDto {
    private String fileName;
    private String fileUrl;
    private String fileMimeType;
}
