package com.sougata.form_service.service.formSchema.questionManager;

import com.sougata.form_service.constant.QuestionType;
import com.sougata.form_service.dto.question.request.MultipleChoiceGridPutReqDto;
import com.sougata.form_service.dto.question.response.MultipleChoiceGridDetailsDto;
import com.sougata.form_service.dto.template.questionTemplate.MultipleChoiceGridTemplateDetails;
import com.sougata.form_service.exception.QuestionNotFoundException;
import com.sougata.form_service.model.formSchema.*;
import com.sougata.form_service.repository.formSchema.MultipleChoiceGridRepository;
import com.sougata.form_service.repository.formSchema.QuestionRepository;
import com.sougata.form_service.service.formSchema.FormService;
import com.sougata.form_service.service.formSchema.QuestionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service("MULTIPLE_CHOICE_GRID_QUESTION_MANAGER")
public class MultipleChoiceGridManager extends QuestionManager<MultipleChoiceGrid, MultipleChoiceGridPutReqDto, MultipleChoiceGridDetailsDto, MultipleChoiceGridTemplateDetails> {

    private final MultipleChoiceGridRepository multipleChoiceGridRepository;

    public MultipleChoiceGridManager(MultipleChoiceGridRepository multipleChoiceGridRepository, FormService formService, QuestionRepository questionRepository) {
        super(questionRepository, formService);
        this.multipleChoiceGridRepository = multipleChoiceGridRepository;
    }

    @Override
    public MultipleChoiceGridDetailsDto get(UUID formId, Long questionId) {
        return toQuestionResDto(multipleChoiceGridRepository.findByQuestionId(questionId).orElseThrow(() -> new QuestionNotFoundException(questionId)));
    }

    @Override
    @Transactional
    public MultipleChoiceGridDetailsDto create(UUID formId, MultipleChoiceGridPutReqDto crudDto) {
        var newMcg = new MultipleChoiceGrid();

        var question = createQuestion(crudDto, formId);

        setPropertiesForNew(crudDto, newMcg, question);

        var saved = multipleChoiceGridRepository.save(newMcg);

        return toQuestionResDto(saved, question);
    }

    @Override
    @Transactional
    public MultipleChoiceGridDetailsDto create(UUID formId, Long questionId, MultipleChoiceGridPutReqDto questionAddUpdateReq) {
        var newMcg = new MultipleChoiceGrid();

        var question = updateQuestion(questionId, questionAddUpdateReq);

        setPropertiesForNew(questionAddUpdateReq, newMcg, question);

        var saved = multipleChoiceGridRepository.save(newMcg);

        return toQuestionResDto(saved, question);
    }

    @Override
    @Transactional
    public MultipleChoiceGridDetailsDto update(UUID formId, Long questionId, MultipleChoiceGridPutReqDto questionAddUpdateReq) {
        MultipleChoiceGrid mcg = multipleChoiceGridRepository.findByQuestionId(questionId)
                .orElseThrow(() -> new QuestionNotFoundException(QuestionType.MULTIPLE_CHOICE_GRID, questionId));

        var question = updateQuestion(questionId, questionAddUpdateReq);
        mcg.setEachRowRequired(questionAddUpdateReq.getEachRowRequired());

        Map<Long, MultipleChoiceGridRow> existingRows = mcg.getRows().stream()
                .collect(Collectors.toMap(MultipleChoiceGridRow::getId, row -> row));
        Set<Long> requestRowIds = questionAddUpdateReq.getRows().stream()
                .map(MultipleChoiceGridPutReqDto.Row::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        mcg.getRows().removeIf(row -> !requestRowIds.contains(row.getId()));

        for (int i = 0; i < questionAddUpdateReq.getRows().size(); i++) {
            var dto = questionAddUpdateReq.getRows().get(i);

            if (dto.getId() == null) {
                MultipleChoiceGridRow row = new MultipleChoiceGridRow();
                row.setRowName(dto.getRow());
                row.setOrderIndex(i);
                row.setMultipleChoiceGrid(mcg);

                mcg.getRows().add(row);
            } else {
                MultipleChoiceGridRow row = existingRows.get(dto.getId());

                if (row == null) {
                    throw new IllegalArgumentException("Invalid row id: " + dto.getId());
                }

                row.setRowName(dto.getRow());
                row.setOrderIndex(i);
            }
        }

        Map<Long, MultipleChoiceGridColumn> existingColumns = mcg.getColumns().stream()
                .collect(Collectors.toMap(MultipleChoiceGridColumn::getId, column -> column));
        Set<Long> requestColumnIds = questionAddUpdateReq.getColumns().stream()
                .map(MultipleChoiceGridPutReqDto.Column::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        mcg.getColumns().removeIf(column -> !requestColumnIds.contains(column.getId()));

        for (int i = 0; i < questionAddUpdateReq.getColumns().size(); i++) {
            var dto = questionAddUpdateReq.getColumns().get(i);

            if (dto.getId() == null) {
                MultipleChoiceGridColumn column = new MultipleChoiceGridColumn();
                column.setColumnName(dto.getColumn());
                column.setOrderIndex(i);
                column.setMultipleChoiceGrid(mcg);

                mcg.getColumns().add(column);
            } else {
                MultipleChoiceGridColumn column = existingColumns.get(dto.getId());

                if (column == null) {
                    throw new IllegalArgumentException("Invalid column id: " + dto.getId());
                }

                column.setColumnName(dto.getColumn());
                column.setOrderIndex(i);
            }
        }

        multipleChoiceGridRepository.save(mcg);

        return toQuestionResDto(mcg, question);
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
    public MultipleChoiceGridPutReqDto toQuestionAddUpdateReq(MultipleChoiceGridDetailsDto questionRes) {
        var mcg = new MultipleChoiceGridPutReqDto();

        populateCommonFields(questionRes, mcg);

        mcg.setRows(
                questionRes.getRows().stream()
                        .map(r -> new MultipleChoiceGridPutReqDto.Row(null, r.getRow()))
                        .toList()
        );
        mcg.setColumns(
                questionRes.getColumns().stream()
                        .map(c -> new MultipleChoiceGridPutReqDto.Column(null, c.getColumn()))
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
    public void delete(UUID formId, Long questionId) {
        multipleChoiceGridRepository.deleteQuestion(questionId);
    }

    private void setPropertiesForNew(MultipleChoiceGridPutReqDto source, MultipleChoiceGrid target, Question question) {
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
