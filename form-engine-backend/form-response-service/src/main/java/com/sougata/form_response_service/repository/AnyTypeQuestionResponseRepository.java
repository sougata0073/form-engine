package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.AnyTypeQuestionResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.UUID;

@NoRepositoryBean
public interface AnyTypeQuestionResponseRepository<Q extends AnyTypeQuestionResponse, ID> extends JpaRepository<Q, ID> {

    @Query("select qr from #{#entityName} qr where qr.questionResponse.questionResponseSummary.formResponseSummary.formId = :formId")
    List<Q> findAllByFormId(UUID formId);

    @Query("select qr from #{#entityName} qr where qr.questionResponse.questionResponseSummary.questionId = :questionId")
    List<Q> findAllByQuestionId(Long questionId);
}
