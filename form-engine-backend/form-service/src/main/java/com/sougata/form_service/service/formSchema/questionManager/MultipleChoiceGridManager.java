package com.sougata.form_service.service.formSchema.questionManager;

import com.sougata.form_engine.constant.ComplexQuestionUpdateAction;
import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.question.details.MultipleChoiceGridDetailsDto;
import com.sougata.form_engine.dto.question.schemaaddrequest.MultipleChoiceGridAddReqDto;
import com.sougata.form_engine.dto.question.schemaupdatereq.MultipleChoiceGridUpdateReqDto;
import com.sougata.form_engine.dto.template.questionTemplate.MultipleChoiceGridTemplateDetails;
import com.sougata.form_service.exception.QuestionNotFoundException;
import com.sougata.form_service.model.formSchema.*;
import com.sougata.form_service.repository.formSchema.MultipleChoiceGridColumnRepository;
import com.sougata.form_service.repository.formSchema.MultipleChoiceGridRepository;
import com.sougata.form_service.repository.formSchema.MultipleChoiceGridRowRepository;
import com.sougata.form_service.repository.formSchema.QuestionRepository;
import com.sougata.form_service.service.formSchema.FormService;
import com.sougata.form_service.service.formSchema.QuestionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.UUID;

@Service("MULTIPLE_CHOICE_GRID_QUESTION_MANAGER")
public class MultipleChoiceGridManager extends QuestionManager<MultipleChoiceGrid, MultipleChoiceGridAddReqDto, MultipleChoiceGridUpdateReqDto, MultipleChoiceGridDetailsDto, MultipleChoiceGridTemplateDetails> {

    private final MultipleChoiceGridRepository multipleChoiceGridRepository;
    private final MultipleChoiceGridRowRepository multipleChoiceGridRowRepository;
    private final MultipleChoiceGridColumnRepository multipleChoiceGridColumnRepository;

    public MultipleChoiceGridManager(MultipleChoiceGridRepository multipleChoiceGridRepository, FormService formService, QuestionRepository questionRepository, MultipleChoiceGridRowRepository multipleChoiceGridRowRepository, MultipleChoiceGridColumnRepository multipleChoiceGridColumnRepository) {
        super(questionRepository, formService);
        this.multipleChoiceGridRepository = multipleChoiceGridRepository;
        this.multipleChoiceGridRowRepository = multipleChoiceGridRowRepository;
        this.multipleChoiceGridColumnRepository = multipleChoiceGridColumnRepository;
    }

    @Override
    public MultipleChoiceGridDetailsDto get(UUID formId, Long questionId) {
        return toQuestionResDto(multipleChoiceGridRepository.findByQuestionId(questionId).orElseThrow(() -> new QuestionNotFoundException(questionId)));
    }

    @Override
    @Transactional
    public MultipleChoiceGridDetailsDto create(UUID formId, MultipleChoiceGridAddReqDto crudDto) {
        var newMcg = new MultipleChoiceGrid();

        var question = createQuestion(crudDto, formId);

        setPropertiesForNew(crudDto, newMcg, question);

        var saved = multipleChoiceGridRepository.save(newMcg);

        return toQuestionResDto(saved, question);
    }

    @Override
    @Transactional
    public MultipleChoiceGridDetailsDto create(UUID formId, Long questionId, MultipleChoiceGridAddReqDto questionAddReq) {
        var newMcg = new MultipleChoiceGrid();

        var question = updateQuestion(questionId, questionAddReq);

        setPropertiesForNew(questionAddReq, newMcg, question);

        var saved = multipleChoiceGridRepository.save(newMcg);

        return toQuestionResDto(saved, question);
    }

