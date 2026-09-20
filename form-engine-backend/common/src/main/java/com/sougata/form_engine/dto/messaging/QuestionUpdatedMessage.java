package com.sougata.form_engine.dto.messaging;

import com.sougata.form_engine.dto.question.details.QuestionDetailsDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionUpdatedMessage<TQD extends QuestionDetailsDto> {
    private UUID formId;
    private TQD questionDetails;
    private Set<String> updatedFields;
}
