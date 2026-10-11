package com.sougata.form_engine.dto.question.details;

import com.sougata.form_engine.dto.others.FileTypeDetails;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@Setter
public class FileUploadDetailsDto extends QuestionDetailsDto {
    private List<FileTypeDetails> allowedFileTypes;
    private Integer maxFileSize;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        FileUploadDetailsDto that = (FileUploadDetailsDto) o;

        var thisFileTypeMapByCategory = allowedFileTypes.stream().collect(Collectors.toMap(FileTypeDetails::getCategory, Function.identity()));
        var thatFileTypeMapByCategory = that.allowedFileTypes.stream().collect(Collectors.toMap(FileTypeDetails::getCategory, Function.identity()));

        for (var entry : thisFileTypeMapByCategory.entrySet()) {
            if (!Objects.equals(entry.getValue(), thatFileTypeMapByCategory.get(entry.getKey()))) {
                return false;
            }
        }

        return super.equals(that) && Objects.equals(maxFileSize, that.maxFileSize);
    }
}

