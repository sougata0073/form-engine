package com.sougata.form_service.service.formSchema.questionManager;

import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.question.details.TickBoxGridDetailsDto;
import com.sougata.form_engine.dto.question.schemaputrequest.TickBoxGridPutReqDto;
import com.sougata.form_engine.dto.template.questionTemplate.TickBoxGridTemplateDetails;
import com.sougata.form_service.exception.QuestionNotFoundException;
import com.sougata.form_service.model.formSchema.*;
import com.sougata.form_service.repository.formSchema.QuestionRepository;
import com.sougata.form_service.repository.formSchema.TickBoxGridRepository;
import com.sougata.form_service.service.formSchema.FormService;
import com.sougata.form_service.service.formSchema.QuestionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service("TICK_BOX_GRID_QUESTION_MANAGER")
public class TickBoxGridManager extends QuestionManager<TickBoxGrid, TickBoxGridPutReqDto, TickBoxGridDetailsDto, TickBoxGridTemplateDetails> {

    private final TickBoxGridRepository tickBoxGridRepository;

    public TickBoxGridManager(TickBoxGridRepository tickBoxGridRepository, FormService formService, QuestionRepository questionRepository) {
        super(questionRepository, formService);
        this.tickBoxGridRepository = tickBoxGridRepository;
    }

    @Override
    public TickBoxGridDetailsDto get(UUID formId, Long questionId) {
        return toQuestionResDto(tickBoxGridRepository.findByQuestionId(questionId).orElseThrow(() -> new QuestionNotFoundException(questionId)));
    }

    @Override
    @Transactional
    public TickBoxGridDetailsDto create(UUID formId, TickBoxGridPutReqDto crudDto) {
        var newTbg = new TickBoxGrid();

        var question = createQuestion(crudDto, formId);

        setPropertiesForNew(crudDto, newTbg, question);

        var savedTbg = tickBoxGridRepository.save(newTbg);

        return toQuestionResDto(savedTbg, question);
    }

    @Override
    @Transactional
    public TickBoxGridDetailsDto create(UUID formId, Long questionId, TickBoxGridPutReqDto questionAddUpdateReq) {
        var newTbg = new TickBoxGrid();

        var question = updateQuestion(questionId, questionAddUpdateReq);

        setPropertiesForNew(questionAddUpdateReq, newTbg, question);

        var savedTbg = tickBoxGridRepository.save(newTbg);

        return toQuestionResDto(savedTbg, question);
    }

    @Override
    @Transactional
    public TickBoxGridDetailsDto update(UUID formId, Long questionId, TickBoxGridPutReqDto questionAddUpdateReq) {
        TickBoxGrid tbg = tickBoxGridRepository.findByQuestionId(questionId)
                .orElseThrow(() -> new QuestionNotFoundException(QuestionType.TICK_BOX_GRID, questionId));

        var question = updateQuestion(questionId, questionAddUpdateReq);
        tbg.setEachRowRequired(questionAddUpdateReq.getEachRowRequired());

        Map<Long, TickBoxGridRow> existingRows = tbg.getRows().stream()
                .collect(Collectors.toMap(TickBoxGridRow::getId, r -> r));
        Set<Long> requestRowIds = questionAddUpdateReq.getRows().stream()
                .map(TickBoxGridPutReqDto.Row::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        tbg.getRows().removeIf(row -> !requestRowIds.contains(row.getId()));

        for (int i = 0; i < questionAddUpdateReq.getRows().size(); i++) {
            var dto = questionAddUpdateReq.getRows().get(i);

            if (dto.getId() == null) {
                TickBoxGridRow row = new TickBoxGridRow();
                row.setRowName(dto.getRow());
                row.setOrderIndex(i);
                row.setTickBoxGrid(tbg);

                tbg.getRows().add(row);
            } else {
                TickBoxGridRow row = existingRows.get(dto.getId());

                if (row == null) {
                    throw new IllegalArgumentException("Invalid row id: " + dto.getId());
                }

                row.setRowName(dto.getRow());
                row.setOrderIndex(i);
            }
        }

        Map<Long, TickBoxGridColumn> existingColumns = tbg.getColumns().stream()
                .collect(Collectors.toMap(TickBoxGridColumn::getId, c -> c));
        Set<Long> requestColumnIds = questionAddUpdateReq.getColumns().stream()
                .map(TickBoxGridPutReqDto.Column::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        tbg.getColumns().removeIf(column -> !requestColumnIds.contains(column.getId()));

        for (int i = 0; i < questionAddUpdateReq.getColumns().size(); i++) {
            var dto = questionAddUpdateReq.getColumns().get(i);

            if (dto.getId() == null) {
                TickBoxGridColumn column = new TickBoxGridColumn();
                column.setColumnName(dto.getColumn());
                column.setOrderIndex(i);
                column.setTickBoxGrid(tbg);

                tbg.getColumns().add(column);
            } else {
                TickBoxGridColumn column = existingColumns.get(dto.getId());

                if (column == null) {
                    throw new IllegalArgumentException("Invalid column id: " + dto.getId());
                }

                column.setColumnName(dto.getColumn());
                column.setOrderIndex(i);
            }
        }

        tickBoxGridRepository.save(tbg);

        return toQuestionResDto(tbg, question);
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
    public TickBoxGridPutReqDto toQuestionAddUpdateReq(TickBoxGridDetailsDto questionRes) {
        var tbg = new TickBoxGridPutReqDto();

        populateCommonFields(questionRes, tbg);

        tbg.setRows(
                questionRes.getRows().stream()
                        .map(r -> new TickBoxGridPutReqDto.Row(null, r.getRow()))
                        .toList()
        );
        tbg.setColumns(
                questionRes.getColumns().stream()
                        .map(c -> new TickBoxGridPutReqDto.Column(null, c.getColumn()))
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
    public void delete(UUID formId, Long questionId) {
        tickBoxGridRepository.deleteQuestion(questionId);
    }

    private void setPropertiesForNew(TickBoxGridPutReqDto source, TickBoxGrid target, Question question) {
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
