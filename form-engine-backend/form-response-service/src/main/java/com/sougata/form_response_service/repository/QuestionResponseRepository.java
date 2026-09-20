package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.QuestionResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Repository
public interface QuestionResponseRepository extends JpaRepository<QuestionResponse, Long> {

    @Modifying
    @Transactional
    @Query("delete from QuestionResponse qr where qr.id in :ids")
    void deleteAllQuestionResponsesById(Set<Long> ids);

    @Modifying
    @Transactional
    @Query("delete from QuestionResponse qr where qr.id in :ids")
    void deleteAllQuestionResponsesById(List<Long> ids);

}
