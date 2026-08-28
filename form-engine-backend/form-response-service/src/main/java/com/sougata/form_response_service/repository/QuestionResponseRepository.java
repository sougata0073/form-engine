package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.QuestionResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionResponseRepository extends JpaRepository<QuestionResponse, Long> {

}
