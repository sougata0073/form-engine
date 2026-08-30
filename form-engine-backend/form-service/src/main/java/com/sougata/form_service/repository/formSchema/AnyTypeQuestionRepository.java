package com.sougata.form_service.repository.formSchema;

import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_service.model.formSchema.AnyTypeQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@NoRepositoryBean
public interface AnyTypeQuestionRepository<Q extends AnyTypeQuestion, ID> extends JpaRepository<Q, ID> {

    Optional<Q> findByQuestionId(Long questionId);

    @Modifying
    @Transactional
    @Query("delete from #{#entityName} e where e.questionId = :questionId")
    void deleteQuestion(long questionId);

    default QuestionType getQuestionType() {
        throw new UnsupportedOperationException(
                "Must be implemented by concrete repository"
        );
    }
}
