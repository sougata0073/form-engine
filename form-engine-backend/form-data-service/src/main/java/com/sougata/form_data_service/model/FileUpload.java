package com.sougata.form_data_service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("file_uploads")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FileUpload extends AnyTypeQuestionResponse {

    @Column("file_name")
    private String fileName;

    @Column("file_url")
    private String fileUrl;

    @Column("file_mime_type")
    private String fileMimeType;

    @Column("file_size")
    private Integer fileSize;

}
