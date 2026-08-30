package com.sougata.form_service.service.template.questionTemplateManager;

import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.template.questionTemplate.DropdownTemplateDetails;
import com.sougata.form_service.model.template.DropdownTemplate;
import com.sougata.form_service.service.template.QuestionTemplateManager;
import org.springframework.stereotype.Service;

import java.util.Comparator;

@Service("DROPDOWN_TEMPLATE_MANAGER")
public class DropdownTemplateManager extends QuestionTemplateManager<DropdownTemplate, DropdownTemplateDetails> {

    @Override
    public DropdownTemplateDetails toQuestionTemplateDetails(DropdownTemplate template) {
        var dd = new DropdownTemplateDetails();

        populateCommonFields(template, dd);

        var options = template.getOptions().stream()
                .map(o ->
                        new DropdownTemplateDetails.DropdownOptionTemplateDetails(o.getId(), o.getOption(), o.getOrderIndex())
                )
                .sorted(Comparator.comparingInt(DropdownTemplateDetails.DropdownOptionTemplateDetails::getOrderIndex))
                .toList();

        dd.setOptions(options);

        return dd;
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.DROPDOWN;
    }
}
