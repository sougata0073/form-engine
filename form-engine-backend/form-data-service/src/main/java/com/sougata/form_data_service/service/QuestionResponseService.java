package com.sougata.form_data_service.service;

public interface QuestionResponseService {

    void deleteAllByQuestionId(Long questionId);
    void deleteAllByQuestionIdAndFormResponseId(Long questionId, Long formResponseId);

}
