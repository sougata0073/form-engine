package com.sougata.form_data_service.formValidation.service.questionSchemaManager;

import com.sougata.form_data_service.formValidation.exception.InvalidFileSizeException;
import com.sougata.form_data_service.formValidation.exception.InvalidFileTypeException;
import com.sougata.form_data_service.formValidation.service.QuestionSchemaManager;
import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.others.FileTypeDetails;
import com.sougata.form_engine.dto.question.details.FileUploadDetailsDto;
import com.sougata.form_engine.dto.question.responseputrequest.FileUploadResponsePutReqDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("FILE_UPLOAD_QUESTION_SCHEMA_MANAGER")
public class FileUploadSchemaManager extends QuestionSchemaManager<FileUploadDetailsDto, FileUploadResponsePutReqDto> {

    @Override
    public boolean validateResponse(FileUploadResponsePutReqDto validationDto, FileUploadDetailsDto fu) {
        if (validationDto.getFileSize() > fu.getMaxFileSize()) {
            throw new InvalidFileSizeException(validationDto.getFileSize(), fu.getMaxFileSize());
        }

        if (!fu.getAllowedFileTypes().isEmpty()) {
            List<String> mimeTypes = fu.getAllowedFileTypes()
                    .stream()
                    .map(FileTypeDetails::getMimeTypes)
                    .flatMap(List::stream)
                    .toList();

            if (!mimeTypes.contains(validationDto.getFileMimeType())) {
                throw new InvalidFileTypeException(validationDto.getFileMimeType(), fu.getAllowedFileTypes().stream().map(FileTypeDetails::getCategory).toList());
            }
        }

        return true;
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.FILE_UPLOAD;
    }

}
