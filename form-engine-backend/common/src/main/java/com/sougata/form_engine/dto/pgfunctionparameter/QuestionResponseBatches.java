package com.sougata.form_engine.dto.pgfunctionparameter;

import com.sougata.form_engine.dto.question.responseputreqbatch.QuestionResponseBatch;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionResponseBatches<TBatch extends QuestionResponseBatch<TBatchResponse>, TBatchResponse extends QuestionResponseBatch.Response> {

    private List<TBatch> batches;

}