    @Override
    @Transactional
    public MultipleChoiceGridDetailsDto update(UUID formId, Long questionId, MultipleChoiceGridUpdateReqDto questionUpdateReq) {
        MultipleChoiceGrid mcg = multipleChoiceGridRepository.findByQuestionId(questionId)
                .orElseThrow(() -> new QuestionNotFoundException(QuestionType.MULTIPLE_CHOICE_GRID, questionId));

        var question = updateQuestion(questionId, questionUpdateReq);

        questionUpdateReq.getUpdateFields().forEach(field -> {
            if (MultipleChoiceGridUpdateReqDto.Fields.eachRowRequired.equals(field)) {
                mcg.setEachRowRequired(questionUpdateReq.getEachRowRequired());
            }
            if (MultipleChoiceGridUpdateReqDto.Fields.rows.equals(field)) {

                var prevRows = mcg.getRows();

                questionUpdateReq.getRows().forEach(row -> {

                    var action = row.getAction();

                    if (action == ComplexQuestionUpdateAction.ADD) {

                        var mcgRow = new MultipleChoiceGridRow();

                        mcgRow.setMultipleChoiceGrid(mcg);
                        mcgRow.setRowName(row.getRow());
                        mcgRow.setOrderIndex(prevRows.size());

                        prevRows.add(mcgRow);

                    } else if (action == ComplexQuestionUpdateAction.UPDATE) {

                        var mcgRow = prevRows
                                .stream()
                                .filter(op -> op.getId().equals(row.getId()))
                                .findFirst()
                                .orElseThrow(() -> new IllegalArgumentException("Multiple choice grid row not found for Id: " + row.getId()));

                        mcgRow.setRowName(row.getRow());

                    } else if (action == ComplexQuestionUpdateAction.DELETE) {

                        var rowToDelete = prevRows
                                .stream()
                                .filter(op -> op.getId().equals(row.getId()))
                                .findFirst()
                                .orElseThrow(() -> new IllegalArgumentException("Multiple choice grid row not found for Id: " + row.getId()));

                        prevRows.remove(rowToDelete);

                        prevRows
                                .stream()
                                .sorted(Comparator.comparingInt(MultipleChoiceGridRow::getOrderIndex))
                                .forEach(op -> {

                                    if (op.getOrderIndex() > rowToDelete.getOrderIndex()) {
                                        op.setOrderIndex(op.getOrderIndex() - 1);
                                    }

                                });
                    }
                });

            }
            if (MultipleChoiceGridUpdateReqDto.Fields.columns.equals(field)) {

                var prevColumns = mcg.getColumns();

                questionUpdateReq.getColumns().forEach(column -> {

                    var action = column.getAction();

                    if (action == ComplexQuestionUpdateAction.ADD) {

                        var mcgColumn = new MultipleChoiceGridColumn();

                        mcgColumn.setMultipleChoiceGrid(mcg);
                        mcgColumn.setColumnName(column.getColumn());
                        mcgColumn.setOrderIndex(prevColumns.size());

                        prevColumns.add(mcgColumn);

                    } else if (action == ComplexQuestionUpdateAction.UPDATE) {

                        var mcgColumn = prevColumns
                                .stream()
                                .filter(op -> op.getId().equals(column.getId()))
                                .findFirst()
                                .orElseThrow(() -> new IllegalArgumentException("Multiple choice grid column not found for Id: " + column.getId()));

                        mcgColumn.setColumnName(column.getColumn());

                    } else if (action == ComplexQuestionUpdateAction.DELETE) {

                        var columnToDelete = prevColumns
                                .stream()
                                .filter(op -> op.getId().equals(column.getId()))
                                .findFirst()
                                .orElseThrow(() -> new IllegalArgumentException("Multiple choice grid column not found for Id: " + column.getId()));

                        prevColumns.remove(columnToDelete);

                        prevColumns
                                .stream()
                                .sorted(Comparator.comparingInt(MultipleChoiceGridColumn::getOrderIndex))
                                .forEach(op -> {

                                    if (op.getOrderIndex() > columnToDelete.getOrderIndex()) {
                                        op.setOrderIndex(op.getOrderIndex() - 1);
                                    }

                                });
                    }
                });
            }
        });

        return toQuestionResDto(multipleChoiceGridRepository.save(mcg), question);
    }

    @Override
    public MultipleChoiceGridDetailsDto toQuestionResDto(MultipleChoiceGrid childQuestion) {
        return toQuestionResDto(childQuestion, childQuestion.getQuestion());
    }

