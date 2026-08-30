package com.sougata.form_service.service.template.questionTemplateManager;

import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.template.questionTemplate.MultipleChoiceGridTemplateDetails;
import com.sougata.form_service.model.template.MultipleChoiceGridTemplate;
import com.sougata.form_service.service.template.QuestionTemplateManager;
import org.springframework.stereotype.Service;

import java.util.Comparator;

@Service("MULTIPLE_CHOICE_GRID_TEMPLATE_MANAGER")
public class MultipleChoiceGridTemplateManager extends QuestionTemplateManager<MultipleChoiceGridTemplate, MultipleChoiceGridTemplateDetails> {

    @Override
    public MultipleChoiceGridTemplateDetails toQuestionTemplateDetails(MultipleChoiceGridTemplate template) {
        var mcg = new MultipleChoiceGridTemplateDetails();

        populateCommonFields(template, mcg);

        var rows = template.getRows().stream()
                .map(r ->
                        new MultipleChoiceGridTemplateDetails.MultipleChoiceGridRowTemplateDetails(r.getId(), r.getRowName(), r.getOrderIndex())
                )
                .sorted(Comparator.comparingInt(MultipleChoiceGridTemplateDetails.MultipleChoiceGridRowTemplateDetails::getOrderIndex))
                .toList();

        var columns = template.getColumns().stream()
                .map(c ->
                        new MultipleChoiceGridTemplateDetails.MultipleChoiceGridColumnTemplateDetails(c.getId(), c.getColumnName(), c.getOrderIndex())
                )
                .sorted(Comparator.comparingInt(MultipleChoiceGridTemplateDetails.MultipleChoiceGridColumnTemplateDetails::getOrderIndex))
                .toList();

        mcg.setRows(rows);
        mcg.setColumns(columns);
        mcg.setEachRowRequired(template.getEachRowRequired());

        return mcg;
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.MULTIPLE_CHOICE_GRID;
    }
}
