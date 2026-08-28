package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.AnyTypeQuestionResponse;
import com.sougata.form_response_service.projection.CommonResponseSummaryProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.UUID;

@NoRepositoryBean
public interface AnyTypeQuestionResponseRepository<Q extends AnyTypeQuestionResponse, ID> extends JpaRepository<Q, ID> {

    @Query("""
            select
            new com.sougata.form_response_service.projection.CommonResponseSummaryProjection(
                        qr.questionResponse.questionId, count(qr.questionResponseId)
            )
            from #{#entityName} qr
            where qr.questionResponse.formResponse.formId = :formId
            group by qr.questionResponse.questionId
            """)
    List<CommonResponseSummaryProjection> getResponseSummaries(UUID formId);

    @Query("""
            select
            new com.sougata.form_response_service.projection.CommonResponseSummaryProjection(
                        :questionId, count(qr.questionResponseId)
            )
            from #{#entityName} qr
            where qr.questionResponse.formResponse.formId = :formId and qr.questionResponse.questionId = :questionId
            """)
    CommonResponseSummaryProjection getResponseSummary(UUID formId, long questionId);

}
