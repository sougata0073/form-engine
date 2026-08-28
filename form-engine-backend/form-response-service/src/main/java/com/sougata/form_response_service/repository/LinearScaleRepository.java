package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.LinearScale;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository("LINEAR_SCALE_RESPONSE_REPOSITORY")
public interface LinearScaleRepository extends AnyTypeQuestionResponseRepository<LinearScale, Long> {

    @Query("""
            select
            ls.questionResponse.questionId questionId,
            ls.scale scale,
            count(ls.scale) responseCount
            from LinearScale ls
            where ls.questionResponse.formResponse.formId = :formId
            group by ls.scale, ls.questionResponse.questionId
            """)
    List<Tuple> getResponseScaleCount(UUID formId);

    @Query(value = """
            select
            ls.scale scale,
            count(*) responseCount
            from form_responses fr
            left join question_responses  qr
            on fr.id = qr.form_response_id
            and qr.question_id = :questionId
            left join linear_scales ls
            on qr.id = ls.question_response_id
            group by ls.scale
            order by responseCount desc, ls.scale asc
            """, nativeQuery = true)
    List<Tuple> groupedByResponseScale(long questionId, Pageable pageable);

    @Query("""
            select
            ls.questionResponse.questionId questionId,
            ls.scale scale
            from LinearScale ls
            where ls.questionResponse.formResponse.id = :formResponseId
            """)
    List<Tuple> getScalesByFormResponse(long formResponseId);

    @Query(value = """
            select
            fr.id responseId,
            fr.user_id userId
            from form_responses fr
            left join question_responses qr
            on fr.id = qr.form_response_id
            and qr.question_id = :questionId
            left join linear_scales ls
            on qr.id = ls.question_response_id
            where (
                (:response is null and ls.scale is null)
                or ls.scale = :response
            )
            order by fr.created_at, fr.id
            """, nativeQuery = true)
    List<Tuple> getResponseIdsByGroupedResponse(long questionId, Integer response, Pageable pageable);

}
