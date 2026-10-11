package com.sougata.form_data_service.service.responseManager;

import com.sougata.form_data_service.model.AnyTypeQuestionResponse;
import com.sougata.form_data_service.model.Paragraph;
import com.sougata.form_data_service.repository.ParagraphRepository;
import com.sougata.form_engine.constant.QuestionType;
import com.sougata.form_engine.dto.formResponse.individual.ParagraphResponseIndividualDto;
import com.sougata.form_engine.dto.question.responseputrequest.ParagraphResponsePutReqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service("PARAGRAPH_RESPONSE_MANAGER")
public class ParagraphManager extends ResponseManager<ParagraphResponsePutReqDto, ParagraphResponseIndividualDto> {

    private final ParagraphRepository paragraphRepository;

    @Autowired
    public ParagraphManager(ParagraphRepository paragraphRepository) {
        this.paragraphRepository = paragraphRepository;
    }

    @Override
    @Async
    public CompletableFuture<Void> create(ParagraphResponsePutReqDto response, UUID formResponseId) {
        Paragraph paragraph = new Paragraph();

        var key = new AnyTypeQuestionResponse.PartitionKey();
        key.setQuestionId(response.getQuestionId());
        key.setFormResponseId(formResponseId);

        paragraph.setKey(key);
        paragraph.setText(response.getText());

        paragraphRepository.save(paragraph);

        return CompletableFuture.completedFuture(null);
    }

    @Override
    @Async
    public CompletableFuture<List<ParagraphResponseIndividualDto>> getFormResponses(UUID formResponseId, List<Long> questionIds) {
        var responses = paragraphRepository.findAllByQuestionIdsAndFormResponseId(questionIds, formResponseId);

        return CompletableFuture.completedFuture(
                responses.stream().map(res -> {
                    var p = new ParagraphResponseIndividualDto();

                    p.setQuestionId(res.getKey().getQuestionId());
                    p.setQuestionType(getQuestionType());
                    p.setText(res.getText());

                    return p;
                }).toList()
        );
    }

    @Override
    @Async
    public CompletableFuture<Void> deleteAllByQuestionIdsAndFormResponseId(List<Long> questionIds, UUID formResponseId) {
        paragraphRepository.deleteAllByQuestionIdsAndFormResponseId(questionIds, formResponseId);

        return CompletableFuture.completedFuture(null);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.PARAGRAPH;
    }

}
