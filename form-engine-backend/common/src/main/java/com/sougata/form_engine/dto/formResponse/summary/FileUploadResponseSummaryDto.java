package com.sougata.form_engine.dto.formResponse.summary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class FileUploadResponseSummaryDto extends ResponseSummaryDto<FileUploadResponseSummaryDto.Response> {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private String fileName;
        private String fileUrl;
        private String fileMimeType;
    }

}
