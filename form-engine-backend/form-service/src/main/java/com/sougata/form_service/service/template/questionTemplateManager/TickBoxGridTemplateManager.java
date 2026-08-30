package com.sougata.form_service.service.template.questionTemplateManager;

import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.template.questionTemplate.TickBoxGridTemplateDetails;
import com.sougata.form_service.model.template.TickBoxGridTemplate;
import com.sougata.form_service.service.template.QuestionTemplateManager;
import org.springframework.stereotype.Service;

import java.util.Comparator;

@Service("TICK_BOX_GRID_TEMPLATE_MANAGER")
public class TickBoxGridTemplateManager extends QuestionTemplateManager<TickBoxGridTemplate, TickBoxGridTemplateDetails> {

    @Override
    public TickBoxGridTemplateDetails toQuestionTemplateDetails(TickBoxGridTemplate template) {
        var tbg = new TickBoxGridTemplateDetails();

        populateCommonFields(template, tbg);

        var rows = template.getRows().stream()
                .map(r ->
                        new TickBoxGridTemplateDetails.TickBoxGridRowTemplateDetails(r.getId(), r.getRowName(), r.getOrderIndex())
                )
                .sorted(Comparator.comparingInt(TickBoxGridTemplateDetails.TickBoxGridRowTemplateDetails::getOrderIndex))
                .toList();

        var columns = template.getColumns().stream()
                .map(c ->
                        new TickBoxGridTemplateDetails.TickBoxGridColumnTemplateDetails(c.getId(), c.getColumnName(), c.getOrderIndex())
                )
                .sorted(Comparator.comparingInt(TickBoxGridTemplateDetails.TickBoxGridColumnTemplateDetails::getOrderIndex))
                .toList();

        tbg.setRows(rows);
        tbg.setColumns(columns);
        tbg.setEachRowRequired(template.getEachRowRequired());

        return tbg;
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.TICK_BOX_GRID;
    }
}
