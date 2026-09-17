package com.sougata.form_service.service.formSchema.questionManager;

import com.sougata.form_engine.constant.ComplexQuestionUpdateAction;
import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.question.details.TickBoxGridDetailsDto;
import com.sougata.form_engine.dto.question.schemaaddrequest.TickBoxGridAddReqDto;
import com.sougata.form_engine.dto.question.schemaupdatereq.MultipleChoiceGridUpdateReqDto;
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

import java.util.*;
import java.util.stream.Collectors;

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
            if (TickBoxGridUpdateReqDto.Fields.row.equals(field)) {
                var row = questionUpdateReq.getRow();
                var action = row.getAction();

                if (action == ComplexQuestionUpdateAction.ADD) {

                    var tbgRow = new TickBoxGridRow();

                    tbgRow.setTickBoxGrid(tbg);
                    tbgRow.setRowName(row.getRow());
                    tbgRow.setOrderIndex(tickBoxGridRepository.getRowCount(questionId).intValue());

                    tickBoxGridRowRepository.save(tbgRow);

                } else if (action == ComplexQuestionUpdateAction.UPDATE) {

                    var tbgRow = tickBoxGridRowRepository.findById(row.getId())
                            .orElseThrow(() -> new IllegalArgumentException("Tick box grid row not found for Id: " + row.getId()));

                    tbgRow.setRowName(row.getRow());

                    tickBoxGridRowRepository.save(tbgRow);

                } else if (action == ComplexQuestionUpdateAction.DELETE) {
                    tickBoxGridRowRepository.deleteById(row.getId());
                }
            }
            if (TickBoxGridUpdateReqDto.Fields.column.equals(field)) {
                var column = questionUpdateReq.getColumn();
                var action = column.getAction();

                if (action == ComplexQuestionUpdateAction.ADD) {

                    var tbgColumn = new TickBoxGridColumn();

                    tbgColumn.setTickBoxGrid(tbg);
                    tbgColumn.setColumnName(column.getColumn());
                    tbgColumn.setOrderIndex(tickBoxGridRepository.getColumnCount(questionId).intValue());

                    tickBoxGridColumnRepository.save(tbgColumn);

                } else if (action == ComplexQuestionUpdateAction.UPDATE) {

                    var mcgColumn = tickBoxGridColumnRepository.findById(column.getId())
                            .orElseThrow(() -> new IllegalArgumentException("Tick box grid column not found for Id: " + column.getId()));

                    mcgColumn.setColumnName(column.getColumn());

                    tickBoxGridColumnRepository.save(mcgColumn);

                } else if (action == ComplexQuestionUpdateAction.DELETE) {
                    tickBoxGridColumnRepository.deleteById(column.getId());
                }
            }
        });

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
