package com.sougata.form_data_service.service.impl;

import com.sougata.form_data_service.repository.QuestionResponseRepository;
import com.sougata.form_data_service.service.QuestionResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionResponseServiceImpl implements QuestionResponseService {

    private final QuestionResponseRepository questionResponseRepository;

    @Override
    public void deleteAllByQuestionId(Long questionId) {
        questionResponseRepository.deleteAllByQuestionId(questionId);
    }

    @Override
    public void deleteAllByQuestionIdAndFormResponseId(Long questionId, Long formResponseId) {
        questionResponseRepository.deleteAllByQuestionIdAndFormResponseId(questionId, formResponseId);
    }

}
