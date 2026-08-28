package com.sougata.form_data_service.repository;

import com.sougata.form_data_service.model.AnyTypeQuestionResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@NoRepositoryBean
public interface AnyTypeQuestionResponseRepository<Q extends AnyTypeQuestionResponse, ID> extends JpaRepository<Q, ID> {

    @Modifying
    @Transactional
    @Query("""
            delete
            from #{#entityName} e
            where e.questionResponse.formResponse.formId = :formId
            and e.questionResponse.questionId = :questionId
            """)
    void deleteAllByFormIdAndQuestionId(UUID formId, Long questionId);

    @Modifying
    @Transactional
    @Query("""
            delete
            from #{#entityName} e
            where e.questionResponse.formResponse.formId = :formId
            and e.questionResponse.formResponse.id = :formResponseId
            """)
    void deleteAllByFormIdAndFormResponseId(UUID formId, long formResponseId);

}
