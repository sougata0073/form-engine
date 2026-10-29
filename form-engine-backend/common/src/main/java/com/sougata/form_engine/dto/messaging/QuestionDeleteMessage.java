package com.sougata.form_engine.dto.messaging;

import com.sougata.form_engine.constant.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDeleteMessage {
    private UUID formId;
    private Long questionId;
    private QuestionType questionType;
}
