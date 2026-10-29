package com.sougata.form_engine.dto.messaging;

import com.sougata.form_engine.dto.question.summary.QuestionSummaryDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FormDeletedMessage {
    private UUID formId;
    private List<QuestionSummaryDto> questionSummaries;
}
