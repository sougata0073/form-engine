package com.sougata.form_service.service.template.questionTemplateManager;

import com.sougata.form_service.constant.QuestionType;
import com.sougata.form_service.dto.template.questionTemplate.MultipleChoiceTemplateDetails;
import com.sougata.form_service.model.template.MultipleChoiceTemplate;
import com.sougata.form_service.service.template.QuestionTemplateManager;
import org.springframework.stereotype.Service;

import java.util.Comparator;

@Service("MULTIPLE_CHOICE_TEMPLATE_MANAGER")
public class MultipleChoiceTemplateManager extends QuestionTemplateManager<MultipleChoiceTemplate, MultipleChoiceTemplateDetails> {

    @Override
    public MultipleChoiceTemplateDetails toQuestionTemplateDetails(MultipleChoiceTemplate template) {
        var mc = new MultipleChoiceTemplateDetails();

        populateCommonFields(template, mc);

        var options = template.getOptions().stream()
                .map(o ->
                        new MultipleChoiceTemplateDetails.MultipleChoiceOptionTemplateDetails(o.getId(), o.getOption(), o.getOrderIndex())
                )
                .sorted(Comparator.comparingInt(MultipleChoiceTemplateDetails.MultipleChoiceOptionTemplateDetails::getOrderIndex))
                .toList();

        mc.setOptions(options);

        return mc;
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.MULTIPLE_CHOICE;
    }
}
