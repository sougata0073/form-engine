package com.sougata.form_service.repository.formSchema;

import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_service.model.formSchema.FileUpload;
import org.springframework.stereotype.Repository;

@Repository("FILE_UPLOAD_REPOSITORY")
public interface FileUploadRepository extends AnyTypeQuestionRepository<FileUpload, Long> {

    @Override
    default QuestionType getQuestionType() {
        return QuestionType.FILE_UPLOAD;
    }

}

