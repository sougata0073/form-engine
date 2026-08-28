package com.sougata.form_service.service.template.questionTemplateManager;

import com.sougata.form_service.constant.QuestionType;
import com.sougata.form_service.dto.template.questionTemplate.RatingTemplateDetails;
import com.sougata.form_service.model.template.RatingTemplate;
import com.sougata.form_service.service.template.QuestionTemplateManager;
import org.springframework.stereotype.Service;

@Service("RATING_TEMPLATE_MANAGER")
public class RatingTemplateManager extends QuestionTemplateManager<RatingTemplate, RatingTemplateDetails> {

    @Override
    public RatingTemplateDetails toQuestionTemplateDetails(RatingTemplate template) {
        var r = new RatingTemplateDetails();

        populateCommonFields(template, r);

        r.setMaxRatingNumber(template.getMaxRatingNumber());
        r.setRatingIcon(template.getRatingIcon());

        return r;
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.RATING;
    }
}
