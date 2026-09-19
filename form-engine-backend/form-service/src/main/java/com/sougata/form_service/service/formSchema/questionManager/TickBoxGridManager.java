package com.sougata.form_service.service.formSchema.questionManager;

import com.sougata.form_engine.constant.ComplexQuestionUpdateAction;
import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.question.details.TickBoxGridDetailsDto;
import com.sougata.form_engine.dto.question.schemaaddrequest.TickBoxGridAddReqDto;
import com.sougata.form_engine.dto.question.schemaupdatereq.TickBoxGridUpdateReqDto;
import com.sougata.form_engine.dto.template.questionTemplate.TickBoxGridTemplateDetails;
import com.sougata.form_service.exception.QuestionNotFoundException;
import com.sougata.form_service.model.formSchema.*;
import com.sougata.form_service.repository.formSchema.QuestionRepository;
import com.sougata.form_service.repository.formSchema.TickBoxGridColumnRepository;
import com.sougata.form_service.repository.formSchema.TickBoxGridRepository;
import com.sougata.form_service.repository.formSchema.TickBoxGridRowRepository;
import com.sougata.form_service.service.formSchema.FormService;
import com.sougata.form_service.service.formSchema.QuestionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.UUID;

@Service("TICK_BOX_GRID_QUESTION_MANAGER")
public class TickBoxGridManager extends QuestionManager<TickBoxGrid, TickBoxGridAddReqDto, TickBoxGridUpdateReqDto, TickBoxGridDetailsDto, TickBoxGridTemplateDetails> {

    private final TickBoxGridRepository tickBoxGridRepository;
    private final TickBoxGridRowRepository tickBoxGridRowRepository;
    private final TickBoxGridColumnRepository tickBoxGridColumnRepository;

    public TickBoxGridManager(TickBoxGridRepository tickBoxGridRepository, FormService formService, QuestionRepository questionRepository, TickBoxGridRowRepository tickBoxGridRowRepository, TickBoxGridColumnRepository tickBoxGridColumnRepository) {
        super(questionRepository, formService);
        this.tickBoxGridRepository = tickBoxGridRepository;
        this.tickBoxGridRowRepository = tickBoxGridRowRepository;
        this.tickBoxGridColumnRepository = tickBoxGridColumnRepository;
    }

    @Override
    public TickBoxGridDetailsDto get(UUID formId, Long questionId) {
        return toQuestionResDto(tickBoxGridRepository.findByQuestionId(questionId).orElseThrow(() -> new QuestionNotFoundException(questionId)));
    }

    @Override
    @Transactional
    public TickBoxGridDetailsDto create(UUID formId, TickBoxGridAddReqDto crudDto) {
        var newTbg = new TickBoxGrid();

        var question = createQuestion(crudDto, formId);

        setPropertiesForNew(crudDto, newTbg, question);

        var savedTbg = tickBoxGridRepository.save(newTbg);

        return toQuestionResDto(savedTbg, question);
    }

    @Override
    @Transactional
    public TickBoxGridDetailsDto create(UUID formId, Long questionId, TickBoxGridAddReqDto questionAddReq) {
        var newTbg = new TickBoxGrid();

        var question = updateQuestion(questionId, questionAddReq);

        setPropertiesForNew(questionAddReq, newTbg, question);

        var savedTbg = tickBoxGridRepository.save(newTbg);

        return toQuestionResDto(savedTbg, question);
    }

