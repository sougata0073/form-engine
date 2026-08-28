package com.sougata.form_engine.dto.others;

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
