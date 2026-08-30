package com.sougata.form_service.repository.formSchema;

import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_service.model.formSchema.Rating;
import org.springframework.stereotype.Repository;

@Repository("RATING_REPOSITORY")
public interface RatingRepository extends AnyTypeQuestionRepository<Rating, Long> {

    @Override
    default QuestionType getQuestionType() {
        return QuestionType.RATING;
    }

}

