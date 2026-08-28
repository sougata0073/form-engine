package com.sougata.form_response_service.repository;

import com.sougata.form_response_service.model.ShortAnswer;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("SHORT_ANSWER_RESPONSE_REPOSITORY")
public interface ShortAnswerRepository extends AnyTypeQuestionResponseRepository<ShortAnswer, Long> {

    @Query("""
            select
            sa.text
            from ShortAnswer sa
            where sa.questionResponse.questionId = :questionId
            group by sa.text
            order by count(sa.questionResponseId) desc, sa.text asc
            """)
    List<String> getResponseTexts(long questionId, Pageable pageable);

    @Query(value = """
            select
            sa.text text,
            count(*) responseCount
            from form_responses fr
            left join question_responses qr
            on fr.id = qr.form_response_id
            and qr.question_id = :questionId
            left join short_answers sa
            on qr.id = sa.question_response_id
            group by sa.text
            order by responseCount desc, sa.text asc
            """, nativeQuery = true)
    List<Tuple> groupedByText(long questionId, Pageable pageable);

    @Query(value = """
            select
            fr.id responseId,
            fr.user_id userId
            from form_responses fr
            left join question_responses qr
            on fr.id = qr.form_response_id
            and qr.question_id = :questionId
            left join short_answers sa
            on qr.id = sa.question_response_id
            where (
                (:response is null and sa.text is null)
                or sa.text = :response
            )
            order by fr.created_at, fr.id
            """, nativeQuery = true)
    List<Tuple> getResponseIdsByGroupedResponse(long questionId, String response, Pageable pageable);

    @Query("""
            select
            sa.questionResponse.questionId questionId,
            sa.text text
            from ShortAnswer sa
            where sa.questionResponse.formResponse.id = :formResponseId
            """)
    List<Tuple> getTextsByFormResponse(long formResponseId);
}
