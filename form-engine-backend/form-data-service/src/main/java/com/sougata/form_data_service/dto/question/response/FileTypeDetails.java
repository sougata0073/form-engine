package com.sougata.form_data_service.dto.question.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileTypeDetails {
    private String category;
    private List<String> mimeTypes;
}