    @Override
    public MultipleChoiceGridDetailsDto toQuestionResDto(MultipleChoiceGrid childQuestion, Question parentQuestion) {
        var mc = new MultipleChoiceGridDetailsDto();

        populateCommonFields(parentQuestion, mc);

        var rows = childQuestion.getRows().stream()
                .map(row ->
                        new MultipleChoiceGridDetailsDto.Row(row.getId(), row.getRowName(), row.getOrderIndex())
                )
                .sorted(Comparator.comparingInt(MultipleChoiceGridDetailsDto.Row::getOrderIndex))
                .toList();

        var columns = childQuestion.getColumns().stream()
                .map(column ->
                        new MultipleChoiceGridDetailsDto.Column(column.getId(), column.getColumnName(), column.getOrderIndex())
                )
                .sorted(Comparator.comparingInt(MultipleChoiceGridDetailsDto.Column::getOrderIndex))
                .toList();

        mc.setEachRowRequired(childQuestion.getEachRowRequired());
        mc.setRows(rows);
        mc.setColumns(columns);

        return mc;
    }

    @Override
    public MultipleChoiceGridAddReqDto toQuestionAddUpdateReq(MultipleChoiceGridDetailsDto questionRes) {
        var mcg = new MultipleChoiceGridAddReqDto();

        populateCommonFields(questionRes, mcg);

        mcg.setRows(
                questionRes.getRows().stream()
                        .map(r -> new MultipleChoiceGridAddReqDto.Row(null, r.getRow()))
                        .toList()
        );
        mcg.setColumns(
                questionRes.getColumns().stream()
                        .map(c -> new MultipleChoiceGridAddReqDto.Column(null, c.getColumn()))
                        .toList()
        );
        mcg.setEachRowRequired(questionRes.getEachRowRequired());

        return mcg;
    }

    @Override
    @Transactional
    public MultipleChoiceGrid createFromTemplate(MultipleChoiceGridTemplateDetails template, Form form) {
        var mcg = new MultipleChoiceGrid();

        mcg.setQuestion(createQuestionFromTemplate(template, form));
        mcg.setEachRowRequired(template.getEachRowRequired());

        var rows = template.getRows().stream()
                .map(row -> {
                    var res = new MultipleChoiceGridRow();

                    res.setMultipleChoiceGrid(mcg);
                    res.setRowName(row.getRow());
                    res.setOrderIndex(row.getOrderIndex());

                    return res;
                })
                .toList();

        var columns = template.getColumns().stream()
                .map(column -> {
                    var res = new MultipleChoiceGridColumn();

                    res.setMultipleChoiceGrid(mcg);
                    res.setColumnName(column.getColumn());
                    res.setOrderIndex(column.getOrderIndex());

                    return res;
                })
                .toList();

        mcg.setRows(rows);
        mcg.setColumns(columns);

        return multipleChoiceGridRepository.save(mcg);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.MULTIPLE_CHOICE_GRID;
    }

    @Override
    @Transactional
    public void delete(Long questionId) {
        multipleChoiceGridRepository.deleteQuestion(questionId);
    }

    private void setPropertiesForNew(MultipleChoiceGridAddReqDto source, MultipleChoiceGrid target, Question question) {
        var rows = new ArrayList<MultipleChoiceGridRow>();
        var columns = new ArrayList<MultipleChoiceGridColumn>();

        for (int i = 0; i < source.getRows().size(); i++) {
            var row = source.getRows().get(i);
            var mcgRow = new MultipleChoiceGridRow();

            mcgRow.setRowName(row.getRow());
            mcgRow.setMultipleChoiceGrid(target);
            mcgRow.setOrderIndex(i);

            rows.add(mcgRow);
        }

        for (int i = 0; i < source.getColumns().size(); i++) {
            var column = source.getColumns().get(i);
            var mcgColumn = new MultipleChoiceGridColumn();

            mcgColumn.setColumnName(column.getColumn());
            mcgColumn.setMultipleChoiceGrid(target);
            mcgColumn.setOrderIndex(i);

            columns.add(mcgColumn);
        }

        target.setRows(rows);
        target.setColumns(columns);
        target.setEachRowRequired(source.getEachRowRequired());
        target.setQuestion(question);
    }
}