    @Override
    @Transactional
    public TickBoxGridDetailsDto update(UUID formId, Long questionId, TickBoxGridUpdateReqDto questionUpdateReq) {
        TickBoxGrid tbg = tickBoxGridRepository.findByQuestionId(questionId)
                .orElseThrow(() -> new QuestionNotFoundException(QuestionType.TICK_BOX_GRID, questionId));

        var question = updateQuestion(questionId, questionUpdateReq);

        questionUpdateReq.getUpdateFields().forEach(field -> {
            if (TickBoxGridUpdateReqDto.Fields.eachRowRequired.equals(field)) {

                tbg.setEachRowRequired(questionUpdateReq.getEachRowRequired());
            }

            if (TickBoxGridUpdateReqDto.Fields.rows.equals(field)) {

                var prevRows = tbg.getRows();

                questionUpdateReq.getRows().forEach(row -> {

                    var action = row.getAction();

                    if (action == ComplexQuestionUpdateAction.ADD) {

                        var tbgRow = new TickBoxGridRow();

                        tbgRow.setTickBoxGrid(tbg);
                        tbgRow.setRowName(row.getRow());
                        tbgRow.setOrderIndex(prevRows.size());

                        prevRows.add(tbgRow);

                    } else if (action == ComplexQuestionUpdateAction.UPDATE) {

                        var tbgRow = prevRows
                                .stream()
                                .filter(op -> op.getId().equals(row.getId()))
                                .findFirst()
                                .orElseThrow(() -> new IllegalArgumentException("Tick Box grid row not found for Id: " + row.getId()));

                        tbgRow.setRowName(row.getRow());

                    } else if (action == ComplexQuestionUpdateAction.DELETE) {

                        var rowToDelete = prevRows
                                .stream()
                                .filter(op -> op.getId().equals(row.getId()))
                                .findFirst()
                                .orElseThrow(() -> new IllegalArgumentException("Tick Box grid row not found for Id: " + row.getId()));

                        prevRows.remove(rowToDelete);

                        prevRows
                                .stream()
                                .sorted(Comparator.comparingInt(TickBoxGridRow::getOrderIndex))
                                .forEach(op -> {

                                    if (op.getOrderIndex() > rowToDelete.getOrderIndex()) {
                                        op.setOrderIndex(op.getOrderIndex() - 1);
                                    }

                                });
                    }
                });

            }
            if (TickBoxGridUpdateReqDto.Fields.columns.equals(field)) {

                var prevColumns = tbg.getColumns();

                questionUpdateReq.getColumns().forEach(column -> {

                    var action = column.getAction();

                    if (action == ComplexQuestionUpdateAction.ADD) {

                        var tbgColumn = new TickBoxGridColumn();

                        tbgColumn.setTickBoxGrid(tbg);
                        tbgColumn.setColumnName(column.getColumn());
                        tbgColumn.setOrderIndex(prevColumns.size());

                        prevColumns.add(tbgColumn);

                    } else if (action == ComplexQuestionUpdateAction.UPDATE) {

                        var tbgColumn = prevColumns
                                .stream()
                                .filter(op -> op.getId().equals(column.getId()))
                                .findFirst()
                                .orElseThrow(() -> new IllegalArgumentException("Tick Box grid column not found for Id: " + column.getId()));

                        tbgColumn.setColumnName(column.getColumn());

                    } else if (action == ComplexQuestionUpdateAction.DELETE) {

                        var columnToDelete = prevColumns
                                .stream()
                                .filter(op -> op.getId().equals(column.getId()))
                                .findFirst()
                                .orElseThrow(() -> new IllegalArgumentException("Tick Box grid column not found for Id: " + column.getId()));

                        prevColumns.remove(columnToDelete);

                        prevColumns
                                .stream()
                                .sorted(Comparator.comparingInt(TickBoxGridColumn::getOrderIndex))
                                .forEach(op -> {

                                    if (op.getOrderIndex() > columnToDelete.getOrderIndex()) {
                                        op.setOrderIndex(op.getOrderIndex() - 1);
                                    }

                                });
                    }
                });

            }

        });

        return toQuestionResDto(tickBoxGridRepository.save(tbg), question);
    }

    @Override
    public TickBoxGridDetailsDto toQuestionResDto(TickBoxGrid childQuestion) {
        return toQuestionResDto(childQuestion, childQuestion.getQuestion());
    }

