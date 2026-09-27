package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.QuestionResponseSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface QuestionResponseSummaryRepository extends JpaRepository<QuestionResponseSummary, Long> {

    @Query("select qrs from QuestionResponseSummary qrs where qrs.formResponseSummary.formId = :formId")
    List<QuestionResponseSummary> findAllByFormId(UUID formId);

    @Query("select qrs.questionId from QuestionResponseSummary qrs where qrs.formResponseSummary.formId = :formId")
    Set<Long> findAllQuestionIdsByFormId(UUID formId);

    Optional<QuestionResponseSummary> findByQuestionId(Long questionId);

    @Modifying
    @Transactional
    @Query("delete from QuestionResponseSummary qrs where qrs.questionId = :questionId")
    void deleteByQuestionId(Long questionId);

    @Modifying
    @Transactional
    @Query("update QuestionResponseSummary qrs set qrs.responseCount = qrs.responseCount + :incrementBy where qrs.questionId in :questionIds")
    void incrementResponseCounts(Set<Long> questionIds, Long incrementBy);
}
