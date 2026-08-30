package com.sougata.form_response_service.repository;

import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_response_service.model.QuestionResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionResponseRepository extends JpaRepository<QuestionResponse, Long> {

    @Query("select distinct qr.questionType from QuestionResponse qr where qr.formResponse.id = :formResponseId")
    List<QuestionType> findDistinctQuestionTypesByFormResponseId(Long formResponseId);

}