    @Override
    public TickBoxGridDetailsDto toQuestionResDto(TickBoxGrid childQuestion, Question parentQuestion) {
        var t = new TickBoxGridDetailsDto();

        populateCommonFields(parentQuestion, t);

        var rows = childQuestion.getRows().stream()
                .map(row ->
                        new TickBoxGridDetailsDto.Row(row.getId(), row.getRowName(), row.getOrderIndex())
                )
                .sorted(Comparator.comparingInt(TickBoxGridDetailsDto.Row::getOrderIndex))
                .toList();

        var columns = childQuestion.getColumns().stream()
                .map(column ->
                        new TickBoxGridDetailsDto.Column(column.getId(), column.getColumnName(), column.getOrderIndex())
                )
                .sorted(Comparator.comparingInt(TickBoxGridDetailsDto.Column::getOrderIndex))
                .toList();

        t.setEachRowRequired(childQuestion.getEachRowRequired());
        t.setRows(rows);
        t.setColumns(columns);

        return t;
    }

    @Override
    public TickBoxGridAddReqDto toQuestionAddUpdateReq(TickBoxGridDetailsDto questionRes) {
        var tbg = new TickBoxGridAddReqDto();

        populateCommonFields(questionRes, tbg);

        tbg.setRows(
                questionRes.getRows().stream()
                        .map(r -> new TickBoxGridAddReqDto.Row(null, r.getRow()))
                        .toList()
        );
        tbg.setColumns(
                questionRes.getColumns().stream()
                        .map(c -> new TickBoxGridAddReqDto.Column(null, c.getColumn()))
                        .toList()
        );
        tbg.setEachRowRequired(questionRes.getEachRowRequired());

        return tbg;
    }

    @Override
    @Transactional
    public TickBoxGrid createFromTemplate(TickBoxGridTemplateDetails template, Form form) {
        var tbg = new TickBoxGrid();

        tbg.setQuestion(createQuestionFromTemplate(template, form));
        tbg.setEachRowRequired(template.getEachRowRequired());

        var rows = template.getRows().stream()
                .map(row -> {
                    var res = new TickBoxGridRow();

                    res.setTickBoxGrid(tbg);
                    res.setRowName(row.getRow());
                    res.setOrderIndex(row.getOrderIndex());

                    return res;
                })
                .toList();

        var columns = template.getColumns().stream()
                .map(column -> {
                    var res = new TickBoxGridColumn();

                    res.setTickBoxGrid(tbg);
                    res.setColumnName(column.getColumn());
                    res.setOrderIndex(column.getOrderIndex());

                    return res;
                })
                .toList();

        tbg.setRows(rows);
        tbg.setColumns(columns);

        return tickBoxGridRepository.save(tbg);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.TICK_BOX_GRID;
    }

    @Override
    @Transactional
    public void delete(Long questionId) {
        tickBoxGridRepository.deleteQuestion(questionId);
    }

    private void setPropertiesForNew(TickBoxGridAddReqDto source, TickBoxGrid target, Question question) {
        var rows = new ArrayList<TickBoxGridRow>();
        var columns = new ArrayList<TickBoxGridColumn>();

        for (int i = 0; i < source.getRows().size(); i++) {
            var row = source.getRows().get(i);
            var tbgRow = new TickBoxGridRow();

            tbgRow.setRowName(row.getRow());
            tbgRow.setTickBoxGrid(target);
            tbgRow.setOrderIndex(i);

            rows.add(tbgRow);
        }

        for (int i = 0; i < source.getColumns().size(); i++) {
            var column = source.getColumns().get(i);
            var tbgColumn = new TickBoxGridColumn();

            tbgColumn.setColumnName(column.getColumn());
            tbgColumn.setTickBoxGrid(target);
            tbgColumn.setOrderIndex(i);

            columns.add(tbgColumn);
        }

        target.setRows(rows);
        target.setColumns(columns);
        target.setEachRowRequired(source.getEachRowRequired());
        target.setQuestion(question);
    }
}
