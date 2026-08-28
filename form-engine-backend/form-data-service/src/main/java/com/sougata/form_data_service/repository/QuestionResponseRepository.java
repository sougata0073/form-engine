package com.sougata.form_data_service.repository;

import com.sougata.form_data_service.model.QuestionResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface QuestionResponseRepository extends JpaRepository<QuestionResponse, Long> {

    @Modifying
    @Transactional
    @Query("delete from QuestionResponse qr where qr.questionId = :questionId")
    void deleteAllByQuestionId(long questionId);

}
