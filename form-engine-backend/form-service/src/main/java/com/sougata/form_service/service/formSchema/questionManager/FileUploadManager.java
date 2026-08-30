package com.sougata.form_service.service.formSchema.questionManager;

import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.others.FileTypeDetails;
import com.sougata.form_engine.dto.question.details.FileUploadDetailsDto;
import com.sougata.form_engine.dto.question.schemaRequest.FileUploadPutReqDto;
import com.sougata.form_engine.dto.template.questionTemplate.FileUploadTemplateDetails;
import com.sougata.form_service.exception.FileTypeNotFoundException;
import com.sougata.form_service.exception.QuestionNotFoundException;
import com.sougata.form_service.model.FileType;
import com.sougata.form_service.model.formSchema.FileUpload;
import com.sougata.form_service.model.formSchema.Form;
import com.sougata.form_service.model.formSchema.Question;
import com.sougata.form_service.repository.FileTypeRepository;
import com.sougata.form_service.repository.formSchema.FileUploadRepository;
import com.sougata.form_service.repository.formSchema.QuestionRepository;
import com.sougata.form_service.service.formSchema.FormService;
import com.sougata.form_service.service.formSchema.QuestionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service("FILE_UPLOAD_QUESTION_MANAGER")
public class FileUploadManager extends QuestionManager<FileUpload, FileUploadPutReqDto, FileUploadDetailsDto, FileUploadTemplateDetails> {

    private final FileUploadRepository fileUploadRepository;
    private final FileTypeRepository fileTypeRepository;

    public FileUploadManager(FileUploadRepository fileUploadRepository, FileTypeRepository fileTypeRepository, FormService formService, QuestionRepository questionRepository) {
        super(questionRepository, formService);
        this.fileUploadRepository = fileUploadRepository;
        this.fileTypeRepository = fileTypeRepository;
    }

    @Override
    public FileUploadDetailsDto get(UUID formId, Long questionId) {
        return toQuestionResDto(fileUploadRepository.findByQuestionId(questionId).orElseThrow(() -> new QuestionNotFoundException(questionId)));
    }

    @Override
    @Transactional
    public FileUploadDetailsDto create(UUID formId, FileUploadPutReqDto crudDto) {
        var newFu = new FileUpload();

        var question = createQuestion(crudDto, formId);

        setPropertiesForNew(crudDto, newFu, question);

        var saved = fileUploadRepository.save(newFu);

        return toQuestionResDto(saved, question);
    }

    @Override
    @Transactional
    public FileUploadDetailsDto create(UUID formId, Long questionId, FileUploadPutReqDto questionAddUpdateReq) {
        var newFu = new FileUpload();

        var question = updateQuestion(questionId, questionAddUpdateReq);

        setPropertiesForNew(questionAddUpdateReq, newFu, question);

        var saved = fileUploadRepository.save(newFu);

        return toQuestionResDto(saved, question);
    }

    @Override
    @Transactional
    public FileUploadDetailsDto update(UUID formId, Long questionId, FileUploadPutReqDto questionAddUpdateReq) {
        FileUpload fu = fileUploadRepository.findByQuestionId(questionId)
                .orElseThrow(() -> new QuestionNotFoundException(QuestionType.FILE_UPLOAD, questionId));

        var question = updateQuestion(questionId, questionAddUpdateReq);

        List<String> categories = questionAddUpdateReq.getAllowedFileCategories();
        List<FileType> fileTypes = categories.stream()
                .map(category ->
                        fileTypeRepository.findByCategory(category)
                                .orElseThrow(() -> new FileTypeNotFoundException(category))
                ).collect(Collectors.toList());

        fu.getAllowedFileTypes().clear();

        fu.setMaxFileSize(questionAddUpdateReq.getMaxFileSize());
        fu.setAllowedFileTypes(fileTypes);

        fileUploadRepository.save(fu);

        return toQuestionResDto(fu, question);
    }

    @Override
    public FileUploadDetailsDto toQuestionResDto(FileUpload childQuestion) {
        return toQuestionResDto(childQuestion, childQuestion.getQuestion());
    }

    @Override
    public FileUploadDetailsDto toQuestionResDto(FileUpload childQuestion, Question parentQuestion) {
        var f = new FileUploadDetailsDto();

        populateCommonFields(parentQuestion, f);

        f.setAllowedFileTypes(childQuestion.getAllowedFileTypes().stream()
                .map(ft -> new FileTypeDetails(ft.getCategory(), Arrays.asList(ft.getMimeTypes())))
                .toList()
        );
        f.setMaxFileSize(childQuestion.getMaxFileSize());

        return f;
    }

    @Override
    public FileUploadPutReqDto toQuestionAddUpdateReq(FileUploadDetailsDto questionRes) {
        var f = new FileUploadPutReqDto();

        populateCommonFields(questionRes, f);

        f.setAllowedFileCategories(
                questionRes.getAllowedFileTypes().stream()
                        .map(FileTypeDetails::getCategory)
                        .toList()
        );
        f.setMaxFileSize(questionRes.getMaxFileSize());

        return f;
    }

    @Override
    @Transactional
    public FileUpload createFromTemplate(FileUploadTemplateDetails template, Form form) {
        var f = new FileUpload();

        f.setQuestion(createQuestionFromTemplate(template, form));
        f.setMaxFileSize(template.getMaxFileSize());
        f.setAllowedFileTypes(template.getAllowedFileTypes().stream().map(ft ->
                        fileTypeRepository.findByCategory(ft.getCategory())
                                .orElseThrow(() -> new FileTypeNotFoundException(ft.getCategory()))
                ).toList()
        );

        return fileUploadRepository.save(f);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.FILE_UPLOAD;
    }

    @Override
    public void delete(UUID formId, Long questionId) {
        fileUploadRepository.deleteQuestion(questionId);
    }

    private void setPropertiesForNew(FileUploadPutReqDto source, FileUpload target, Question question) {
        List<String> categories = source.getAllowedFileCategories();
        List<FileType> fileTypes = categories.stream()
                .map(category ->
                        fileTypeRepository.findByCategory(category)
                                .orElseThrow(() -> new FileTypeNotFoundException(category))
                ).collect(Collectors.toList());

        target.setQuestion(question);
        target.setMaxFileSize(source.getMaxFileSize());
        target.setAllowedFileTypes(fileTypes);
    }
}
