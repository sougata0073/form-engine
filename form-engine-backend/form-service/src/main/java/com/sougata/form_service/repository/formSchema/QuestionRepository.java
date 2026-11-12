package com.sougata.form_service.repository.formSchema;

import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_service.model.formSchema.Question;
import com.sougata.form_service.projection.QuestionIdAndOrderIndexProjection;
import com.sougata.form_service.projection.QuestionSummaryProjection;
import com.sougata.form_service.projection.QuestionTypeProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findAllByFormId(UUID formId);

    List<QuestionSummaryProjection> findQuestionSummariesByFormId(UUID formId);

    Optional<QuestionSummaryProjection> findQuestionSummaryById(Long id);

    Optional<QuestionTypeProjection> findQuestionTypeById(Long id);

    List<QuestionIdAndOrderIndexProjection> findAllIdAndOrderIndexByFormId(UUID formId);

    boolean existsByForm_IdAndQuestionType(UUID formId, QuestionType questionType);

    @Modifying
    @Transactional
    @Query("delete from Question q where q.id = :questionId")
    void deleteQuestion(long questionId);

    @Modifying
    @Transactional
    @Query(value = """
            update
            form_schema.questions q
            set order_index = q.order_index - 1
            where q.order_index > :orderIndex
            and q.form_id = :formId
            """, nativeQuery = true)
    void setQuestionOrderAfterDeleteQuestion(UUID formId, int orderIndex);

    @Modifying
    @Transactional
    @Query(value = """
            update
            form_schema.questions q
            set order_index = q.order_index + 1
            where q.order_index > :orderIndex
            and q.form_id = :formId
            """, nativeQuery = true)
    void setQuestionOrderDuringCopyQuestion(UUID formId, int orderIndex);

    @Query("select cast(count(q.id) as int) from Question q where q.form.id = :formId")
    Integer getNextQuestionIndex(UUID formId);

    @Modifying
    @Transactional
    @Query(value = """
            update
            form_schema.questions q
            set order_index = case
                when :prevIndex > :currIndex then (
                    case
                        when q.order_index >= :currIndex and q.order_index < :prevIndex then q.order_index + 1
                        else q.order_index
                    end
                )
                when :prevIndex < :currIndex then (
                    case
                        when q.order_index > :prevIndex and q.order_index <= :currIndex then q.order_index - 1
                        else q.order_index
                    end
                )
                else q.order_index
            end
            where q.id != :questionId
            and q.form_id = :formId
            """, nativeQuery = true)
    void updateNextQuestionOrderIndexes(UUID formId, long questionId, int prevIndex, int currIndex);
}
