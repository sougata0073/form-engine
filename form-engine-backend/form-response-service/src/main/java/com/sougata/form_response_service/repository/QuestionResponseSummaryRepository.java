package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.QuestionResponseSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface QuestionResponseSummaryRepository extends JpaRepository<QuestionResponseSummary, Long> {

    @Query("select qrs from QuestionResponseSummary qrs where qrs.formResponseSummary.formId = :formId")
    List<QuestionResponseSummary> findAllByFormId(UUID formId);

    Optional<QuestionResponseSummary> findByQuestionId(Long questionId);

    boolean existsByQuestionId(Long questionId);

    @Modifying
    @Transactional
    @Query("delete from QuestionResponseSummary qrs where qrs.questionId = :questionId")
    void deleteByQuestionId(Long questionId);
}
